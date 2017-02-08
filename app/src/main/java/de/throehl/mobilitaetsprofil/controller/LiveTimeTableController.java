package de.throehl.mobilitaetsprofil.controller;

import android.os.AsyncTask;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import de.throehl.mobilitaetsprofil.model.dbEntries.HandlerThread;
import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Route;
import de.throehl.mobilitaetsprofil.view.ConnectionSearchActivity;


/**
 * Created by thomas on 20.01.17.
 */

public class LiveTimeTableController {

    //http://www.bahnseite.de/DS100/DS100_main.html
    //http://iris.noncd.db.de/iris-tts/timetable/plan/8000105/170120/21
    //http://iris.noncd.db.de/wbt/js/index.html?bhf=FMH

    private Handler infoHandler;

    private Runnable runnable;
    public final String TAG = "LTTC";
    private String date;
    private String evaNr;
    private String hour;
    private String url;

    public LiveTimeTableController(){
        this.autoChecks = new ArrayList<HandlerThread>();
        infoHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                runCheck();
            }
        };
        infoHandler.postDelayed(runnable, 5000);
    }

    public void runCheck(){
        Log.d(TAG, "Do Run\t"+autoChecks.size());
        for(HandlerThread h: autoChecks){
            if (h.isTurnedOn()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
                String currentDateandTime = sdf.format(new Date());
//                Log.d(TAG, currentDateandTime + "\t" + h.getDate()+h.getTime());
                if (checkTimeRange(currentDateandTime, h.getDate()+h.getTime()))
                    h.run();
            }
        }
        infoHandler.postDelayed(runnable, 10000);
    }

    private void updateUrl(){
        url = "http://iris.noncd.db.de/iris-tts/timetable/plan/"+evaNr+"/"+date+"/" +hour;
    }

    private class DownloadHandler extends AsyncTask<String, Integer, String> {

        private String url;

        public DownloadHandler(String url){
            this.url = url;
        }

        @Override
        protected String doInBackground(String... urls) {
            int count = 0;
            Log.d(TAG, "START DOWNLOAD\t" + this.url);
            try {
                URL url = new URL(this.url);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                InputStream input = new BufferedInputStream(url.openStream());
                byte data[] = new byte[4096];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((count = input.read(data)) != -1) {
                    baos.write(data, 0, count);
                }
                Log.d(TAG, "FINISHED DOWNLOAD");
                return baos.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public ArrayList<Route> loadTimeTable(String stationName, String date, String hour){
        this.date = date;
        this.hour = hour;
        try {
            this.evaNr = getEvaNr(stationName);
            this.updateUrl();
            XMLParser parser = ControllerFactory.getXmlParser();
            parser.setText(new DownloadHandler(url).execute().get());
            return parser.readTimeTableXML(stationName);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEvaNr(String stationName){
        String ds100 = FileLoader.loadDs100(stationName);
        String url = "http://iris.noncd.db.de/iris-tts/timetable/station/"+ds100.trim();
        String s = null;
        try {
            s = new DownloadHandler(url).execute().get();
            if (s == null) return null;
            if (s.contains("eva=\"")){
                String tmp = s.split("eva=\"")[1];
                tmp = tmp.substring(0, tmp.indexOf("\""));
                Log.d(TAG, "eva\t"+tmp);
                return tmp;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStationLiveInfoContent(String eva){
        String url = "http://iris.noncd.db.de/iris-tts/timetable/fchg/" + eva;
        try {
            String s = new DownloadHandler(url).execute().get();
            if (s == null) return null;
            return s;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveInformation getTimeTableLiveInfo(String stationName, String date, String time){
        String eva = getEvaNr(stationName);
        this.evaNr = eva;
        this.date = date;
        this.hour = time.substring(0,2);
        this.updateUrl();
        try{
            String content = new DownloadHandler(this.url).execute().get();
            XMLParser parser = ControllerFactory.getXmlParser();
            parser.setText(content);
            Log.d(TAG, "date\t"+date);
            Log.d(TAG, "time\t"+time);
            Log.d(TAG, "Eva\t" + eva);
            String trainID = parser.findTrainByTime(date, time);
            Log.d(TAG, "TrainID\t"+trainID);
            content = getStationLiveInfoContent(eva);
            parser.setText(content);
            LiveInformation l = parser.readTimeTableLiveInfo(trainID, eva, (date+time), (date+time));
            Log.d(TAG, "LiveInfo\t"+l.toString());
            return l;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getDestTime(String stationName, String trainID, String date, String time){
        String t = time.substring(0,2);
        Log.d("SHOW", stationName + "\t" + date + "\t" + time);
        ArrayList<Route> routes = loadTimeTable(stationName, date, t);
        try{
            int i = Integer.parseInt(t);
            routes.addAll(loadTimeTable(stationName, date, (i+1)+""));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (Route r: routes){
            if (r.getID().equals(trainID)) return r.getDp();
        }
        return null;
    }

    public ArrayList<Route> findRoute(String station, String dest, String time, String date, int amount, String timeRange){
        String t = time.substring(0,2);
        ArrayList<Route> routes = loadTimeTable(station, date, t);
        try{
            int i = Integer.parseInt(t);
            routes.addAll(loadTimeTable(station, date, (i+1)+""));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "loading done!\n found:\t" + routes.size());

        ArrayList<Route> tmp = new ArrayList<Route>();
        ArrayList<Route> result = new ArrayList<Route>();

        for(Route r: routes){
            Log.d(TAG, r + "\t" + r.getRouteAfter());
            if (r.getRouteAfter().contains(dest)){
                tmp.add(r);
            }
            if (r.getRouteAfter().contains(dest.replace(" ", ""))) tmp.add(r);
        }

        Log.d(TAG, "finding same done!");

        for (Route r: tmp){
            if (checkTime(date+time, r.getDp(), timeRange)){
                Log.d(TAG, r.toString());
                result.add(r);
            }
        }

        Log.d(TAG, "checking time done!");
        return result;
    }

    private boolean checkTime(String orgT, String newT, String range){
        try {
            int a = Integer.parseInt(orgT);
            int b = Integer.parseInt(newT);
            int r = Integer.parseInt(range);
            int x = Integer.parseInt(orgT.substring(8));
            int z = 0;
            int y = r;
            if (x + r > 60){
                z = (x+r)%60;
                y = ((x+r)/60)*100;
            }
            Log.d(TAG, a + "\t" + b + "\t" + x + "\t" + y + "\t" + z);
            if (a < b && a+z+y > b){

                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkTimeRange(String cT, String nT){
        try {
            int a = Integer.parseInt(cT);
            int b = Integer.parseInt(nT);
            int h = Integer.parseInt(cT.substring(6,8));

            if (a > b) return false;
            if (h + 1 == 24) a = a - 2300 + 10000;
//            Log.d(TAG, ""+a + "\t" + b);
            if (a + 100 > b) return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    ArrayList<HandlerThread> autoChecks;

    public void startAutoCheck(final String stationName, String date, String time, final String trainID){
//        Log.d(TAG, "Start:\t"+stationName+"\t"+trainID);
        final HandlerThread h = new HandlerThread(date, time, trainID, stationName);
        if (!autoChecks.contains(h)){
//            Log.d(TAG, "Not Contains");
            h.setHandler(new Handler());
            h.setRunnable(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "" + h.getStationID() + "\t" + h.getTrainID() + "\t" + h.getDate()+h.getTime());
                }
            });
            h.setTurnedOn(true);
            autoChecks.add(h);
        }
        else{
//            Log.d(TAG, "Contains");
            if (!autoChecks.get(autoChecks.indexOf(h)).isTurnedOn()){
                autoChecks.get(autoChecks.indexOf(h)).setTurnedOn(true);
            }
        }
    }

    public void stopAutoCheck(String stationName, String trainID){
//        Log.d(TAG, "Turn Off\t" + stationName + "\t" + trainID);
        HandlerThread h = new HandlerThread("", "", trainID, stationName);
        if (autoChecks.contains(h)){
//            Log.d(TAG, "Contains");
            autoChecks.get(autoChecks.indexOf(h)).setTurnedOn(false);
//            Log.d(TAG, "Still turned on\t"+autoChecks.get(autoChecks.indexOf(h)).isTurnedOn());
        }
    }


}
