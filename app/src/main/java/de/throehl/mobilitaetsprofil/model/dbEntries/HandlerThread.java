package de.throehl.mobilitaetsprofil.model.dbEntries;

import android.os.Handler;
import android.util.Log;

/**
 * Created by thomas on 28.01.17.
 */

public class HandlerThread {

    private Handler handler;
    private Runnable runnable;
    private String date;
    private String time;
    private String stationID;
    private String trainID;
    private boolean turnedOn;

    public HandlerThread(String date, String time, String trainID, String stationID) {
        this.date = date;
        this.time = time;
        this.trainID = trainID;
        this.stationID = stationID;
    }

    public Handler getHandler() {
        return handler;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTrainID() {
        return trainID;
    }

    public String getStationID() {
        return stationID;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        if (turnedOn == false) this.handler.removeCallbacks(runnable);
        this.turnedOn = turnedOn;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run(){
        this.handler.post(runnable);
    }

    @Override
    public boolean equals(Object obj){
        boolean res = false;
        if (!obj.getClass().equals(this.getClass())) res = false;
        HandlerThread h = (HandlerThread) obj;
        if (this.stationID.equals(h.getStationID()) &&
                this.trainID.equals(h.getTrainID())) res = true;
//        Log.d("LTTC", "Equals\t"+res + "\t" + this.stationID + "\t" + h.stationID);
        return res;
    }
}
