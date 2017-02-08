package de.throehl.mobilitaetsprofil.model.dbEntries;

import java.io.Serializable;

/**
 * Created by thomas on 15.01.17.
 */

public class Stop implements Serializable{
    private String NAME;
    private String DEPARTURE;
    private String ARRIVAL;
    private String DATE;
    private String TRAINID;

    public Stop(String NAME, String ARRIVAL, String DEPARTURE, String DATE, String TRAINID) {
        this.NAME = NAME;
        this.DEPARTURE = DEPARTURE;
        this.ARRIVAL = ARRIVAL;
        this.TRAINID = TRAINID;
        this.DATE = DATE;
    }

    public String getDATE() {
        return DATE;
    }

    public String getNAME() {
        return NAME;
    }

    public String getDEPARTURE() {
        return DEPARTURE;
    }

    public String getARRIVAL() {
        return ARRIVAL;
    }

    public String getTRAINID() {
        return TRAINID;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
