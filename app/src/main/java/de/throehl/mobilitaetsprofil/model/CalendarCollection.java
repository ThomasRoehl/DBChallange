package de.throehl.mobilitaetsprofil.model;

/**
 * Created by tetiana on 08.02.17.
 */
//package com.androidfromhome.calendar.util;

import java.util.ArrayList;

public class CalendarCollection {
    public String date="";
    public String connections_info ="";

    public static ArrayList<CalendarCollection> date_collection_arr;
    public CalendarCollection(String date,String event_message){

        this.date=date;
        this.connections_info =event_message;

    }

    @Override
    public String toString(){
        return "Date:\t"+date+"\nMessage:\t"+connections_info;
    }

}