package de.throehl.mobilitaetsprofil.model.dbEntries;

import android.util.Log;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * Created by thomas on 19.01.17.
 */

public class UserAccount {
    private String password;
    private String name;
    private String userID;
    public final String TAG = "UserAccount";

    public UserAccount(String name, String pw){
        this.password = pw;
        this.name = name;
    }

    public void setUserID(String id){ userID = id;}

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getUserID(){
        return userID;
    }
}
