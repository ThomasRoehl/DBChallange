package de.throehl.mobilitaetsprofil.controller;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by thomas on 17.01.17.
 */

public class LiveInformationController {

    public final String TAG = "LI-Controller";

    private final Handler handler;
    private final int delay = 10000;
    private boolean running = false;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "This is an autopost");
            handler.postDelayed(run, delay);
        }
    };
    private Context context;

    public LiveInformationController(Context con){
        this.context = con;
        this.handler = new Handler();
    }

    public void stopLiveCheck(){
        if (running)
            handler.removeCallbacks(run);
        running = false;
    }

    public void startLiveCheck(){
        if (!running)
            handler.post(run);
        running = true;
    }

}
