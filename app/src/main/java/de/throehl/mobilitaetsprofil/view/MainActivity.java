package de.throehl.mobilitaetsprofil.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.DatabaseController;
import de.throehl.mobilitaetsprofil.controller.Initiator;
import de.throehl.mobilitaetsprofil.controller.JsonParser;
import de.throehl.mobilitaetsprofil.controller.LiveInformationController;
import de.throehl.mobilitaetsprofil.controller.LiveTimeTableController;
import de.throehl.mobilitaetsprofil.controller.XMLParser;
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

        Initiator initiator = new Initiator(this.getApplicationContext());
        initiator.emptyDB();
        initiator.initTravelPlans();
        initiator.initLiveInfo();
        initiator.initJsonParser();
        initiator.initXmlParser();
    }
}
