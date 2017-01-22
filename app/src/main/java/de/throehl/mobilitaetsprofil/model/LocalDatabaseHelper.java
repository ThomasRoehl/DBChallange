package de.throehl.mobilitaetsprofil.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thomas on 08.01.17.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DB-Helper";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MOBI_DB";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseSchema.CREATE_TRAVEL_PLAN);
        db.execSQL(DatabaseSchema.CREATE_LIVE_INFO);
        db.execSQL(DatabaseSchema.CREATE_LOCATIONS);
        db.execSQL(DatabaseSchema.CREATE_DEPARTURES);
        db.execSQL(DatabaseSchema.CREATE_TRANSFERS);
        db.execSQL(DatabaseSchema.CREATE_USER_ACCOUNT);
        db.execSQL(DatabaseSchema.CREATE_USER_SAVES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete from "+ DatabaseSchema.TravelPlan.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.LiveInfo.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.Locations.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.Transfers.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.Departures.TABLE_NAME);

        db.execSQL("delete from "+ DatabaseSchema.UserAccount.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.UserSaves.TABLE_NAME);
    }


}
