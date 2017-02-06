package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Route;

/**
 * Created by thomas on 22.01.17.
 */

public class XMLParser {

    private static final String ns = null;
    private final String TAG = "XML-Parser";
    private String currentText = null;
    private String filename = "null";
    private XmlPullParser parser = null;
    private Context context;
    private String trainID = "null";
    private String trainName = "null";
    private ArrayList<LiveInformation> entries = null;

    public XMLParser(Context con){
        entries = new ArrayList<LiveInformation>();
        context = con;
    }

    public boolean loadFile(String filename){
        if (FileLoader.context == null) FileLoader.initFileLoader(this.context);
        currentText = FileLoader.loadFile(filename);
        this.filename = filename;
        this.entries.clear();
        this.trainID = "null";
        this.trainName = "null";
        return initParser();
    }

    public boolean setText(String text){
        this.currentText = text;
        return initParser();
    }


    public boolean initParser(){
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(currentText));
            parser.nextTag();
            return true;
        }
        catch(Exception e){
            Log.e(TAG, "Failed initializing XML-Parser");
        }
        return false;
    }

    public ArrayList<LiveInformation> readLiveInfoXML(){
        try{
            parser.require(XmlPullParser.START_TAG, ns, "Paket");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                String name = parser.getName();
                if (name.equals("Fahrtposition") || name.equals("Anschlussbewertung")){
                    Log.e(TAG, "No LiveInformation in this file \t"+filename);
                    return null;
                }
                if (name.equals("Zug")){
                    trainID = parser.getAttributeValue(ns, "Nr");
                    trainName = parser.getAttributeValue(ns, "Name");
                }
                if (name.equals("ZE")){
                    readZE(parser);
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "Failed reading XML");
            e.printStackTrace();
        }
        return entries;
    }

    private void readZE(XmlPullParser parser){
        try{
            parser.require(XmlPullParser.START_TAG, ns, "ZE");
            String station = "";
            String description = "";
            String destTime = "";
            String delay = "";
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                String name = parser.getName();

                if (name.equals("Bf")){
                    station = parser.getAttributeValue(ns, "EvaNr");
//                    Log.d(TAG, parser.getAttributeValue(ns, "Name"));
//                    Log.d(TAG, parser.getAttributeValue(ns, "EvaNr"));
                    continue;
                }
                if (name.equals("Zeit")){
                    destTime = parser.getAttributeValue(ns, "Soll");
                    delay = parser.getAttributeValue(ns, "Prog");
//                    Log.d(TAG, parser.getAttributeValue(ns, "Soll"));
//                    Log.d(TAG, parser.getAttributeValue(ns, "Prog"));
                    continue;
                }
                if (!station.equals("") && !destTime.equals("")){
                    entries.add(new LiveInformation(station, trainID, description, destTime, delay));
                    station = "";
                    description = "";
                    destTime = "";
                    delay = "";
                    continue;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Route> readTimeTableXML(String stationName){
        ArrayList<Route> route = new ArrayList<Route>();
        try{
            parser.require(XmlPullParser.START_TAG, ns, "timetable");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                String name = parser.getName();
                if (name.equals("s")){
                    parser.require(XmlPullParser.START_TAG, ns, "s");
                    continue;
                }
                if (name.equals("tl")){
//                    Log.d(TAG, parser.getAttributeValue(ns, "c"));
                    trainName = parser.getAttributeValue(ns, "c");
                    continue;
                }
                if (name.equals("ar")){
//                    Log.d(TAG, "ARDate\t"+parser.getAttributeValue(ns, "pt"));
//                    Log.d(TAG, "ARTrack\t"+parser.getAttributeValue(ns, "pp"));
//                    Log.d(TAG, "ARTrainNumber\t"+parser.getAttributeValue(ns, "l"));
//                    Log.d(TAG, "ARPath\t"+parser.getAttributeValue(ns, "ppth"));
                    continue;
                }
                if (name.equals("dp")){
//                    Log.d(TAG, "DPDate\t"+parser.getAttributeValue(ns, "pt"));
//                    Log.d(TAG, "DPTrack\t"+parser.getAttributeValue(ns, "pp"));
//                    Log.d(TAG, "DPTrainNumber\t"+parser.getAttributeValue(ns, "l"));
//                    Log.d(TAG, "DPPath\t"+ Arrays.toString(parser.getAttributeValue(ns, "ppth").split("\\|")));
                    Route r = new Route("1");
                    r.setTrainName(trainName + parser.getAttributeValue(ns, "l"));
                    r.setCurrentStop(stationName);
                    r.setTrack(parser.getAttributeValue(ns, "pp"));
                    r.setDp(parser.getAttributeValue(ns, "pt"));
                    for (String s: parser.getAttributeValue(ns, "ppth").split("\\|")){
                        r.addStopAfter(s);
                    }
//                    Log.d(TAG, r.toString());
                    route.add(r);
                    continue;
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "Failed reading XML");
            e.printStackTrace();
        }
        Log.d(TAG, ""+route.size());
        return route;
    }

    public LiveInformation readTimeTableLiveInfo(String trainID, String stationID, String orgAr, String orDp){
        Log.d(TAG, "READ TIME TABLE LIVE INFO");
        LiveInformation info = null;
        String newAr = null;
        String newDp = null;
        boolean found = false;
        try{
            parser.require(XmlPullParser.START_TAG, ns, "timetable");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                String name = parser.getName();
                if (name.equals("s")){
                    parser.require(XmlPullParser.START_TAG, ns, "s");
                    if (parser.getAttributeValue(ns, "id").equals(trainID)){
                        found = true;
                    }
                    continue;
                }
                if (name.equals("ar") && found){
                    newAr = parser.getAttributeValue(ns, "ct");
                    continue;
                }
                if (name.equals("dp") && found){
                    newDp = parser.getAttributeValue(ns, "ct");
                    continue;
                }
                if (newDp != null && newAr != null){
                    break;
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "Failed reading XML");
            e.printStackTrace();
        }

        if ((newAr != null && newDp != null)){
            info = new LiveInformation(stationID, trainID, getTimeDiff(orDp, newDp), orDp, newDp);
            Log.d(TAG, info.toString());
        }
        else{
            Log.d(TAG, "No LiveInfo found");
        }
        return info;
    }

    public String findTrainByTime(String date, String time){
        String trainID = null;

        try{
            parser.require(XmlPullParser.START_TAG, ns, "timetable");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() != XmlPullParser.START_TAG) continue;
                String name = parser.getName();
                if (name.equals("s")){
                    parser.require(XmlPullParser.START_TAG, ns, "s");
                    trainID = parser.getAttributeValue(ns, "id");
                    continue;
                }
                if (name.equals("dp")){
                    String dp = parser.getAttributeValue(ns, "pt");
//                    Log.d(TAG, "DP\t"+dp);
//                    Log.d(TAG, "D+T\t"+(date+time));
                    if (dp.equals(date+time)) return trainID;
                    continue;
                }
            }
        }
        catch (Exception e){
            Log.e(TAG, "Failed reading XML");
            e.printStackTrace();
        }
        return null;
    }

    //TODO check time new day change
    public static String getTimeDiff(String oldTime, String newTime){
        String timeO = oldTime.substring(6);
        String timeN = newTime.substring(6);
        int oh = Integer.parseInt(timeO.substring(0,2));
        int nh = Integer.parseInt(timeN.substring(0,2));
        int om = Integer.parseInt(timeO.substring(2));
        int nm = Integer.parseInt(timeN.substring(2));

        int dm = nm - om;
        int dh = nh - oh;

        if (dh < 1){
            return dm+" minutes delay";
        }
        else{
            return dh+"."+dm+" hours delay";
        }
    }
}
