package de.throehl.mobilitaetsprofil.model.dbEntries;

/**
 * Created by thomas on 15.01.17.
 */

public class Stop {
    private String NAME;
    private String DEPARTURE;
    private String ARRIVAL;
    private String DATE;
    private int TRAINID;

    public Stop(String NAME, String ARRIVAL, String DEPARTURE, String DATE, int TRAINID) {
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

    public int getTRAINID() {
        return TRAINID;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
