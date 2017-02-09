package de.throehl.mobilitaetsprofil.view;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ControllerFactory;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;
import de.throehl.mobilitaetsprofil.dummy.DummyTravelPlan;
import de.throehl.mobilitaetsprofil.model.dbEntries.ConnectionInformation;
import de.throehl.mobilitaetsprofil.model.dbEntries.Transfers;
import de.throehl.mobilitaetsprofil.model.dbEntries.UserSaves;

public class ConnectionSearchActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String start = "";
    private String dest = "";
    private final String TAG = "CSA";
    private Connections_search tab1;
    private Calender tab2;
    private Your_connections tab3;
    public static String className;

    private Handler transferHandler;
    private Runnable transferRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_search);
        className = this.getClass().getName();
        ViewControllerFactory.addActivity(this.getClass().getName(), this);
        checkForExist();

        String tmp = checkForStart();
        if (tmp != null) start = tmp;
        tmp = checkForDest();
        if (tmp != null) dest = tmp;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    Log.d(TAG, "pageSelect Calendar\t");
                    tab2.refreshCalendar();
                }
                else if (position == 2){
                    tab3.initEntries();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        displayItem();
        Log.d(TAG, "onCreate");
        DummyTravelPlan d = new DummyTravelPlan();
        ControllerFactory.getDbController().insertTravelPlan(d.getCon(1));

        transferHandler = new Handler();
        transferRun = new Runnable() {
            @Override
            public void run() {
                transferCheck();
            }
        };
        transferHandler.postDelayed(transferRun, 5000);
    }

    public void transferCheck(){
        Log.d(TAG, "Do transferCheck");
        ArrayList<String> res = ControllerFactory.getDbController().getTransfer(ControllerFactory.getID());
        for (String s: res){
            Log.d(TAG, "Transfer Recieved");
            Toast.makeText(ControllerFactory.getAppContext(), "Neue Verbindung erhalten", Toast.LENGTH_SHORT).show();
            ControllerFactory.getDbController().insertUserSave(new UserSaves(ControllerFactory.getID(), s));
        }
        ControllerFactory.getDbController().removeTransfer(ControllerFactory.getID());
        transferHandler.postDelayed(transferRun, 10000);
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkForExist();
        String tmp = checkForStart();
        if (tmp != null) start = tmp;
        tmp = checkForDest();
        if (tmp != null) dest = tmp;
        if (tab1 != null)
            tab1.updateView(start, dest);
        displayItem();
        if (tab3 != null)
            checkForNewRoutes();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "new Intent found");
    }


    private void checkForExist(){
        Intent i = getIntent();
        if (i.getExtras() != null){
            if (i.getExtras().containsKey("ACTIVITY"))
                ViewControllerFactory.closeActivity(i.getExtras().getString("ACTIVITY"));
        }
    }

    private String checkForDest(){
        Intent i = getIntent();
        String s = null;
        if (i.getExtras() != null) {
            if (i.getExtras().containsKey("DEST")) {
                s = i.getExtras().getString("DEST");
                tab1.endLoc = dest;
            }
        }
        return s;
    }

    private String checkForStart(){
        Intent i = getIntent();
        String s = null;
        if (i.getExtras() != null) {
            i.getExtras().get("START");
            if (i.getExtras().containsKey("START")) {
                s = i.getExtras().getString("START");
                tab1.startLoc = start;
            }

        }
        return s;
    }

    private void displayItem(){
        Intent i = getIntent();
        if (i.getExtras() != null) {
            if (i.getExtras().containsKey("VIEW")) {
                int view = (int) getIntent().getExtras().get("VIEW");
                mViewPager.setCurrentItem(view);
            }
        }
    }

    private void checkForNewRoutes(){
        Intent i = getIntent();
        if (i.getExtras() != null){
            if (i.getExtras().containsKey("ADDED")){
                int id = (int) getIntent().getExtras().get("ADDED");
                Log.d(TAG, "ID:\t"+id);
                ConnectionInformation conInfo = ControllerFactory.getDbController().getTravelPlan(id);
//                tab3.addEntry(id, conInfo.getStations().get(0).getDATE(),conInfo.getTRAIN(), conInfo.getSTART(), conInfo.getStations().get(0).getDEPARTURE(), conInfo.getDEST(), conInfo.getStations().get(conInfo.getStations().size() -1).getDEPARTURE());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ControllerFactory.getDbController().insertTransfer(new Transfers("99", ControllerFactory.getID(), "99"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    tab1 = Connections_search.newInstance(getApplicationContext());
                    Log.d(TAG, "newInstance Search\t"+start+"\t"+dest);
                    return tab1;
                case 1:
                    tab2 = Calender.newInstance(getApplicationContext());
                    Log.d(TAG, "newInstance Calendar\t"+start+"\t"+dest);
                    return tab2;
                case 2:
                    if (tab3 == null)
                        tab3 = new Your_connections();
                    Log.d(TAG, "newInstance List\t"+start+"\t"+dest);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Reiseauskunft";
                case 1:
                    return "Kalender";
                case 2:
                    return "Verbindungen";
            }
            return null;
        }
    }
}
