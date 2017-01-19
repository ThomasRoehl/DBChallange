package de.throehl.mobilitaetsprofil.model;

import android.content.ContentValues;

/**
 * Created by thomas on 17.01.17.
 */

public abstract class LiveInformationHelper {
    public static ContentValues createTravelPlanEntry(String station, String trainID, String destTime, String delay, String description){
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.LiveInfo.COLUMN_NAME_STATION, station);
        values.put(DatabaseSchema.LiveInfo.COLUMN_NAME_TRAINID, trainID);
        values.put(DatabaseSchema.LiveInfo.COLUMN_NAME_DESTTIME, destTime);
        values.put(DatabaseSchema.LiveInfo.COLUMN_NAME_DELAY, delay);
        values.put(DatabaseSchema.LiveInfo.COLUMN_NAME_DESCR, description);
        return values;
    }
}
