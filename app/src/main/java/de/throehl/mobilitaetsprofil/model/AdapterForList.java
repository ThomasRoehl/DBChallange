package de.throehl.mobilitaetsprofil.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.view.ConnectionSearchActivity;
import de.throehl.mobilitaetsprofil.view.SelectedConnectionsActivity;

/**
 * Created by tetiana on 04.02.17.
 */
public class AdapterForList extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> conections_list;
    private ArrayList<String> iteration_list;
    private ArrayList<String> delay_list;
    private AdapterForList a = this;

    public AdapterForList(Context context, int resource, ArrayList<String> liste1, ArrayList<String> liste2, ArrayList<String> liste3 ) {
        super(context, resource, liste1);
        this.context = context;
        this.conections_list = liste1;
        this.iteration_list=liste2;
        this.delay_list =liste3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("Adapter", "update\t"+position);

        final TextView row1;
        final TextView row2;
        final TextView row3;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row, null, false);
        }

        row1 = (TextView) convertView.findViewById(R.id.id_conections);
        row2 = (TextView) convertView.findViewById(R.id.id_iteration);
        row3 = (TextView) convertView.findViewById(R.id.id_dalay);

        row1.setText(conections_list.get(position));
        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectedConnectionsActivity.class);
                String[] s = row1.getText().toString().split("\n");
                int id  = ControllerFactory.getDbController().getRouteID(s[3], s[2]);
                Log.d("AdapterList", "ID\t"+id);
                ConnectionInformation con = ControllerFactory.getDbController().getTravelPlan(99);
                Log.d("AdapterList", con.toString());
                intent.putExtra("ACTIVITY", "SHOW");
                intent.putExtra("START",con.getSTART());
                intent.putExtra("DEST",con.getDEST());
                intent.putExtra("TRAIN",con.getTRAIN());
                intent.putExtra("TIME1",con.getStations().get(0).getDEPARTURE());
                intent.putExtra("TIME2",con.getStations().get(con.getStations().size()-1).getDEPARTURE());
                intent.putExtra("DATE",con.getStations().get(0).getDATE());
                intent.putExtra("STATIONS",con.getStations());
                intent.putExtra("ID", con.getROUTEID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        row2.setText(iteration_list.get(position));
        if (row2.getText().equals("an")) row2.setTextColor(Color.argb(255,14,100,27));
        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iteration_list.get(position).equals("aus")){
                    iteration_list.set(position, "an");
                    row2.setTextColor(Color.argb(255,14,100,27));
                }

                else {
                    iteration_list.set(position, "aus");
                    row2.setTextColor(Color.argb(255, 108,3,22));
                }
                a.notifyDataSetChanged();
            }
        });

        row3.setText(delay_list.get(position));
        if (row3.getText().equals("an")) row3.setTextColor(Color.argb(255,14,100,27));
        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delay_list.get(position).equals("aus")){
                    delay_list.set(position, "an");
                    row3.setTextColor(Color.argb(255,14,100,27));
                    String[] s = conections_list.get(position).split("\n");
                    int id  = ControllerFactory.getDbController().getRouteID(s[3].trim(), s[2].trim());
                    String date = s[0].trim();
                    Log.d("AdapterList", date);
                    String time = s[2];
                    String nDate = date.substring(8)+date.substring(3,5)+date.substring(0,2);
                    String nTime = time.replace("\\.", "").replace(":", "");
                    Log.d("AdapterList", nDate+"\t"+nTime);

//                    ControllerFactory.getTtController().startAutoCheck(s[3].trim(), s[0].trim(), s[2].trim(), ""+id);
                    ControllerFactory.getTtController().startAutoCheck(s[3].trim(), nDate, nTime, ""+id);
                }

                else {
                    delay_list.set(position, "aus");
                    row3.setTextColor(Color.argb(255, 108,3,22));
                    String[] s = conections_list.get(position).split("\n");
                    int id  = ControllerFactory.getDbController().getRouteID(s[3].trim(), s[2].trim());
                    ControllerFactory.getTtController().stopAutoCheck(s[3].trim(), ""+id);
                }
                a.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void updateLists(ArrayList<String> cons, ArrayList<String> iter, ArrayList<String> del){
        this.conections_list = cons;
        this.iteration_list = iter;
        this.delay_list = del;
        Log.d("Adapter", conections_list.toString());
        a.notifyDataSetChanged();
    }
}
