package de.throehl.mobilitaetsprofil.view;
//package com.androidfromhome.calendar;

/**
 * Created by tetiana on 08.02.17.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;

public class ListViewActivity extends Activity implements OnClickListener {

    private ListView lv_android;
    private AndroidListAdapter list_adapter;
    private Button btn_calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        CalendarCollection.date_collection_arr=new ArrayList<CalendarCollection>();
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-02-01","John Birthday"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-02-01","Client Meeting at 5 p.m."));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-02-06","Hallo World"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-02-02","Marriage Anniversary"));
        CalendarCollection.date_collection_arr.add(new CalendarCollection("2017-02-11","Live Event and Concert of sonu"));

        getWidget();
    }

    /////////////////////////////////////////////////////////// <<
    // TODO: Das ist erste Seite:  weg lassen
    //
    public void getWidget(){
        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_calender.setOnClickListener(this);

        lv_android = (ListView) findViewById(R.id.lv_android);
        list_adapter=new AndroidListAdapter(ListViewActivity.this,R.layout.list_item, CalendarCollection.date_collection_arr);
        lv_android.setAdapter(list_adapter);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_calender:
                startActivity(new Intent(ListViewActivity.this,CalenderActivity.class));
                break;
            default:
                break;
        }

    }
    ///////////////////////////////////////////////////////////>>


}