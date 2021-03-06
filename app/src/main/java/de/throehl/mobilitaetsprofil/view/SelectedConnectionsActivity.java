package de.throehl.mobilitaetsprofil.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ResourceBundle;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;
import de.throehl.mobilitaetsprofil.model.CalendarCollection;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.LiveInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Route;
import de.throehl.mobilitaetsprofil.model.dbEntries.Stop;
import de.throehl.mobilitaetsprofil.model.dbEntries.Transfers;
import de.throehl.mobilitaetsprofil.model.dbEntries.UserSaves;

public class SelectedConnectionsActivity extends AppCompatActivity {

    private final String className = this.getClass().getName();
    private String start, dest, time1, time2, date, train;
    private ArrayList<String> path;
    private ArrayList<Stop> stops;
    private TextView startE, destE, time1E, time2E, trainE, dateE, pathE;
    private String route;
    private String trainID;
    private EditText sendTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_connections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(R.drawable.dblogo5);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stops = new ArrayList<Stop>();
        startE = (TextView) findViewById(R.id.start);
        destE = (TextView) findViewById(R.id.dest);
        time1E = (TextView) findViewById(R.id.time1);
        time2E = (TextView) findViewById(R.id.time2);
        trainE = (TextView) findViewById(R.id.train);
        dateE = (TextView) findViewById(R.id.date);
        pathE = (TextView) findViewById(R.id.path);

