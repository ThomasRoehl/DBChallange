package de.throehl.mobilitaetsprofil.view;

import android.content.Intent;
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

import de.throehl.mobilitaetsprofil.R;
import de.throehl.mobilitaetsprofil.controller.ViewControllerFactory;

public class ConnectionSearchActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String start = "";
    private String dest = "";
    private final String TAG = "CSA";
    Connections_search tab1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_search);

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        displayItem();
        Log.d(TAG, "onCreate");
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
                    Log.d(TAG, "newInstance\t"+start+"\t"+dest);
                    return tab1;
                case 1:
                    Calender tab2 = Calender.newInstance(getApplicationContext());
                    return tab2;
                case 2:
                    Your_connections tab3 = new Your_connections();
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
