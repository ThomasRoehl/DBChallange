package de.throehl.mobilitaetsprofil.model.dbEntries;

import java.util.ArrayList;

/**
 * Created by thomas on 25.01.17.
 */

public class Route {

    private final String ID;
    private ArrayList<String> routeAfter;
    private ArrayList<String> routeBefore;
    private String currentStop;
    private String trainName;
    private String track;
    private String dp;

    public Route(String ID) {
        this.ID = ID;
        this.routeAfter = new ArrayList<String>();
        this.routeBefore = new ArrayList<String>();
    }

    public String getID() {
        return ID;
    }

    public ArrayList<String> getRoute() {
        ArrayList<String> route = new ArrayList<String>();
        route.addAll(routeBefore);
        route.add(currentStop);
        route.addAll(routeAfter);
        return route;
    }

    public ArrayList<String> getRouteBefore() {
        return routeBefore;
    }

    public ArrayList<String> getRouteAfter() {
        return routeAfter;
    }

    public void addStopAfter(String stop){
        this.routeAfter.add(stop);
    }

    public void addStopBefore(String stop){
        this.routeBefore.add(stop);
    }

    public String getCurrentStop() {
        return currentStop;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrack() {
        return track;
    }

    public String getDp() {
        return dp;
    }

    public void setCurrentStop(String currentStop) {
        this.currentStop = currentStop;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    @Override
    public String toString() {
        return  "train='" + trainName + '\'' +
                ", track='" + track + '\'' +
                ", dp='" + dp + '\'' +
                '}';
    }
}
