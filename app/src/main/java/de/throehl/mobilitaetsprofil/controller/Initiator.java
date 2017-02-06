package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;

import de.throehl.mobilitaetsprofil.dummy.DummyLiveInformation;
import de.throehl.mobilitaetsprofil.dummy.DummyTravelPlan;

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
        xmlParser.readLiveInfoXML();

        xmlParser.loadFile("liveInformation/1000.xml");
        xmlParser.initParser();
        xmlParser.readLiveInfoXML();

        xmlParser.loadFile("liveInformation/1028.xml");
        xmlParser.initParser();
        databaseController.insertLiveInformationMulti(xmlParser.readLiveInfoXML());
    }

    public void initXmlParserTT(){
        XMLParser xmlParser = ControllerFactory.getXmlParser();
        xmlParser.loadFile("timetable/timetable.xml");
        xmlParser.initParser();
        xmlParser.readTimeTableXML("");
    }

    public String test(){
//        String trainID = "8873351982035387838-1701261858-10";
//        String stationID = "8004647";
//        String text = FileLoader.loadFile("timetable/liveInfo.xml");
        String s = ControllerFactory.getTtController().getTimeTableLiveInfo("Frankfurt (Main) Hbf", "170126", "2151").toString();
        if (s != null)
            return s;
        else return "null";
//        ControllerFactory.getXmlParser().readTimeTableLiveInfo(trainID, stationID, "1701261919", "1701261920");

//        FileLoader.loadStationNames("Frankfurt");
    }

    public void newHandler(String statioName, String date, String time, String trainID){
        ControllerFactory.getTtController().startAutoCheck(statioName, date, time, trainID);
    }

    public void stopHandler(String stationName, String trainID){
        ControllerFactory.getTtController().stopAutoCheck(stationName, trainID);
    }



}
