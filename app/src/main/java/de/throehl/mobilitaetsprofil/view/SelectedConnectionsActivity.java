package de.throehl.mobilitaetsprofil.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import de.throehl.mobilitaetsprofil.R;

public class SelectedConnectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_connections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button inCalenderButton = (Button) findViewById(R.id.id_button_in_calender_save);
        inCalenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(), "in Kalender gespeichert", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ConnectionSearchActivity.class);
                intent.putExtra("VIEW", 1);
                startActivity(intent);



            }
        });


    }

}
