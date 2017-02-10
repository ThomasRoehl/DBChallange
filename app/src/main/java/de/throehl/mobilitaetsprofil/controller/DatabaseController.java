package de.throehl.mobilitaetsprofil.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
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
    }

    public void emptyTables(){
        try{
            this.open();
            db.execSQL("delete from "+ DatabaseSchema.TravelPlan.TABLE_NAME);
            db.execSQL("delete from "+ DatabaseSchema.LiveInfo.TABLE_NAME);
            db.execSQL("delete from "+ DatabaseSchema.Locations.TABLE_NAME);
            db.execSQL("delete from "+ DatabaseSchema.Transfers.TABLE_NAME);
            db.execSQL("delete from "+ DatabaseSchema.Departures.TABLE_NAME);
//            db.execSQL("delete from "+ DatabaseSchema.UserAccount.TABLE_NAME);
            db.execSQL("delete from "+ DatabaseSchema.UserSaves.TABLE_NAME);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        this.close();

    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    // ----------------------------------------------------
    // TRAVEL PLAN
    // ----------------------------------------------------

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
            ContentValues values = TravelPlanHelper.createTravelPlanEntry(con.getTRAIN(), s.getDEPARTURE(), s.getARRIVAL(), s.getNAME(), s.getDATE(), s.getTRAINID(), con.getTRAINTYPE(),con.getROUTEID());
            db.insert(DatabaseSchema.TravelPlan.TABLE_NAME, null, values);
        }
        this.close();
    }

    public int getRouteID(String start, String time){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return -1;
        }

        Cursor cursor = db.query(DatabaseSchema.TravelPlan.TABLE_NAME, new String[]{DatabaseSchema.TravelPlan.COLUMN_NAME_ROUTEID}, DatabaseSchema.TravelPlan.COLUMN_NAME_STATION+"=? AND "+DatabaseSchema.TravelPlan.COLUMN_NAME_DEPARTIME+"=?", new String[]{start, time}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            String s = cursor.getString(0);
            try{
                return Integer.parseInt(s);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        close();
        return -1;
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
        Cursor cursor = db.query(DatabaseSchema.TravelPlan.TABLE_NAME, columns, DatabaseSchema.TravelPlan.COLUMN_NAME_ROUTEID +"=?", new String[]{""+routeID}, null, null, null);

        cursor.moveToFirst();
        ArrayList<Stop> stops = new ArrayList<Stop>();
        int n = 0;
        while (!cursor.isAfterLast()) {
            Log.d(TAG, cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_STATION))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_ARRIVTIME))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_DEPARTIME))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_DATE))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAIN))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINTYPE))
                    + "\t" +cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINID)));
            if (n == 0) con.setSTART(cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_STATION)));
            else{
                con.setDEST(cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_STATION)));
            }
            stops.add(new Stop(cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_STATION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_ARRIVTIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_DEPARTIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_DATE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINID))));
            con.setTRAINTYPE(cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAINTYPE)));
            con.setTRAIN(cursor.getString(cursor.getColumnIndex(DatabaseSchema.TravelPlan.COLUMN_NAME_TRAIN)));

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

    // ----------------------------------------------------
    // LIVE INFORMATION
    // ----------------------------------------------------

    public void insertLiveInformation(LiveInformation liveInfo){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues values = LiveInformationHelper.createTravelPlanEntry(liveInfo.getStationID(), liveInfo.getTrainID(), liveInfo.getDestTime(), liveInfo.getDelay(), liveInfo.getDescription());
        db.insert(DatabaseSchema.LiveInfo.TABLE_NAME, null, values);
        this.close();
    }

    public void insertLiveInformationMulti(ArrayList<LiveInformation> infos){
        for (LiveInformation l: infos){
            this.insertLiveInformation(l);
        }
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

        close();

        return liveInfo;
    }

    // ----------------------------------------------------
    // USER ACCOUNTS
    // ----------------------------------------------------

    public void insertUserAccount(UserAccount user){
        try {
            this.open();
            Log.d("DB"," OPEND FOR INSERT USERACCOUNT");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }
        ContentValues con = UserAccountHelper.insertUserAccount(user.getName(), user.getPassword());
        db.insert(DatabaseSchema.UserAccount.TABLE_NAME, null, con);

        this.close();
    }

    public int getUserID(UserAccount user){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return -1;
        }

        Cursor cursor = db.query(DatabaseSchema.UserAccount.TABLE_NAME, new String[]{DatabaseSchema.UserAccount._ID}, DatabaseSchema.UserAccount.COLUMN_NAME_USERNAME+"=? AND "+DatabaseSchema.UserAccount.COLUMN_NAME_PASSWORD+"=?", new String[]{user.getName(), user.getPassword()}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            String s = cursor.getString(0);
            return Integer.parseInt(s);
        }
        this.close();
        return -1;
    }

    public int getUserIDByName(String name){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return -1;
        }

        Cursor cursor = db.query(DatabaseSchema.UserAccount.TABLE_NAME, new String[]{DatabaseSchema.UserAccount._ID}, DatabaseSchema.UserAccount.COLUMN_NAME_USERNAME+"=?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            String s = cursor.getString(0);
            return Integer.parseInt(s);
        }
        this.close();
        return -1;
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
        Log.d(TAG, "insert Transfer"+transfer.getReceiverID());
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

    public ArrayList<String> getTransfer(String userID){
        ArrayList<String> res = new ArrayList<String>();
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return res;
        }
        Cursor cursor = db.query(DatabaseSchema.Transfers.TABLE_NAME, new String[]{DatabaseSchema.Transfers.COLUMN_NAME_ROUTEID}, DatabaseSchema.Transfers.COLUMN_NAME_RECEIVER+"=?", new String[]{""+userID}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            res.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return res;
    }

    public void removeTransfer(String userID){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return;
        }

        db.delete(DatabaseSchema.Transfers.TABLE_NAME, DatabaseSchema.Transfers.COLUMN_NAME_RECEIVER+"=?", new String[]{userID});
    }

    public long countUserSaves(){
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return 0;
        }
        long num = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + DatabaseSchema.UserSaves.TABLE_NAME, new String[]{});
        return num;
    }

    public ArrayList<String> getUserSaves(String userID){
        Log.d(TAG, "USERID:\t"+userID);
        ArrayList<String> res = new ArrayList<String>();
        try {
            this.open();
            Log.d("DB"," OPEND");
        }
        catch (Exception e){
            Log.d("DB",e.getMessage());
            return res;
        }
        Cursor cursor = db.query(DatabaseSchema.UserSaves.TABLE_NAME, new String[]{DatabaseSchema.UserSaves.COLUMN_NAME_ROUTEID}, DatabaseSchema.UserSaves.COLUMN_NAME_USERID+"=?", new String[]{""+userID}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            res.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        Log.d(TAG, res.toString());
        return res;
    }
}
