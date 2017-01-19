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
    private static final String DATABASE_NAME = "test";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseSchema.CREATE_TRAVEL_PLAN);
        db.execSQL(DatabaseSchema.CREATE_LIVE_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
