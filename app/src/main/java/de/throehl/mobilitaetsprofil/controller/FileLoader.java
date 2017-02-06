package de.throehl.mobilitaetsprofil.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.view.MainActivity;

/**
 * Created by thomas on 22.01.17.
 */

public abstract class FileLoader {

    public static Context context = null;
    private static final String TAG = "FileLoader";

    public static void initFileLoader(Context con){
        context = con;
    }

    public static String loadFile(String filename){
        AssetManager assetManager = context.getAssets();
        String res = null;
        try {
            InputStream inp = assetManager.open(filename);
            byte[] buffer = new byte[inp.available()];
            inp.read(buffer);
            inp.close();
            res = new String(buffer);
        }
        catch (Exception e){
            Log.e(TAG, e.getStackTrace().toString());
        }
        if (res==null) Log.e(TAG, "couldn't load File: " + filename);
        else return res;
        return null;
    }

    public static String loadDs100(String stationName){
        AssetManager assetManager = context.getAssets();
        String res = null;
        try {
            InputStream inp = assetManager.open("DS100/ds100.csv");
            byte[] buffer = new byte[inp.available()];
            inp.read(buffer);
            inp.close();
            res = new String(buffer);
            int i = res.indexOf(stationName);
            res = res.substring(i-6,i-1);
            if (res.contains("\n")){
                res = res.substring(res.indexOf("\n"));
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getStackTrace().toString());
        }

        return res;
    }

    public static ArrayList<String> loadStationNames(String stationName){
        AssetManager assetManager = context.getAssets();
        String res = null;
        ArrayList<String> res_l = new ArrayList<String>();
        try {
            InputStream inp = assetManager.open("DS100/ds100.csv");
            byte[] buffer = new byte[inp.available()];
            inp.read(buffer);
            inp.close();
            res = new String(buffer);
            while (res.contains(stationName)){
                int x = res.indexOf(stationName);
                res = res.substring(x);
                res_l.add(res.substring(0,res.indexOf(",")));
                res = res.substring(res.indexOf(","));
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getStackTrace().toString());
        }
        for (String s: res_l){
            Log.d(TAG, s);
        }
        return res_l;
    }


    private static class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int count = 0;
            String result = "";
            try {
                URL url = new URL((String) params[0]);
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
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Log.d(TAG, "DOWNLOAD FAILED");
            return null;
        }
    }

    public static Context ac;

    public static void downloadFile(Context con){
        ac = con;
        String hour = "";
        String evaNr = "8000105";
        String date = "";
        String url = "http://iris.noncd.db.de/iris-tts/timetable/fchg/" + evaNr;
        try {
            String s = new DownloadFile().execute(url).get();
            Log.d(TAG, s.length()+"");
        }
        catch (Exception e){

        }
    }
}
