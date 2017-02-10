package de.throehl.mobilitaetsprofil.view;

/**
 * Created by tetiana on 26.01.17.
 */

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.GregorianCalendar;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.model.CalendarAdapter;
import de.throehl.mobilitaetsprofil.model.CalendarCollection;


public class Calendar extends Fragment{
    public GregorianCalendar calendarMonth, calendarMonthCopy;
    private CalendarAdapter calendarAdapter;
    private TextView tvMonth;
    Context context;
    //final View rootView = inflater.inflate(R.layout.your_connections, container, false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.calender, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));



        tvMonth = (TextView) rootView.findViewById(R.id.id_tv_month);
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", calendarMonth));

        ImageButton previous = (ImageButton) rootView.findViewById(R.id.id_month_back);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageButton next = (ImageButton) rootView.findViewById(R.id.id_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        GridView gridview = (GridView) rootView.findViewById(R.id.id_gv_calendar);
        gridview.setAdapter(calendarAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("\\.");
                String gridvalueString = separatedTime[0].replaceFirst("^0*","");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate);
            }

        });




        // TODO
        //find  @id hier
//        final CalendarView cal = (CalendarView)rootView.findViewById(R.id.calendarView);
//        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                view.setBackgroundColor(0xFF0000);
//                Log.d("TAG", year+"\t" +month+"\t"+dayOfMonth);
//            }
//        });
        return rootView;
    }

    public static Calendar newInstance(Context activity_context){
        Calendar calender = new Calendar();
        calender.context = activity_context; //for new activity
        return calender;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calender);

        calendarMonth = (GregorianCalendar) GregorianCalendar.getInstance();
        calendarMonthCopy = (GregorianCalendar) calendarMonth.clone();
        calendarAdapter = new CalendarAdapter(context, calendarMonth, CalendarCollection.date_collection_arr);

    }


    protected void setNextMonth() {
        if (calendarMonth.get(GregorianCalendar.MONTH) == calendarMonth.getActualMaximum(GregorianCalendar.MONTH)) {
            calendarMonth.set((calendarMonth.get(GregorianCalendar.YEAR) + 1),
                    calendarMonth.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            calendarMonth.set(GregorianCalendar.MONTH,
                    calendarMonth.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (calendarMonth.get(GregorianCalendar.MONTH) == calendarMonth.getActualMinimum(GregorianCalendar.MONTH)) {
            calendarMonth.set((calendarMonth.get(GregorianCalendar.YEAR) - 1),
                    calendarMonth.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            calendarMonth.set(GregorianCalendar.MONTH,
                    calendarMonth.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        calendarAdapter.refreshDays();
        calendarAdapter.notifyDataSetChanged();
        tvMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", calendarMonth));
    }




    public void onAddEventClicked(View view){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        java.util.Calendar cal = java.util.Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, "Neel Birthday");
        intent.putExtra(CalendarContract.Events.DESCRIPTION,  "This is a sample description");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("CalendarActivity", "onResume\n"+CalendarCollection.date_collection_arr);
        refreshCalendar();
    }
}