        sendTo = (EditText) findViewById(R.id.sendEdit);
        Button send = (Button) findViewById(R.id.id_button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recieverID = sendTo.getText().toString();
                if (recieverID.isEmpty()) return;
                if (ControllerFactory.getDbController().getRouteID(start, time1) == -1) {
                    long routeID = ControllerFactory.getDbController().countUserSaves();
                    ConnectionInformation conInfo = new ConnectionInformation();
                    conInfo.setSTART(start);
                    conInfo.setDEST(dest);
                    conInfo.setTRAINTYPE(train);
                    conInfo.setTRAIN(trainID);
                    conInfo.setStations(stops);
                    conInfo.setROUTEID((int) routeID + 1);
                    ControllerFactory.getDbController().insertTravelPlan(conInfo);


                    routeID = ControllerFactory.getDbController().getRouteID(start, time1);
                    recieverID = ControllerFactory.getDbController().getUserIDByName(recieverID)+"";
                    if (recieverID.equals("-1")) return;
                    ControllerFactory.getDbController().insertTransfer(new Transfers(ControllerFactory.getID(), recieverID, routeID+""));
                }
                else{
                    long routeID = ControllerFactory.getDbController().getRouteID(start, time1);
                    recieverID = ControllerFactory.getDbController().getUserIDByName(recieverID)+"";
                    if (recieverID.equals("-1")) return;
                    ControllerFactory.getDbController().insertTransfer(new Transfers(ControllerFactory.getID(), recieverID, routeID+""));
                }
            }
        });

        ViewControllerFactory.addActivity(className, this);

        init();

        Button inCalenderButton = (Button) findViewById(R.id.id_button_in_calender_save);
        inCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewControllerFactory.closeActivity(getIntent().getExtras().getString("ACTIVITY"));
                Intent intent = new Intent(ControllerFactory.getAppContext(), ConnectionSearchActivity.class);
                intent.putExtra("VIEW", 2);

                if (ControllerFactory.getDbController().getRouteID(start, time1) == -1) {
                    long routeID = ControllerFactory.getDbController().countUserSaves();
                    ConnectionInformation conInfo = new ConnectionInformation();
                    conInfo.setSTART(start);
                    conInfo.setDEST(dest);
                    conInfo.setTRAINTYPE(train);
                    conInfo.setTRAIN(trainID);
                    conInfo.setStations(stops);
                    conInfo.setROUTEID((int) routeID + 1);
                    ControllerFactory.getDbController().insertTravelPlan(conInfo);
                    ControllerFactory.getDbController().insertUserSave(new UserSaves(ControllerFactory.getID(), ""+(routeID+1)));
                    int id = ControllerFactory.getDbController().getRouteID(start, time1);
                    intent.putExtra("ADDED", id);
                    addDelay((routeID + 1)+"", time1);

                    // Add Calendar entry
                    String msg = trainID + "\n" + time1 + "\t" + start + "\n" + time2 + "\t" + dest;
                    CalendarCollection.date_collection_arr.add(new CalendarCollection(date, msg));
                }
                intent.putExtra("ACTIVITY", className);
                startActivity(intent);
            }
        });
        Intent i = getIntent();
        if (i.getExtras() != null){
            if (i.getExtras().containsKey("ACTIVITY")){
                if (!i.getExtras().getString("ACTIVITY").equals("SHOW")){
                    new Thread(new Runnable() {
                        public void run(){
                            try {
                                synchronized (this) {
//                                    wait(5000);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            extractPath();
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else{
                    getPaths();
                }
            }
        }

    }

    private void addDelay(String route, String dest){
        ControllerFactory.getDbController().insertLiveInformation(new LiveInformation(route, route, "5 Minute Verspätung", dest, "5"));
    }

    private void getPaths(){
        time2E.setText(stops.get(stops.size()-1).getDEPARTURE());
        stops.remove(0);
        route = "";
        for (Stop s: stops){
            route += s.getDEPARTURE() +  "     ->     " + s.getNAME() +"\n";
        }
        pathE.setText(route);
    }

    private void extractPath(){
        route = "";
        boolean found = false;
        stops.add(new Stop(start, time1, time1, date, trainID));
        for (String s: path){
            if (s.equals(dest)) found=true;

            String t = ControllerFactory.getTtController().getDestTime(s, trainID, transformDate(date), transformTime(time1));
            Log.d("TEST", "Time: "+t);
            time2 =  retransformDateTime(t)[1];
            route += time2 +  "     ->     " + s +"\n";
            stops.add(new Stop(s, time2, time2, date, trainID));
            if (found) break;
        }
        pathE.setText(route);
        time2E.setText(time2);
    }

    private void init(){
        Intent i = getIntent();
        if (i.getExtras() != null){
            if (i.getExtras().containsKey("START")){
                start = i.getExtras().getString("START");
                startE.setText(start);
            }
            if (i.getExtras().containsKey("DEST")){
                dest = i.getExtras().getString("DEST");
                destE.setText(dest);
            }
            if (i.getExtras().containsKey("TIME1")){
                time1 = i.getExtras().getString("TIME1");
                time1E.setText(time1);
                Log.d("TEST", time1);
            }
            if (i.getExtras().containsKey("TIME2")){
                time2 = i.getExtras().getString("TIME2");
                time2E.setText("");
            }
            if (i.getExtras().containsKey("TRAIN")){
                train = i.getExtras().getString("TRAIN");
                trainE.setText(train);
            }
            if (i.getExtras().containsKey("DATE")){
                date = i.getExtras().getString("DATE");
                dateE.setText(date);
                Log.d("TEST", date);
            }
            if (i.getExtras().containsKey("PATH")){
                path = (ArrayList<String>) i.getExtras().get("PATH");
                pathE.setText("");
            }
            if (i.getExtras().containsKey("ID")){
                trainID =  i.getExtras().getString("ID");
            }
            if (i.getExtras().containsKey("STATIONS")){
                stops = (ArrayList<Stop>) i.getExtras().get("STATIONS");
            }
        }
    }

    @Override
    public void onBackPressed(){
        if (getIntent().getExtras().containsKey("ACTIVITY")){
            if (getIntent().getExtras().getString("ACTIVITY").equals("SHOW")){
                Intent intent = new Intent(ControllerFactory.getAppContext(), ConnectionSearchActivity.class);
                intent.putExtra("VIEW", 2);
                intent.putExtra("ACTIVITY", className);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), FoundConnectionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                ViewControllerFactory.closeActivity(className);
            }
        }
        else{
            Intent intent = new Intent(getApplicationContext(), FoundConnectionsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            ViewControllerFactory.closeActivity(className);
        }
    }


    public String transformTime(String time){
        return time.replace(":", "");
    }

    public String transformDate(String date){
        String year = date.split("\\.")[2];
        String day = date.split("\\.")[0];
        String month = date.split("\\.")[1];
        return year.substring(2) + month + day;
    }

    public String[] retransformDateTime(String tmp){
        String dateY = "20"+tmp.substring(0,2);
        String dateM = tmp.substring(2,4);
        String dateD = tmp.substring(4,6);
        String timeH = tmp.substring(6,8);
        String timeM = tmp.substring(8);
        return new String[]{dateD+"."+dateM+"."+dateY, timeH+":"+timeM};
    }
}
