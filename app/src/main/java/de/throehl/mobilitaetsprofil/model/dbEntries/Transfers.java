package de.throehl.mobilitaetsprofil.model.dbEntries;

/**
 * Created by thomas on 19.01.17.
 */

public class Transfers {

    private final String senderID;
    private final String receiverID;
    private final String routeID;

    public Transfers(String senderID, String receiverID, String routeID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.routeID = routeID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getRouteID() {
        return routeID;
    }
}
