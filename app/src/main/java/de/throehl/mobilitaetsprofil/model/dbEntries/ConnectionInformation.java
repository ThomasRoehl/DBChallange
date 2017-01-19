package de.throehl.mobilitaetsprofil.model.dbEntries;

import java.util.ArrayList;

/**
 * Created by thomas on 15.01.17.
 */

public class ConnectionInformation {
    private String START;
    private String DEST;
    private ArrayList<Stop> stations;
    private String TRAIN;
    private String TRAINTYPE;
    private int ROUTEID;

    public int getROUTEID() {
        return ROUTEID;
    }

    public void setROUTEID(int ROUTEID) {
        this.ROUTEID = ROUTEID;
    }

    public ConnectionInformation(){
        stations = new ArrayList<Stop>();
    }

    public String getSTART() {
        return START;
    }

    public void setSTART(String START) {
        this.START = START;
    }

    public String getDEST() {
        return DEST;
    }

    public void setDEST(String DEST) {
        this.DEST = DEST;
    }

    public String getTRAIN() {
        return TRAIN;
    }

    public void setTRAIN(String TRAIN) {
        this.TRAIN = TRAIN;
    }

    public String getTRAINTYPE() {
        return TRAINTYPE;
    }

    public void setTRAINTYPE(String TRAINTYPE) {
        this.TRAINTYPE = TRAINTYPE;
    }

    public ArrayList<Stop> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Stop> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        String res = "ConnectionInformation{" +
                "START='" + START + '\'' + '\n' +
                "DEST='" + DEST + '\'' + '\n' +
                "TRAIN='" + TRAIN + '\'' + '\n' +
                "TRAINTYPE='" + TRAINTYPE + '\'' + '\n' +
                "ROUTEID=" + ROUTEID + '\n';
        for(Stop s: stations){
            res += s.toString() + '\n';
        }
        res +="}";
        return res;
    }
}