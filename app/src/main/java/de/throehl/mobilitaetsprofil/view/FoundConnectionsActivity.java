package de.throehl.mobilitaetsprofil.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;
import de.throehl.mobilitaetsprofil.model.dbEntries.Route;
import de.throehl.mobilitaetsprofil.model.dbEntries.Stop;

public class FoundConnectionsActivity extends AppCompatActivity {

    private final String className = this.getClass().getName();

    private String startloc, endloc, time, date;

    private TextView start, dest;
    private TextView time1, time1b, date1, dest1, path1;
    private TextView time2, time2b, date2, dest2, path2;
    private TextView time3, time3b, date3, dest3, path3;


    private GridLayout layer1,layer2, layer3;

    private ArrayList<Route> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_connections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.dblogo5);

        ViewControllerFactory.addActivity(className, this);


        Log.d("TAG", "onCreate FoundConnections");

        layer1 = (GridLayout) findViewById (R.id.id_lLayout_1);
        layer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), SelectedConnectionsActivity.class);
                intent.putExtra("ACTIVITY", className);
                intent.putExtra("START",start.getText());
                intent.putExtra("DEST",dest.getText());
                intent.putExtra("TRAIN",dest1.getText());
                intent.putExtra("TIME1",time1.getText());
                intent.putExtra("TIME2",time1b.getText());
                intent.putExtra("DATE",date1.getText());
                intent.putExtra("PATH",routes.get(0).getRouteAfter());
                intent.putExtra("ID", routes.get(0).getID());
                startActivity(intent);
            }
        });

        layer2 = (GridLayout) findViewById (R.id.id_gLayout_1);
        layer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), SelectedConnectionsActivity.class);
                intent.putExtra("ACTIVITY", className);
                intent.putExtra("START",start.getText());
                intent.putExtra("DEST",dest.getText());
                intent.putExtra("TRAIN",dest2.getText());
                intent.putExtra("TIME1",time2.getText());
                intent.putExtra("TIME2",time2b.getText());
                intent.putExtra("DATE",date2.getText());
                intent.putExtra("PATH",routes.get(1).getRouteAfter());
                intent.putExtra("ID", routes.get(1).getID());
                startActivity(intent);
            }
        });

        layer3 = (GridLayout) findViewById (R.id.id_gLayout_2);
        layer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), SelectedConnectionsActivity.class);
                intent.putExtra("ACTIVITY", className);
                intent.putExtra("START",start.getText());
                intent.putExtra("DEST",dest.getText());
                intent.putExtra("TRAIN",dest2.getText());
                intent.putExtra("TIME1",time2.getText());
                intent.putExtra("TIME2",time2b.getText());
                intent.putExtra("DATE",date2.getText());
                intent.putExtra("PATH",routes.get(2).getRouteAfter());
                intent.putExtra("ID", routes.get(2).getID());
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Option back

        start = (TextView) findViewById(R.id.id_start_position);
        dest = (TextView) findViewById(R.id.id_destination_position);

        time1 = (TextView) findViewById(R.id.time1);
        time1b = (TextView) findViewById(R.id.time1b);
        date1 = (TextView) findViewById(R.id.date1);
        dest1 = (TextView) findViewById(R.id.dest1);
        //path1 = (TextView) findViewById(R.id.path1);

        time2 = (TextView) findViewById(R.id.time2);
        time2b = (TextView) findViewById(R.id.time2b);
        date2 = (TextView) findViewById(R.id.date2);
        dest2 = (TextView) findViewById(R.id.dest2);
        //path2 = (TextView) findViewById(R.id.path2);

        time3 = (TextView) findViewById(R.id.time3);
        time3b = (TextView) findViewById(R.id.time3b);
        date3 = (TextView) findViewById(R.id.date3);
        dest3 = (TextView) findViewById(R.id.dest3);
        //path3 = (TextView) findViewById(R.id.path3);

        init();
        loadRoutes();

        new Thread(new Runnable() {
            public void run(){
                try {
                    synchronized (this) {
//                        wait(5000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FCA", "THREAD");
                                if (routes.size() < 2) {
                                    extractPath(0);
                                }
                                if (routes.size() < 3){
                                    extractPath(0);
                                    extractPath(1);
                                }
                                else {
                                    extractPath(0);
                                    extractPath(1);
                                    extractPath(2);
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void init(){
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            startloc = intent.getExtras().getString("START");
            endloc = intent.getExtras().getString("DEST");
            start.setText(startloc);
            dest.setText(endloc);
//            if (startloc.equals("Frankfurt (Main) Hbf (tief)")) start.setText("Frankfurt Hbf (tief)");
//            if (endloc.equals("Frankfurt (Main) Hbf (tief)")) dest.setText("Frankfurt Hbf (tief)");
            date = intent.getExtras().getString("DATE");
            time = intent.getExtras().getString("TIME");
            Log.d("TAG", "DATE\t"+transformDate(date));
            Log.d("TAG", "TIME\t"+transformTime(time));

        }
    }

    public void loadRoutes(){
        ArrayList<Route> table = ControllerFactory.getTtController().findRoute(start.getText().toString(), dest.getText().toString(), transformTime(time), transformDate(date), 3, "120");
        routes = new ArrayList<Route>();
        for (int i = 0; i < table.size(); i++){
            String[] td = retransformDateTime(table.get(i).getDp());
            switch (i){
                case 0:
                    time1.setText(td[1]);
                    time1b.setText(table.get(i).getDp());
                    date1.setText(td[0]);
                    dest1.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
                    routes.add(table.get(i));
//                    path1.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
                    continue;
                case 1:
                    time2.setText(td[1]);
                    time2b.setText(table.get(i).getDp());
                    date2.setText(td[0]);
                    dest2.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
//                    path2.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
                    routes.add(table.get(i));
                    continue;
                case 2:
                    time3.setText(td[1]);
                    time3b.setText(table.get(i).getDp());
                    date3.setText(td[0]);
                    dest3.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
//                    path3.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
                    routes.add(table.get(i));
                    continue;
            }
        }

        if (table.size() < 3) layer3.setVisibility(GridLayout.INVISIBLE);
        if (table.size() < 2) layer2.setVisibility(GridLayout.INVISIBLE);
        if (table.size() < 1) layer1.setVisibility(LinearLayout.INVISIBLE);
    }

    public String transformTime(String time){
        return time.replace(":", "");
    }

    public String transformDate(String date){
        String year = date.split("\\. ")[2];
        String day = date.split("\\. ")[0];
        String month = date.split("\\. ")[1];
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

    private void extractPath(int pos){
        Log.d("FCA", pos+"");
        String time;
        if (pos == 0) time = time1.getText().toString();
        else if (pos == 1) time = time2.getText().toString();
        else time = time3.getText().toString();
        try{
            String t = ControllerFactory.getTtController().getDestTime(dest.getText().toString(), routes.get(pos).getID(), transformDate(date), transformTime(time));
            t = retransformDateTime(t)[1];
            if (pos == 0) time1b.setText(t);
            if (pos == 1) time2b.setText(t);
            else time3b.setText(t);
            Log.d("FCA", t);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
