package de.throehl.mobilitaetsprofil.dummy;

import java.util.ArrayList;
import java.util.Random;

import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;

/**
 * Created by thomas on 17.01.17.
 */

public class DummyLiveInformation {

    public final String TAG = "DummyLiveInfo";

    private String[] stations = new String[]{"Muehlheim am Main", "Offenbach Ost", "Offenbach Marktplatz", "Offenbach Ledermuseum", "Muehlberg", "Konstablerwache", "Hauptwache", "Frankfurt Hauptbahnhof"};
    private String[] trainIDs = new String[]{"0"};
    private String[] destTimes;
    private String[] delays = new String[]{"5min", "10min", "15min", "20min", "25min", "30min", "35min", "40min", "45min", "50min", };
    private String[] descrs = new String[]{"d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10"};

    private ArrayList<LiveInformation> liveInfos;

    public DummyLiveInformation(int HH, int MM, int steps, int number){
        liveInfos = new ArrayList<LiveInformation>();
        setCurrentTimes(HH, MM, steps, number);
        setEntries(number);
    }

    public void setCurrentTimes(int HH, int MM, int steps, int number){
        destTimes = new String[number+1];
        destTimes[0] = "" + HH + ":" + MM;
        for(int i = 1; i < number; i++){
            MM = MM + steps;
            if (MM >= 60){
                HH += MM / 60;
                MM = MM % 60;
                HH = HH % 24;
            }
            destTimes[i] = "" + HH + ":" + MM;
        }
    }

    public void setEntries(int number){
        int ds = stations.length;
        int dt = trainIDs.length;
        int ddest = destTimes.length;
        int ddel = delays.length;
        int ddesc = descrs.length;

        Random r = new Random();
        for(int i = 0; i < number; i ++){
            String station = stations[r.nextInt(ds)];
            String trainId = trainIDs[r.nextInt(dt)];
            String destTime = destTimes[r.nextInt(ddest)];
            String delay = delays[r.nextInt(ddel)];
            String descr = descrs[r.nextInt(ddesc)];
            liveInfos.add(new LiveInformation(station, trainId, descr, destTime, delay));
        }
    }

    public ArrayList<LiveInformation> getLiveInfos(){
        return this.liveInfos;
    }

}
