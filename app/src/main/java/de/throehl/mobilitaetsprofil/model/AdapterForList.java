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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Exchanger;

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

                    String date = conections_list.get(position).split("\n")[0].trim();
                    for (int i = 7; i < 10*7; i += 7){
                        String nDate = getCalculatedDate(date, "dd.MM.yyyy", i);
                        CalendarCollection.date_collection_arr.add(new CalendarCollection(nDate, conections_list.get(position).replace(date, nDate)));
                    }


                }

                else {
                    iteration_list.set(position, "aus");
                    row2.setTextColor(Color.argb(255, 108,3,22));
                    ArrayList<CalendarCollection> copy = (ArrayList<CalendarCollection>) CalendarCollection.date_collection_arr.clone();
                    for (CalendarCollection c: copy){
                        Log.d("AdapterList - tryremove", c.toString()+" --- \n"+conections_list.get(position).split("\n")[2]);
                        if (c.connections_info.contains(conections_list.get(position).split("\n")[2]) && !c.date.equals(conections_list.get(position).split("\n")[0].trim())){
                            Log.d("AdapterList - removed", c.toString());
                            CalendarCollection.date_collection_arr.remove(c);
                        }
                    }
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
                    Log.d("AdapterList", s[2].split("\t")[0].trim());
                    int id  = ControllerFactory.getDbController().getRouteID(s[2].split("\t")[1].trim(), s[2].split("\t")[0].trim());
                    String date = s[0].trim();
                    Log.d("AdapterList", date);
                    String time = s[2].split("\t")[0];
                    String nDate = date.substring(8)+date.substring(3,5)+date.substring(0,2);
                    Log.d("AdapterList", time);
                    String nTime = time.replace(".", "").replace(":", "");
                    Log.d("AdapterList", nDate+"\t"+nTime);

//                    ControllerFactory.getTtController().startAutoCheck(s[3].trim(), s[0].trim(), s[2].trim(), ""+id);
                    ControllerFactory.getTtController().startAutoCheck(conections_list.get(position), nDate, nTime, ""+id);
                }

                else {
                    delay_list.set(position, "aus");
                    row3.setTextColor(Color.argb(255, 108,3,22));
                    String[] s = conections_list.get(position).split("\n");
                    int id  = ControllerFactory.getDbController().getRouteID(s[2].split("\t")[1].trim(), s[2].split("\t")[0].trim());
                    ControllerFactory.getTtController().stopAutoCheck(conections_list.get(position), ""+id);
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
//        Log.d("Adapter", conections_list.toString());
        a.notifyDataSetChanged();
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {


        try {
            Calendar cal = Calendar.getInstance();
//            Log.d("AdapterList - Cal", cal.getTime().toString());
            SimpleDateFormat s = new SimpleDateFormat(dateFormat);
            cal.setTime(new Date(s.parse(date).getTime()));
//            Log.d("AdapterList - Cal", cal.getTime().toString());
            cal.add(Calendar.DAY_OF_YEAR, days);
//            Log.d("AdapterList - Cal", cal.getTime().toString());
            return s.format(cal.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }

    public String getAlarm(int pos){
        Log.d("AdapterList - alarm", delay_list.size()+"");
        if (delay_list.size() <= pos) return "none";
        return delay_list.get(pos);
    }

    public String getRepeat(int pos){
        Log.d("AdapterList - iter", iteration_list.size()+"");
        if (iteration_list.size() <= pos) return "none";
        return iteration_list.get(pos);
    }
}
