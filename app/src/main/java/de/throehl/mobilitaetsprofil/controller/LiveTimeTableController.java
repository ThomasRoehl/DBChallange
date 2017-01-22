package de.throehl.mobilitaetsprofil.controller;

import android.os.AsyncTask;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by thomas on 20.01.17.
 */

public class LiveTimeTableController extends AsyncTask<String, Integer, String>{

    //http://www.bahnseite.de/DS100/DS100_main.html
    //http://iris.noncd.db.de/iris-tts/timetable/plan/8000105/170120/21
    //http://iris.noncd.db.de/wbt/js/index.html?bhf=FMH

    public final String TAG = "LTTC";
    public final String url = "http://iris.noncd.db.de/wbt/js/index.html?bhf=FMH";

    @Override
    protected String doInBackground(String... params) {
        return null;
    }

//    @Override
//    protected String doInBackground(String... urls) {
//        String result = "";
//
//        HttpURLConnection urlConnection = null;
//
//        try {
//            URL url = new URL(this.url);
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            InputStream in = urlConnection.getInputStream();
//
//            InputStreamReader reader = new InputStreamReader(in);
//
//            int data = reader.read();
//
//            while (data != -1) {
//
//
//                char current = (char) data;
//
//                result += current;
//
//                data = reader.read();
//
//
//
//            }
//
//            return result;
//
//
//        } catch (MalformedURLException e) {
//
//
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }
}
