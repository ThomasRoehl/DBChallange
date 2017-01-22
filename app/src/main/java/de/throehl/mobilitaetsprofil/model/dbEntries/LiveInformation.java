package de.throehl.mobilitaetsprofil.model.dbEntries;

/**
 * Created by thomas on 17.01.17.
 */

public class LiveInformation {
    private String stationID;
    private String trainID;
    private String description;
    private String destTime;
    private String delay;

    public LiveInformation(String stationID, String trainID, String description, String destTime, String delay) {
        this.stationID = stationID;
        this.trainID = trainID;
        this.description = description;
        this.destTime = destTime;
        this.delay = delay;
    }

    public String getStationID() {
        return stationID;
    }

    public String getTrainID() {
        return trainID;
    }

    public String getDescription() {
        return description;
    }

    public String getDestTime() {
        return destTime;
    }

    public String getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "LiveInformation{" +
                "stationID='" + stationID + '\'' +
                ", trainID='" + trainID + '\'' +
                ", description='" + description + '\'' +
                ", destTime='" + destTime + '\'' +
                ", delay='" + delay + '\'' +
                '}';
    }
}
