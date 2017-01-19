package de.throehl.mobilitaetsprofil.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.DatabaseController;
import de.throehl.mobilitaetsprofil.controller.JsonParser;
import de.throehl.mobilitaetsprofil.controller.LiveInformationController;
import de.throehl.mobilitaetsprofil.dummy.DummyLiveInformation;
import de.throehl.mobilitaetsprofil.dummy.DummyTravelPlan;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main-Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DummyTravelPlan dummy = new DummyTravelPlan();
        DummyLiveInformation dummyL = new DummyLiveInformation(16, 50, 2, 20);

        ControllerFactory.initController(this.getApplicationContext());

        DatabaseController databaseController = ControllerFactory.getDbController();

        databaseController.insertTravelPlan(dummy.getCon(1));
        ConnectionInformation con1 = databaseController.getTravelPlan(1);
        databaseController.insertTravelPlan(dummy.getCon(2));
        ConnectionInformation con2 = databaseController.getTravelPlan(2);
        Log.d(TAG, con1.toString());
        Log.d(TAG, con2.toString());


        databaseController.insertLiveInformationMulti(dummyL.getLiveInfos());
        ArrayList<LiveInformation> liveInfo1 = databaseController.getLiveInformation("0");
        for (LiveInformation l: liveInfo1){
            Log.d(TAG, l.toString());
        }

        JsonParser parser = ControllerFactory.getJsonParser();
        parser.loadFile("fahrplanAPI/locations.json");
        parser.printMap(parser.loadLocation());

        final LiveInformationController informationController = ControllerFactory.getLiController();

        Button start = (Button) findViewById(R.id.btn_start);
        Button stop = (Button) findViewById(R.id.btn_stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informationController.startLiveCheck();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informationController.stopLiveCheck();
            }
        });


    }
}
