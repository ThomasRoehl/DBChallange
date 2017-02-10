package de.throehl.mobilitaetsprofil.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.FileLoader;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;

public class StartActivity extends AppCompatActivity {

    private ArrayList<String> stationNames = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private ListView listView;
    private EditText text;
    private final String className = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.dblogo5);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewControllerFactory.addActivity(className, this);

        text = (EditText) findViewById(R.id.id_start);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3){
                    stationNames.addAll(FileLoader.loadStationNames(s.toString()));
                    listAdapter.notifyDataSetChanged();
                    listAdapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView = (ListView) findViewById(R.id.possibleNames);
        listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stationNames);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), ConnectionSearchActivity.class);
                intent.putExtra("VIEW", 0);
                intent.putExtra("START", ((TextView)view).getText().toString());
                intent.putExtra("ACTIVITY", className);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

}
