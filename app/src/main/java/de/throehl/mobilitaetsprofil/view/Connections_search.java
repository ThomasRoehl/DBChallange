package de.throehl.mobilitaetsprofil.view;

/**
 * Created by tetiana on 26.01.17.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;

public class Connections_search extends Fragment{

    EditText vonEditText;
    EditText nachEditText;
    Context context;
    /** Private members of the class */
    private TextView displayTime, displayDate;
    private FrameLayout pickTime, pickDate;
    public String startLoc = "";
    public String endLoc = "";

    private int pHour, pMinute, pDay,pYear, pMonth;

    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    private static final int TIME_DIALOG_ID = 0;
    private static final int DATE_DIALOG_ID = 1;

    public static Connections_search newInstance(Context activity_context){
        Connections_search connections_search = new Connections_search();
        connections_search.context = activity_context; //for new activity
        return connections_search;
    }

    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    view.setIs24HourView(true);  //?
                    pHour = hourOfDay;
                    pMinute = minute;
                    updateDisplay();
//                    displayToast();
                }
            };
/////////////////
    /** Callback received when the user "picks" a time in the dialog */
    private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            pDay = dayOfMonth;
            pMonth = monthOfYear;
            pYear = year;
            updateDisplay();
        }
    };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        displayTime.setText(
                new StringBuilder()
                        .append(pad(pHour)).append(":")
                        .append(pad(pMinute)));
        displayDate.setText(
                new StringBuilder()
                        .append(pad(pDay)).append(". ")
                        .append(pad(pMonth+1)).append(". ")
                        .append(pad(pYear)));

    }


    /** Add padding to numbers ++++++++++less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void updateView(String startLoc, String endLoc){
        this.startLoc = startLoc;
        this.endLoc = endLoc;
        Log.d("CSA", endLoc+"\t"+startLoc);
        nachEditText.setText(endLoc);
        vonEditText.setText(startLoc);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CSAa", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.connection_search, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        Log.d("CSAa", "onCreateView");
        vonEditText = (EditText) rootView.findViewById(R.id.id_start);

        vonEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), StartActivity.class);
                startActivity(intent);
            }
        });


        nachEditText = (EditText) rootView.findViewById(R.id.id_destination);

        nachEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), DestinationActivity.class);
                startActivity(intent);
            }
        });


        displayTime = (TextView) rootView.findViewById(R.id.timeDisplay);
        pickTime = (FrameLayout) rootView.findViewById(R.id.id_Time_onClock);

        /** Listener for click event of the button */
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("TAG", "Button pressed");
                //getActivity().showDialog(TIME_DIALOG_ID);
                // Create and show the dialog.
                createDialog(0).show(); ////////###################

            }
        });

        displayDate = (TextView) rootView.findViewById(R.id.dateDisplay);
        pickDate = (FrameLayout) rootView.findViewById(R.id.id_Datum_onClick);


        /** Listener for click event of the button */
        pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("TAG", "Button pressed");
                //getActivity().showDialog(TIME_DIALOG_ID);
                // Create and show the dialog.
                createDialog(1).show(); ////////###################

            }
        });


        Button mRegisterButton = (Button) rootView.findViewById(R.id.id_search);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ControllerFactory.getAppContext(), FoundConnectionsActivity.class);
                Log.d("TEST", vonEditText.getText().toString() + "\t"+ nachEditText.getText().toString());
                intent.putExtra("START", vonEditText.getText().toString());
                intent.putExtra("DEST", nachEditText.getText().toString());
                intent.putExtra("TIME", displayTime.getText().toString());
                intent.putExtra("DATE", displayDate.getText().toString());
                startActivity(intent);

            }
        });





        /** Get the current time */
        Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        pMonth =cal.get(Calendar.MONTH);
        pYear=cal.get(Calendar.YEAR);

        Log.d("TAG", pDay+"");
        Log.d("TAG", pMonth+"");
        Log.d("TAG", pYear+"");

        updateDisplay();

        return rootView;



    }


    //////77#####################
    public Dialog createDialog(int id) {
        Log.d("TAG", "create Dialog");
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(getActivity(), mTimeSetListener, pHour, pMinute, true);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity(), mDataSetListener, pYear, pMonth, pDay);
        }
        return null;
    }

}


/** Displays a notification when the time is updated */
//    private void displayToast() {
//        Toast.makeText(context, new StringBuilder().append("Time choosen is ").append(displayTime.getText()),   Toast.LENGTH_SHORT).show();
//
//    }
