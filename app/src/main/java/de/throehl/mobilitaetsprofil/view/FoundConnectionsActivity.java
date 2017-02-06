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

public class FoundConnectionsActivity extends AppCompatActivity {

    private final String className = this.getClass().getName();

    private String startloc, endloc, time, date;

    private TextView start, dest;
    private TextView time1, time1b, date1, dest1, path1;
    private TextView time2, time2b, date2, dest2, path2;
    private TextView time3, time3b, date3, dest3, path3;

    private LinearLayout layer1;
    private GridLayout layer2, layer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_connections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewControllerFactory.addActivity(className, this);


        layer1 = (LinearLayout) findViewById (R.id.id_lLayout_1);

        layer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(), SelectedConnectionsActivity.class);
                startActivity(intent);
            }
        });

        layer2 = (GridLayout) findViewById (R.id.id_gLayout_1);
        layer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectedConnectionsActivity.class);
                startActivity(intent);
            }
        });

        layer3 = (GridLayout) findViewById (R.id.id_gLayout_2);
        layer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectedConnectionsActivity.class);
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
        path1 = (TextView) findViewById(R.id.path1);

        time2 = (TextView) findViewById(R.id.time2);
        time2b = (TextView) findViewById(R.id.time2b);
        date2 = (TextView) findViewById(R.id.date2);
        dest2 = (TextView) findViewById(R.id.dest2);
        path2 = (TextView) findViewById(R.id.path2);

        time3 = (TextView) findViewById(R.id.time3);
        time3b = (TextView) findViewById(R.id.time3b);
        date3 = (TextView) findViewById(R.id.date3);
        dest3 = (TextView) findViewById(R.id.dest3);
        path3 = (TextView) findViewById(R.id.path3);

        init();
        loadRoutes();
    }

    public void init(){
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            startloc = intent.getExtras().getString("START");
            endloc = intent.getExtras().getString("DEST");
            start.setText(startloc);
            dest.setText(endloc);
            if (startloc.equals("Frankfurt (Main) Hbf (tief)")) start.setText("Frankfurt Hbf (tief)");
            if (endloc.equals("Frankfurt (Main) Hbf (tief)")) dest.setText("Frankfurt Hbf (tief)");
            date = intent.getExtras().getString("DATE");
            time = intent.getExtras().getString("TIME");
            Log.d("TAG", transformDate(date));
            Log.d("TAG", transformTime(time));

        }
    }

    public void loadRoutes(){
        ArrayList<Route> table = ControllerFactory.getTtController().findRoute(start.getText().toString(), dest.getText().toString(), transformTime(time), transformDate(date), 3, "60");
        for (int i = 0; i < table.size(); i++){
            String[] td = retransformDateTime(table.get(i).getDp());
            Log.d("TAG", "i:\t"+i);
            switch (i){
                case 0:
                    time1.setText(td[1]);
                    time1b.setText(table.get(i).getDp());
                    date1.setText(td[0]);
                    dest1.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
                    path1.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
                    continue;
                case 1:
                    time2.setText(td[1]);
                    time2b.setText(table.get(i).getDp());
                    date2.setText(td[0]);
                    dest2.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
                    path2.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
                    continue;
                case 2:
                    time3.setText(td[1]);
                    time3b.setText(table.get(i).getDp());
                    date3.setText(td[0]);
                    dest3.setText(table.get(i).getTrainName() + " nach " +table.get(i).getRouteAfter().get(table.get(i).getRouteAfter().size()-1));
                    path3.setText(table.get(i).getRouteAfter().subList(0,2).toString().replace("]", "")+" ...");
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
        String year = date.split(". ")[2];
        String day = date.split(". ")[0];
        String month = date.split(". ")[1];
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
