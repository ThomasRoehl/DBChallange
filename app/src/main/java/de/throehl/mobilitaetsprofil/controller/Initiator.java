package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.dummy.DummyLiveInformation;
import de.throehl.mobilitaetsprofil.dummy.DummyTravelPlan;
import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;

/**
 * Created by thomas on 22.01.17.
 */

public class Initiator {

    private DummyTravelPlan dummy;
    private DummyLiveInformation dummyL;
    private final String TAG = "Initiator";

    public Initiator(Context context){
        dummy = new DummyTravelPlan();
        dummyL = new DummyLiveInformation(16, 50, 2, 20);
        ControllerFactory.initController(context);
    }

    public void emptyDB(){
        DatabaseController databaseController = ControllerFactory.getDbController();
        databaseController.emptyTables();
    }

    public void initTravelPlans(){
        DatabaseController databaseController = ControllerFactory.getDbController();
        databaseController.insertTravelPlan(dummy.getCon(1));
        databaseController.insertTravelPlan(dummy.getCon(2));
    }

    public void initLiveInfo(){
        DatabaseController databaseController = ControllerFactory.getDbController();
        databaseController.insertLiveInformationMulti(dummyL.getLiveInfos());
    }

    public void initJsonParser(){
        DatabaseController databaseController = ControllerFactory.getDbController();
        JsonParser parser = ControllerFactory.getJsonParser();
        parser.loadFile("fahrplanAPI/locations.json");
        parser.printMap(parser.loadLocation());
    }

    public void initXmlParser(){
        DatabaseController databaseController = ControllerFactory.getDbController();
        XMLParser xmlParser = ControllerFactory.getXmlParser();
        xmlParser.loadFile("liveInformation/100.xml");
        xmlParser.initParser();
        xmlParser.readXML();

        xmlParser.loadFile("liveInformation/1000.xml");
        xmlParser.initParser();
        xmlParser.readXML();

        xmlParser.loadFile("liveInformation/1028.xml");
        xmlParser.initParser();
        databaseController.insertLiveInformationMulti(xmlParser.readXML());
    }



}
