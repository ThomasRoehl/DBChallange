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

//    private String[] stations = new String[]{"Muehlheim am Main", "Offenbach Ost", "Offenbach Marktplatz", "Offenbach Ledermuseum", "Muehlberg", "Konstablerwache", "Hauptwache", "Frankfurt Hauptbahnhof"};

    private  Stop stop1 = new Stop("Frankfurt Hbf (tief)", "11.59", "11.59", "08.02.2017", "99" );
    private  Stop stop2 = new Stop("Frankfurt(M)Taunusanlage", "12.01", "12.01", "08.02.2017", "99" );
    private  Stop stop3 = new Stop("Frankfurt(M)Hauptwache", "12.03", "12.03", "08.02.2017", "99" );
    private  Stop stop4 = new Stop("Frankfurt(M)Konstablerwache", "12.05", "12.05", "08.02.2017", "99" );
    private  Stop stop5 = new Stop("Frankfurt(M)Ostendstraße", "12.06", "12.06", "08.02.2017", "99" );
    private  Stop stop6 = new Stop("Frankfurt(M)Mühlberg", "12.08", "12.08", "08.02.2017", "99" );
    private  Stop stop7 = new Stop("Offenbach(Main) Kaiserlei", "12.11", "12.11", "08.02.2017", "99" );
    private  Stop stop8 = new Stop("Offenbach(Main) Ledermuseum", "12.13", "12.13", "08.02.2017", "99" );
    private  Stop stop9 = new Stop("Offenbach(Main) Marktplatz", "12.15", "12.15", "08.02.2017", "99" );
    private  Stop stop10 = new Stop("Offenbach(Main)Ost", "12.18", "12.18", "08.02.2017", "99" );
    private  Stop stop11 = new Stop("Mühlheim(Main)", "12.22", "12.22", "08.02.2017", "99" );

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
        stations1.add(stop9);
        stations1.add(stop10);
        stations1.add(stop11);


        con1.setStations(stations1);
        con1.setDEST("Mühlheim(Main)");
        con1.setROUTEID(99);
        con1.setSTART("Frankfurt Hbf (tief)");
        con1.setTRAIN("S9");
        con1.setTRAINTYPE("S9 nach Hanau Hbf");
    }
    
    public ConnectionInformation getCon(int n){
        Log.d(TAG,"CALL");
        switch (n){
            case 1: return con1;
        }
        return con1;
    }
}
