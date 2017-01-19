package de.throehl.mobilitaetsprofil.model.dbEntries;

/**
 * Created by thomas on 19.01.17.
 */

public class UserSaves {
    private final String userID;
    private final String routeID;

    public UserSaves(String userID, String routeID) {
        this.userID = userID;
        this.routeID = routeID;
    }

    public String getUserID() {
        return userID;
    }

    public String getRouteID() {
        return routeID;
    }
}
