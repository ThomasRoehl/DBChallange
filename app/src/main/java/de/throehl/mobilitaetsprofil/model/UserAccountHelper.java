package de.throehl.mobilitaetsprofil.model;

import android.content.ContentValues;

/**
 * Created by thomas on 19.01.17.
 */

public abstract class UserAccountHelper {

    public static ContentValues insertUserAccount(String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSchema.UserAccount.COLUMN_NAME_USERNAME, username);
        contentValues.put(DatabaseSchema.UserAccount.COLUMN_NAME_PASSWORD, password);
        return contentValues;
    }

    public static ContentValues insertUserSave(String userID, String routeID){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSchema.UserSaves.COLUMN_NAME_USERID, userID);
        contentValues.put(DatabaseSchema.UserSaves.COLUMN_NAME_ROUTEID, routeID);
        return contentValues;
    }

    public static ContentValues insertTransfer(String senderID, String receiverID, String routeID){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSchema.Transfers.COLUMN_NAME_SENDER, senderID);
        contentValues.put(DatabaseSchema.Transfers.COLUMN_NAME_RECEIVER, receiverID);
        contentValues.put(DatabaseSchema.Transfers.COLUMN_NAME_ROUTEID, routeID);
        return contentValues;
    }
}
