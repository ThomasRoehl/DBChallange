package de.throehl.mobilitaetsprofil.dummy;

import android.util.Log;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Stop;

/**
 * Created by thomas on 15.01.17.
 */

public class DummyTravelPlan {

    public final String TAG = "Dummy";

    private String[] stations = new String[]{"Muehlheim am Main", "Offenbach Ost", "Offenbach Marktplatz", "Offenbach Ledermuseum", "Muehlberg", "Konstablerwache", "Hauptwache", "Frankfurt Hauptbahnhof"};

    private  Stop stop1 = new Stop("Muehlheim am Main", "08:08", "08:09", "2016-10-10", 0 );
    private  Stop stop2 = new Stop("Offenbach Ost", "08:10", "08:11", "2016-10-10", 0 );
    private  Stop stop3 = new Stop("Offenbach Marktplatz", "08:15", "08:16", "2016-10-10", 0 );
    private  Stop stop4 = new Stop("Offenbach Ledermuseum", "08:20", "08:21", "2016-10-10", 0 );
    private  Stop stop5 = new Stop("Muehlberg", "08:25", "08:26", "2016-10-10", 0 );
    private  Stop stop6 = new Stop("Konstablerwache", "08:30", "08:31", "2016-10-10", 0 );
    private  Stop stop7 = new Stop("Hauptwache", "08:35", "08:36", "2016-10-10", 0 );
    private  Stop stop8 = new Stop("Frankfurt Hauptbahnhof", "08:40", "08:41", "2016-10-10", 0 );

    private  ConnectionInformation con1 = new ConnectionInformation();
    private  ConnectionInformation con2 = new ConnectionInformation();

    public DummyTravelPlan(){
        ArrayList<Stop> stations1 = new ArrayList<Stop>();
        stations1.add(stop1);
        stations1.add(stop2);
        stations1.add(stop3);
        stations1.add(stop4);
        stations1.add(stop5);
        stations1.add(stop6);
        stations1.add(stop7);
        stations1.add(stop8);

        ArrayList<Stop> stations2 = new ArrayList<Stop>();
        stations2.add(stop8);
        stations2.add(stop7);
        stations2.add(stop6);
        stations2.add(stop5);
        stations2.add(stop4);
        stations2.add(stop3);
        stations2.add(stop2);
        stations2.add(stop1);

        con1.setStations(stations1);
        con1.setDEST("Frankfurt Hauptbahnhof");
        con1.setROUTEID(1);
        con1.setSTART("Muehlheim am Main");
        con1.setTRAIN("S9");
        con1.setTRAINTYPE("S-Bahn");

        con2.setStations(stations2);
        con2.setDEST("Muehlheim am Main");
        con2.setROUTEID(2);
        con2.setSTART("Frankfurt Hauptbahnhof");
        con2.setTRAIN("S8");
        con2.setTRAINTYPE("S-Bahn");
    }
    
    public ConnectionInformation getCon(int n){
        Log.d(TAG,"CALL");
        switch (n){
            case 1: return con1;
            case 2: return con2;
        }
        return con1;
    }
}
