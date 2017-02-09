package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.view.CalendarCollection;

import static android.R.attr.id;

/**
 * Created by thomas on 19.01.17.
 */

public abstract class ControllerFactory {

    private static Context APPLICATION_CONTEXT;
    private static DatabaseController DB_CONTROLLER;
    private static LiveInformationController LI_CONTROLLER;
    private static LiveTimeTableController TT_CONTROLLER;
    private static JsonParser JSON_PARSER;
    private static XMLParser XML_PARSER;
    private static String USERID;

    private static final String TAG = "ControllerFactory";

    public static void initController(Context context){
        APPLICATION_CONTEXT = context;
        DB_CONTROLLER = new DatabaseController(APPLICATION_CONTEXT);
        LI_CONTROLLER = new LiveInformationController(APPLICATION_CONTEXT);
        JSON_PARSER = new JsonParser(APPLICATION_CONTEXT);
        XML_PARSER = new XMLParser(APPLICATION_CONTEXT);
        TT_CONTROLLER = new LiveTimeTableController();
        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
        FileLoader.initFileLoader(APPLICATION_CONTEXT);
    }



    public static void setID(String id){
        USERID = id;
        Log.d(TAG, "USER ID:\t"+id);
    }

    public static String getID(){ return USERID;}

    public static DatabaseController getDbController(){
        return DB_CONTROLLER;
    }

    public static LiveInformationController getLiController(){
        return LI_CONTROLLER;
    }

    public static JsonParser getJsonParser(){
        return JSON_PARSER;
    }

    public static XMLParser getXmlParser(){
        return XML_PARSER;
    }

    public static LiveTimeTableController getTtController() { return TT_CONTROLLER; }

    public static Context getAppContext(){
        return APPLICATION_CONTEXT;
    }

}
