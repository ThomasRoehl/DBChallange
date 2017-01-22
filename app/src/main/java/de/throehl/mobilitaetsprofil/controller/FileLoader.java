package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;

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
}
