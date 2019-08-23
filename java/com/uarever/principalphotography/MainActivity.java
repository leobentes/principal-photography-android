package com.uarever.principalphotography;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;


import com.uarever.principalphotography.util.DatePickerFragment;
import com.uarever.principalphotography.util.LocalDb;

import java.text.SimpleDateFormat;

import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    // Tag for the log messages
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + MainActivity.class.getSimpleName() + "):";

    // Set the build variant
    public static final String BUILD_VAR = BuildConfig.FLAVOR;
    public static final String FLAVOR_FILTERS = "filtersUI";
    public static final String FLAVOR_TABS = "tabsUI";


    // Preferences constants
    public static final String PREFS_FILE = "MyPPPreferences";
    public static final String PREF_DATE1 = "myDate1";
    public static final String PREF_DATE2 = "myDate2";
    public static final String PREF_POSITIONS = "myPositions";
    public static final String PREF_LOCATION = "myLocation";
    public static final String PREF_RATE = "myRate";

    /**
     * Spinner for filtering the results
     */
    private Spinner mFilterSpinner;

    /**
     * ViewPager
     **/
    ViewPager mViewPager;

    ActionBar mActionBar;

    SharedPreferences mPreferences;

    // View for showing search results
    View mHeader;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Find the view pager that will allow the user to swipe between fragments
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        ProjectsFragmentAdapter adapter = new ProjectsFragmentAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        mViewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(mViewPager);

        //ViewGroup rootView = findViewById(android.R.id.content);
        //mHeader = LayoutInflater.from(this).inflate(R.layout.results_header, rootView);


        // Get the preferences
        mPreferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        // If the mandatory preference "myLocation" does not exist, ask and set all values;
        if (!mPreferences.contains(PREF_LOCATION)) {
            // Create a new intent and go to {@link FirstSetupActivity}
            Intent intent = new Intent(this, FirstSetupActivity.class);
            startActivity(intent);
            setMyInitialDate (mPreferences);
            setMyFinalDate (mPreferences);
        }

        // Listen for changes in pages so the head can be updated
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int fragmentNumber) {
                switch (fragmentNumber) {
                    case 0:
                        updateHeader(MyPositionsMyDatesFragment.getNumberOfResults(),
                                MyPositionsMyDatesFragment.getInitialDate(), fragmentNumber,
                                MyPositionsMyDatesFragment.getResultView());
                        break;
                    case 1:
                        updateHeader(MyPositionsAllDatesFragment.getNumberOfResults(),
                                MyPositionsAllDatesFragment.getInitialDate(), fragmentNumber,
                                MyPositionsAllDatesFragment.getResultView());
                        break;
                    case 2:
                        updateHeader(AllPositionsMyDatesFragment.getNumberOfResults(),
                                AllPositionsMyDatesFragment.getInitialDate(), fragmentNumber,
                                AllPositionsMyDatesFragment.getResultView());
                        break;
                    case 3:
                        updateHeader(AllPositionsAllDatesFragment.getNumberOfResults(),
                                AllPositionsAllDatesFragment.getInitialDate(), fragmentNumber,
                                AllPositionsAllDatesFragment.getResultView());
                        break;
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options and add menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_change_positions:
                selectCrewPositions();
                return true;
            // Respond to a click on the "Change dates" menu option
            case R.id.action_change_dates:
                showDateDialog();
                return true;
            // Respond to a click on the "Change City" menu option
            case R.id.action_change_location:
                setMyLocation();
                return true;
            // Respond to a click on the "Update projects list" menu option
            case R.id.action_update_local_DB:
                LocalDb.manualUpdate(this);
                return true;
            // Respond to a click on the "Update projects list" menu option
            case R.id.action_help:
                help();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Update the header based on query results and the fragment being showed
     * @param numberOfResults is the number of projects available for a specific search
     * @param date is the initial date for the list of projecyt being shown
     * @param currentPage the viewPager page number
     * @param view the view associated to the fragment being show
     */
    public void updateHeader(int numberOfResults, long date, int currentPage, View view) {
        // Test if the page number received as parameter is the active page
        // This is necessary because this helper method may being called by a loader
        // which data are not currently being show. For example, if a preference (like
        // date or crew position) if updated the respective loader is reloaded in order to
        // perform a new search, however such results list may not be the one on screen at
        // the time
        if (currentPage != mViewPager.getCurrentItem()){
            return;
        }

        // Get the view to be updated with the results
    TextView firstLine = (TextView) view.findViewById(R.id.search_results);

    // Format the date and show results
    SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
    firstLine.setText(numberOfResults + " " + getString(R.string.project_starting_after) + " "
            + dateFormat.format(date));
    /*SimpleDateFormat dfAux = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    firstLine.setText(numberOfResults + " " + getString(R.string.project_starting_after) + " "
            + dateFormat.format(date) + "\nDB atualizado em  " + dfAux.format(LocalDb.getLastUpdate(this)));*/
}


    /*
     * Set user location
     */
    //private void setMyLocation(SharedPreferences preferences) {
    private void setMyLocation() {
        // Allow user to choose their location
        //FragmentManager fm = getSupportFragmentManager();
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, new SetLocationFragment())
                .commit();*/

        // Create a new intent to go to {@link SetLocationActivity}
        Intent intent = new Intent(this, SetLocationActivity.class);

        // Send the intent to launch a new activity
        startActivity(intent);
    }

    /*
     * Set preferred initial date
     */
    private void setMyInitialDate(SharedPreferences preferences) {
        // Allow user to choose their initial date
        showDateDialog();

        // TODO: come back to this logic
        /* Right now the DatePickerFragment puts the chosen date in the SharedPreferences file
           but it may be better if all editor.commit() are done inside setMyWhatever helper
           method (as in setMyCrewPositions and setMyRates */
    }

    /*
     * Set preferred final date
     */
    private void setMyFinalDate(SharedPreferences preferences) {
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putLong(PREF_DATE2, 0L);
        editorPref.commit();

        // TODO: implement in the same way of setMyInitialDate
    }

    /*
     * Allow the user to pick dates for the project listing
     */
    private void showDateDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dateDialog = new DatePickerFragment();
        dateDialog.show(fm, "");
    }


    /*
     * Allow the user to their pick crew positions
     */
    private void selectCrewPositions() {
        // Create a new intent to go to {@link SetCrewActivity}
        Intent intent = new Intent(this, SetCrewActivity.class);

        // Send the intent to launch a new activity
        startActivity(intent);
    }

    /*
 * Allow the user to their pick crew positions
 */
    private void help() {
        // Create a new intent to go to {@link HelpActivity}
        Intent intent = new Intent(this, HelpActivity.class);

        // Send the intent to launch a new activity
        startActivity(intent);
    }


    /*
     * Set rates preference
     */
    /*private void setMyRate(SharedPreferences preferences) {
        // Allow user to choose their rate
        // TODO: create a picker
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putInt(PREF_RATE, 0);
        editorPref.commit();
    }*/

}


