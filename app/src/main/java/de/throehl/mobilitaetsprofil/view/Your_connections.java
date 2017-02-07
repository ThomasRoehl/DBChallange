package de.throehl.mobilitaetsprofil.view;

/**
 * Created by tetiana on 26.01.17.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.model.AdapterForList;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;


public class Your_connections extends Fragment{

    private ArrayList<String> connections;
    private ArrayList<String> alarm;
    private ArrayList<String> repeat;
    private ArrayList<Integer> ids;
    private AdapterForList adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.your_connections, container, false);

        Log.d("CSA", "onCreate YourConnections");



//        String[] your_connections_list = new String[] {
//                "Mo. 23.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache",
//                "Mo. 24.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache",
//                "Mo. 25.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache",
//                "Mo. 26.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache",
//                "Mo. 27.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache",
//                "Mo. 23.02.2017\n S9 Richtung Wiesbadeb\n -> 20:00 Mühlheim am Main \n -> 20:26 Konstabler Wache"};
//
//        String[] itteration_list = new String[] {
//                "aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus"
//           };
//
//        String[] dalay_list = new String[] {
//                "aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus","aus",
//        };

        connections = new ArrayList<String>();
        alarm = new ArrayList<String>();
        repeat = new ArrayList<String>();
        ids = new ArrayList<Integer>();



        listView = (ListView) rootView.findViewById(R.id.conList);
        adapter = new AdapterForList(rootView.getContext(), android.R.layout.simple_list_item_1, connections, repeat, alarm);
        listView.setAdapter(adapter);

        initEntries();
        return rootView;
    }

    public void initEntries(){
        Log.d("CSA", "initEntries");
        ArrayList<String> routes = ControllerFactory.getDbController().getUserSaves(ControllerFactory.getID());
        connections.clear();
        alarm.clear();
        repeat.clear();
        for(String s: routes){
            Log.d("CSA", s);
            try{
                int i = Integer.parseInt(s);
                ConnectionInformation con = ControllerFactory.getDbController().getTravelPlan(i);
                String entry = con.getStations().get(0).getDATE() + "\n" + con.getTRAINTYPE() + "\n" + con.getStations().get(0).getDEPARTURE() + "\n" + con.getStations().get(0).getNAME() + "\n" + con.getStations().get(con.getStations().size()-1).getDEPARTURE() + "\n" + con.getStations().get(con.getStations().size()-1).getNAME();
                connections.add(entry);
                alarm.add("aus");
                repeat.add("an");
                ids.add(0);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        adapter.updateLists(connections, repeat, alarm);
    }

//    public void addEntry(int id, String date, String train, String start, String time1, String dest, String time2){
//        String entry = date + "\n" + train + "\n" + time1 + "\n" + start + "\n" + time2 + "\n" + dest;
//        connections.add(entry);
//        alarm.add("aus");
//        repeat.add("an");
//        ids.add(id);
////        Log.d("CSA", connections.toString());
//        adapter.updateLists(connections, repeat, alarm);
//    }
}