package de.throehl.mobilitaetsprofil.model;

import android.provider.BaseColumns;

import java.sql.Timestamp;

/**
 * Created by thomas on 08.01.17.
 */

public final class DatabaseSchema {

    private DatabaseSchema(){};

    public static final String CREATE_TRAVEL_PLAN =
        "CREATE TABLE " + TravelPlan.TABLE_NAME + " (" +
                TravelPlan._ID + " INTEGER PRIMARY KEY," +
                TravelPlan.COLUMN_NAME_TRAIN + " TEXT," +
                TravelPlan.COLUMN_NAME_DEPARTIME + " TEXT," +
                TravelPlan.COLUMN_NAME_ARRIVTIME + " TEXT," +
                TravelPlan.COLUMN_NAME_DATE + " TEXT," +
                TravelPlan.COLUMN_NAME_STATION + " TEXT," +
                TravelPlan.COLUMN_NAME_TRAINID + " TEXT," +
                TravelPlan.COLUMN_NAME_TRAINTYPE + " TEXT," +
                TravelPlan.COLUMN_NAME_ROUTEID + " TEXT" +
                ")";

    public static final String CREATE_LIVE_INFO =
            "CREATE TABLE " + LiveInfo.TABLE_NAME + " (" +
                    LiveInfo._ID + " INTEGER PRIMARY KEY," +
                    LiveInfo.COLUMN_NAME_TRAINID + " TEXT," +
                    LiveInfo.COLUMN_NAME_STATION + " TEXT," +
                    LiveInfo.COLUMN_NAME_DESTTIME + " TEXT," +
                    LiveInfo.COLUMN_NAME_DELAY + " TEXT," +
                    LiveInfo.COLUMN_NAME_DESCR + " TEXT" +
                    ")";
    public static final String CREATE_LOCATIONS =
            "CREATE TABLE " + Locations.TABLE_NAME + " (" +
                    Locations._ID+ " INTEGER PRIMARY KEY," +
                    Locations.TABLE_NAME + " TEXT," +
                    Locations.COLUMN_NAME_NAME + " TEXT," +
                    Locations.COLUMN_NAME_EVANR + " TEXT" +
                    ")";

    public static final String CREATE_DEPARTURES =
            "CREATE TABLE " + Departures.TABLE_NAME + " (" +
                    Departures._ID+ " INTEGER PRIMARY KEY," +
                    Departures.TABLE_NAME + " TEXT," +
                    Departures.COLUMN_NAME_DATE + " TEXT," +
                    Departures.COLUMN_NAME_DIRECTION + " TEXT," +
                    Departures.COLUMN_NAME_EVANR + " TEXT," +
                    Departures.COLUMN_NAME_PLATFORM + " TEXT," +
                    Departures.COLUMN_NAME_TIME + " TEXT," +
                    Departures.COLUMN_NAME_TRAINID + " TEXT," +
                    Departures.COLUMN_NAME_TRAINTYPE + " TEXT" +
                    ")";

    public static final String CREATE_USER_ACCOUNT =
            "CREATE TABLE " + UserAccount.TABLE_NAME + " (" +
                    UserAccount._ID+ " INTEGER PRIMARY KEY," +
                    UserAccount.TABLE_NAME + " TEXT," +
                    UserAccount.COLUMN_NAME_USERNAME + " TEXT," +
                    UserAccount.COLUMN_NAME_PASSWORD + " TEXT" +
                    ")";
    public static final String CREATE_USER_SAVES =
            "CREATE TABLE " + UserSaves.TABLE_NAME + " (" +
                    UserSaves._ID+ " INTEGER PRIMARY KEY," +
                    UserSaves.TABLE_NAME + " TEXT," +
                    UserSaves.COLUMN_NAME_USERID + " TEXT," +
                    UserSaves.COLUMN_NAME_ROUTEID + " TEXT" +
                    ")";
    public static final String CREATE_TRANSFERS =
            "CREATE TABLE " + Transfers.TABLE_NAME + " (" +
                    Transfers._ID+ " INTEGER PRIMARY KEY," +
                    Transfers.TABLE_NAME + " TEXT," +
                    Transfers.COLUMN_NAME_SENDER + " TEXT," +
                    Transfers.COLUMN_NAME_RECEIVER + " TEXT," +
                    Transfers.COLUMN_NAME_ROUTEID + " TEXT" +
                    ")";


    public static class Locations implements  BaseColumns{
        public static final String TABLE_NAME = "Locations";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_EVANR = "EvaNr";
    }

    public static class Departures implements BaseColumns{
        public static final String TABLE_NAME = "Departures";
        public static final String COLUMN_NAME_EVANR = "EvaNr";
        public static final String COLUMN_NAME_TRAINID = "Train_ID";
        public static final String COLUMN_NAME_TRAINTYPE = "Train_Type";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_DIRECTION = "Direction";
        public static final String COLUMN_NAME_PLATFORM = "Platform";
    }

    public static class TravelPlan implements BaseColumns{
        public static final String TABLE_NAME = "Travel_Plan";
        public static final String COLUMN_NAME_TRAIN = "Train";
        public static final String COLUMN_NAME_DEPARTIME = "Departure_Time";
        public static final String COLUMN_NAME_ARRIVTIME = "Arrival_Time";
        public static final String COLUMN_NAME_DATE = "Date";
        public static final String COLUMN_NAME_STATION = "Station";
        public static final String COLUMN_NAME_TRAINID = "Train_ID";
        public static final String COLUMN_NAME_TRAINTYPE = "Train_Type";
        public static final String COLUMN_NAME_ROUTEID = "Route_ID";
    }

    public static class LiveInfo implements BaseColumns{
        public static final String TABLE_NAME = "Live_Information";
        public static final String COLUMN_NAME_TRAINID = "Train_ID";
        public static final String COLUMN_NAME_STATION = "Stop";
        public static final String COLUMN_NAME_DESTTIME = "Destination_Time";
        public static final String COLUMN_NAME_DELAY = "Delay";
        public static final String COLUMN_NAME_DESCR = "Description";
    }

    public static class UserAccount implements BaseColumns{
        public static final String TABLE_NAME = "UserAccount";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_USERNAME = "Name";
    }

    public static class UserSaves implements BaseColumns{
        public static final String TABLE_NAME = "User_Saves";
        public static final String COLUMN_NAME_USERID = "User_ID";
        public static final String COLUMN_NAME_ROUTEID = "Route_ID";
    }

    public static class Transfers implements BaseColumns{
        public static final String TABLE_NAME = "Transfers";
        public static final String COLUMN_NAME_SENDER = "Sender_ID";
        public static final String COLUMN_NAME_RECEIVER = "Receiver_ID";
        public static final String COLUMN_NAME_ROUTEID = "Route_ID";
    }
}
