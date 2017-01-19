package de.throehl.mobilitaetsprofil.model.dbEntries;

/**
 * Created by thomas on 17.01.17.
 */

public class LiveInformation {
    private String station;
    private String trainID;
    private String description;
    private String destTime;
    private String delay;

    public LiveInformation(String station, String trainID, String description, String destTime, String delay) {
        this.station = station;
        this.trainID = trainID;
        this.description = description;
        this.destTime = destTime;
        this.delay = delay;
    }

    public String getStation() {
        return station;
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
                "station='" + station + '\'' +
                ", trainID='" + trainID + '\'' +
                ", description='" + description + '\'' +
                ", destTime='" + destTime + '\'' +
                ", delay='" + delay + '\'' +
                '}';
    }
}
