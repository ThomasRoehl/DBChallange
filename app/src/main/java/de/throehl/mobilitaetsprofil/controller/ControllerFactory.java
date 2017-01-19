package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;

/**
 * Created by thomas on 19.01.17.
 */

public abstract class ControllerFactory {

    private static Context APPLICATION_CONTEXT;
    private static DatabaseController DB_CONTROLLER;
    private static LiveInformationController LI_CONTROLLER;
    private static JsonParser JSON_PARSER;

    public static void initController(Context context){
        APPLICATION_CONTEXT = context;
        DB_CONTROLLER = new DatabaseController(APPLICATION_CONTEXT);
        LI_CONTROLLER = new LiveInformationController(APPLICATION_CONTEXT);
        JSON_PARSER = new JsonParser(APPLICATION_CONTEXT);
    }

    public static DatabaseController getDbController(){
        return DB_CONTROLLER;
    }

    public static LiveInformationController getLiController(){
        return LI_CONTROLLER;
    }

    public static JsonParser getJsonParser(){
        return JSON_PARSER;
    }


}
