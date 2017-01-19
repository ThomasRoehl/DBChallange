package de.throehl.mobilitaetsprofil.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.model.DatabaseSchema;
import de.throehl.mobilitaetsprofil.model.LiveInformationHelper;
import de.throehl.mobilitaetsprofil.model.LocalDatabaseHelper;
import de.throehl.mobilitaetsprofil.model.TravelPlanHelper;
import de.throehl.mobilitaetsprofil.model.UserAccountHelper;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Stop;
import de.throehl.mobilitaetsprofil.model.dbEntries.Transfers;
import de.throehl.mobilitaetsprofil.model.dbEntries.UserAccount;
import de.throehl.mobilitaetsprofil.model.dbEntries.UserSaves;

import static de.throehl.mobilitaetsprofil.model.DatabaseSchema.TravelPlan.COLUMN_NAME_ROUTEID;

/**
 * Created by thomas on 15.01.17.
 */

public class DatabaseController {

    private SQLiteDatabase db;
    private LocalDatabaseHelper dbHelper;

    private final String TAG = "DB-Controller";

    public DatabaseController(Context context){
        dbHelper = new LocalDatabaseHelper(context);
        Log.d("DB", "CREATED");
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ DatabaseSchema.TravelPlan.TABLE_NAME);
        db.execSQL("delete from "+ DatabaseSchema.LiveInfo.TABLE_NAME);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public void insertTravelPlan(ConnectionInformation con){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }

        for(Stop s: con.getStations()){
            ContentValues values = TravelPlanHelper.createTravelPlanEntry(con.getTRAIN(), s.getDEPARTURE(), s.getARRIVAL(), s.getDATE(), s.getNAME(), s.getTRAINID(), con.getTRAINTYPE(),con.getROUTEID());
            db.insert(DatabaseSchema.TravelPlan.TABLE_NAME, null, values);
        }
        this.close();
    }

    public void insertLiveInformation(LiveInformation liveInfo){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues values = LiveInformationHelper.createTravelPlanEntry(liveInfo.getStation(), liveInfo.getTrainID(), liveInfo.getDestTime(), liveInfo.getDelay(), liveInfo.getDescription());
        db.insert(DatabaseSchema.LiveInfo.TABLE_NAME, null, values);
        this.close();
    }

    public void insertLiveInformationMulti(ArrayList<LiveInformation> infos){
        for (LiveInformation l: infos){
            this.insertLiveInformation(l);
        }
    }

    public ConnectionInformation getTravelPlan(int routeID){
        ConnectionInformation con = new ConnectionInformation();
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return null;
        }
        String[] columns = new String[]{DatabaseSchema.TravelPlan.COLUMN_NAME_TRAIN,
                DatabaseSchema.TravelPlan.COLUMN_NAME_ARRIVTIME,
                DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINTYPE,
                DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINID,
                DatabaseSchema.TravelPlan.COLUMN_NAME_DATE,
                DatabaseSchema.TravelPlan.COLUMN_NAME_DEPARTIME,
                DatabaseSchema.TravelPlan.COLUMN_NAME_STATION
                };
        Cursor cursor = db.query(DatabaseSchema.TravelPlan.TABLE_NAME, columns, DatabaseSchema.TravelPlan.COLUMN_NAME_ROUTEID+"=?", new String[]{""+routeID}, null, null, null);

        cursor.moveToFirst();
        ArrayList<Stop> stops = new ArrayList<Stop>();
        int n = 0;
        while (!cursor.isAfterLast()) {
            if (n == 0) con.setSTART(cursor.getString(4));
            else{
                con.setDEST(cursor.getString(4));
            }
            stops.add(new Stop(cursor.getString(4),
                    cursor.getString(1),
                    cursor.getString(5),
                    cursor.getString(6),
                    Integer.parseInt(cursor.getString(3))));
            con.setTRAINTYPE(cursor.getString(3));
            con.setTRAIN(cursor.getString(0));

            n += 1;
            cursor.moveToNext();
        }
        con.setStations(stops);
        con.setROUTEID(routeID);
        // make sure to close the cursor
        cursor.close();

        this.close();
//        Log.d(TAG, stops.toString());
        return con;
    }

    public ArrayList<LiveInformation> getLiveInformation(String trainID){
        ArrayList<LiveInformation> liveInfo = new ArrayList<LiveInformation>();
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return null;
        }

        String[] columns = new String[]{
                DatabaseSchema.LiveInfo.COLUMN_NAME_STATION,
                DatabaseSchema.LiveInfo.COLUMN_NAME_DESTTIME,
                DatabaseSchema.LiveInfo.COLUMN_NAME_DELAY,
                DatabaseSchema.LiveInfo.COLUMN_NAME_DESCR
        };

        Cursor cursor = db.query(DatabaseSchema.LiveInfo.TABLE_NAME, columns, DatabaseSchema.LiveInfo.COLUMN_NAME_TRAINID+"=?", new String[]{""+trainID}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String station = cursor.getString(0);
            String destTime = cursor.getString(1);
            String delay = cursor.getString(2);
            String description = cursor.getString(3);
            liveInfo.add(new LiveInformation(station, trainID, description, destTime, delay));
            cursor.moveToNext();
        }
        cursor.close();

        return liveInfo;
    }

    // ----------------------------------------------------

    public void insertUserAccount(UserAccount user){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues con = UserAccountHelper.insertUserAccount(user.getName(), user.getPassword(), user.getUserID());
        db.insert(DatabaseSchema.UserAccount.TABLE_NAME, null, con);

        this.close();
    }

    public void insertUserSave(UserSaves save){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues con = UserAccountHelper.insertUserSave(save.getUserID(), save.getRouteID());
        db.insert(DatabaseSchema.UserSaves.TABLE_NAME, null, con);

        this.close();
    }

    public void insertTransfer(Transfers transfer){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues con = UserAccountHelper.insertTransfer(transfer.getSenderID(), transfer.getReceiverID(), transfer.getRouteID());
        db.insert(DatabaseSchema.Transfers.TABLE_NAME, null, con);

        this.close();
    }
}
