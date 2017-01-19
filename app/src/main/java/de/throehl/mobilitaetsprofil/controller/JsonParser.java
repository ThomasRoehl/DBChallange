package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by thomas on 19.01.17.
 */

public class JsonParser {

    private Context context;
    private String currentText = null;
    public final String TAG = "JsonParser";

    public JsonParser(Context context){
        this.context = context;
    }

    public void loadFile(String filename){
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
        else currentText = res;
    }

    public HashMap<String, String> loadLocation(){
        if (currentText == null){
            Log.e(TAG, "no file loaded for Parsing");
            return null;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            JSONObject jsonObj = new JSONObject(currentText);
            JSONObject root = jsonObj.getJSONObject("LocationList");
            JSONArray stops = root.getJSONArray("StopLocation");
            for(int i = 0; i < stops.length(); i++){
                JSONObject stop = stops.getJSONObject(i);
                map.put(stop.getString("id"), stop.getString("name"));
            }
        }
        catch (Exception e){
            Log.e(TAG, e.getStackTrace().toString());
        }

        return map;
    }

    public void printMap(HashMap<String, String> map){
        for(String s: map.keySet()){
            Log.d(TAG, map.get(s) + "\t\t\t" + s);
        }

    }


}
