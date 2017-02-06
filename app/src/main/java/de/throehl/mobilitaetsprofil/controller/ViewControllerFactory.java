package de.throehl.mobilitaetsprofil.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by thomas on 04.02.17.
 */

public abstract class ViewControllerFactory {

    private static final String TAG = "ViewControllerFactory";

    private static HashMap<String, Activity> activities = new HashMap<String, Activity>();

    public static void addActivity(String name, Activity act){
        if (!activities.containsKey(name)){
            activities.put(name, act);
            Log.d(TAG, "Registered\t"+name);
        }
        else Log.e(TAG, "Activity " + name + " already registered");
    }

    public static void closeActivity(String name){

        if (activities.containsKey(name)){
            activities.get(name).finish();
            activities.remove(name);
            Log.d(TAG, "Removed\t"+name);
        }
        else Log.e(TAG, "Activity not found:\t" + name);
    }

    public static Activity getActivity(String name){
        return activities.get(name);
    }

    public static boolean containsActivity(String name){
        if (activities.containsKey(name)) return true;
        return false;
    }

    public static void printActivities(){
        Log.d(TAG, "Print Activities");
        for (String s: activities.keySet()){
            Log.d(TAG, s);
        }
    }
}
