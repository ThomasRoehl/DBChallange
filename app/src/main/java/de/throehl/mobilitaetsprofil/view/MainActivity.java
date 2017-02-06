package de.throehl.mobilitaetsprofil.view;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.FileLoader;
import de.throehl.mobilitaetsprofil.controller.Initiator;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main-Activity";
    String stationS;
    String timeS;
    String dateS;
    ArrayList<String> stationNames = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button) findViewById(R.id.search);
        Button display = (Button) findViewById(R.id.display);
        final TextView vonnach = (TextView) findViewById(R.id.vonnach);
        final TextView time = (TextView) findViewById(R.id.time);
        final TextView date = (TextView) findViewById(R.id.date);
        final ListView listview = (ListView) findViewById(R.id.listview);

        final Initiator initiator = new Initiator(this.getApplicationContext());



        listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stationNames);
        listview.setAdapter(listAdapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stationS = vonnach.getText().toString();
//                timeS = time.getText().toString();
//                dateS = date.getText().toString();
////                stationNames.clear();
////                stationNames.addAll(FileLoader.loadStationNames(stationS));
////                listAdapter.notifyDataSetChanged();
//                initiator.newHandler(stationS, dateS, timeS, "1");
                FileLoader.downloadFile(MainActivity.this);
            }
        });

        final Intent intent = new Intent(this, DisplayTrain.class);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stationS = vonnach.getText().toString();
//                timeS = time.getText().toString();
//                dateS = date.getText().toString();
//                String[] msg = new String[]{stationS, timeS, dateS};
//                intent.putExtra("MSG", msg);
//                startActivity(intent);
                initiator.stopHandler(vonnach.getText().toString(), time.getText().toString());
            }
        });


//        initiator.emptyDB();
//        initiator.initTravelPlans();
//        initiator.initLiveInfo();
//        initiator.initJsonParser();
//        initiator.initXmlParser();
//        initiator.initXmlParserTT();

//        TextView tv = (TextView) findViewById(R.id.textView2);
//        tv.setText(initiator.test());
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }
}
