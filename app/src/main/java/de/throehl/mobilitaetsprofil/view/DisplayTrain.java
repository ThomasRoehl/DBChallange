package de.throehl.mobilitaetsprofil.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.model.dbEntries.Route;

public class DisplayTrain extends AppCompatActivity {

    String stationS;
    String timeS;
    String dateS;
    String nachS;
    ArrayList<String> routes = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_train);

        String[] msg = getIntent().getStringArrayExtra("MSG");

        stationS = msg[0].trim();
        timeS = msg[1].trim();
        dateS = msg[2].trim();
        nachS = "Wiesbaden Hbf";

        if (stationS.isEmpty()){
            stationS = "Mühlheim (Main)";
            timeS = "2200";
            dateS = "170127";
        }

        TextView von = (TextView) findViewById(R.id.von);
        TextView nach = (TextView) findViewById(R.id.nach);
        TextView zug = (TextView) findViewById(R.id.zug);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time = (TextView) findViewById(R.id.time);

        von.setText(stationS);
        nach.setText(timeS);
//        zug.setText("S9");
        date.setText(dateS);
        time.setText(timeS);
        nach.setText(nachS);


        Button show = (Button) findViewById(R.id.show);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Route> table = ControllerFactory.getTtController().findRoute(stationS, nachS, timeS, dateS, 10, "40");
                for(Route r: table){
                    routes.add(r.toString());
                }
                adapter.notifyDataSetChanged();
            }
        });

        ListView listView = (ListView) findViewById(R.id.displayList);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, routes);
        listView.setAdapter(adapter);



    }
}
