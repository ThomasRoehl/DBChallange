package de.throehl.mobilitaetsprofil.model;

/**
 * Created by tetiana on 08.02.17.
 */
//package com.androidfromhome.calendar.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.throehl.mobilitaetsprofil.R;


public class AndroidListAdapter extends ArrayAdapter<CalendarCollection>{

    private final Context context;
    private final ArrayList<CalendarCollection> values;
    private ViewHolder vHolder;
    private final int resourceId;

    public AndroidListAdapter(Context context, int resourceId,ArrayList<CalendarCollection> values) {
        super(context, resourceId, values);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.values = values;
        this.resourceId = resourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, parent, false);

            vHolder = new ViewHolder();
            vHolder.save_connections_date = (TextView) convertView.findViewById(R.id.is_save_connections_date);
            vHolder.save_connectins = (TextView) convertView.findViewById(R.id.id_save_connection);


            convertView.setTag(vHolder);


        }else
        {
            vHolder = (ViewHolder) convertView.getTag();
        }
        CalendarCollection list_obj=values.get(position);
        vHolder.save_connections_date.setText(list_obj.date);
        vHolder.save_connectins.setText(list_obj.connections_info);

        return convertView;
    }


    public class ViewHolder {
        TextView save_connectins;
        TextView save_connections_date;

    }

}