package de.throehl.mobilitaetsprofil.model;


import android.content.ContentValues;
import android.provider.ContactsContract;

import java.sql.Timestamp;

/**
 * Created by thomas on 14.01.17.
 */

public abstract class TravelPlanHelper {

    public static ContentValues createTravelPlanEntry(String train, String depa, String arri, String station, String date, int id, String type, int routeID){
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAIN, train);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_DEPARTIME, depa);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_ARRIVTIME, arri);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_DATE, date);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_STATION, station);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINID, id);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINTYPE, type);
        values.put(DatabaseSchema.TravelPlan.COLUMN_NAME_ROUTEID, routeID);
        return values;
    }
}