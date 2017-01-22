package de.throehl.mobilitaetsprofil.controller;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;

/**
 * Created by thomas on 22.01.17.
 */

public class XMLParser {

    private static final String ns = null;
    private final String TAG = "XML-Parser";
    private String currentText = null;
    private String filename;
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

    public ArrayList<LiveInformation> readXML(){
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
                }
                if (name.equals("Zeit")){
                    destTime = parser.getAttributeValue(ns, "Soll");
                    delay = parser.getAttributeValue(ns, "Prog");
//                    Log.d(TAG, parser.getAttributeValue(ns, "Soll"));
//                    Log.d(TAG, parser.getAttributeValue(ns, "Prog"));
                }
                if (!station.equals("") && !destTime.equals("")){
                    Log.d(TAG, "Entry added!");
                    entries.add(new LiveInformation(station, trainID, description, destTime, delay));
                    station = "";
                    description = "";
                    destTime = "";
                    delay = "";
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
