package com.uarever.principalphotography;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/*
 * The only way to use "this" as the 3rd parameter of initLoader was importing
 * the "...support.v4..." classes below instead of the ones without "...support.v4..."
 */
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.TextView;

import com.uarever.principalphotography.data.PPContract.ProjectEntry;
import com.uarever.principalphotography.util.LocalDb;
import com.uarever.principalphotography.util.PPHelper;

import java.util.ArrayList;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPositionsMyDatesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Initial date for listing projects
    private static Long mInitialDate;

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MyPositionsMyDatesFragment.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + MyPositionsMyDatesFragment.class.getSimpleName() + "):";

    ProjectCursorAdapter mCursorAdapter;

    private static final int MYPOSITION_MYDATES_LOADER = 0;

    private static int mCount;

    // Workaround for the problem of onPageSelected not being called on the very first fragment
    private static boolean firstLoad = true;

    // Since the preference manager does not currently store a strong reference to the listener,
    // it's necessary to store a strong reference to the listener otherwise it will be susceptible
    // to garbage collection.
    // https://developer.android.com/reference/android/content/SharedPreferences.html
    SharedPreferences.OnSharedPreferenceChangeListener prefListener;

    // View to show search results.
    private static View mMainView;


    public MyPositionsMyDatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // in the Fragment, we need to inflate the view from the XML layout resource ID
        // and return that view in the onCreateView() method.
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get the ListView by calling findViewById(int) on the rootView object,
        // which should contain children views such as the ListView.
        ListView projectsView = (ListView) rootView.findViewById(R.id.text_view_projects);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = rootView.findViewById(R.id.empty_view);
        projectsView.setEmptyView(emptyView);

        // Set the text to be shown based on the existance of any project the the city
        TextView noProjectsView = emptyView.findViewById(R.id.no_projects_text);
        noProjectsView.setText((LocalDb.getNumberOfProjects() > 0 ?
                getString(R.string.no_projects_found_text) : getString(R.string.no_projects_exist_text)));

        // Set the header part of the empty view
        TextView emptyHeader = (TextView) rootView.findViewById(R.id.empty_search_results);
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
        emptyHeader.setText(getString(R.string.project_starting_after)
                + " " +  dateFormat.format(PPHelper.getMyDate(1, getContext())));
                //+ "\n" + Locations.getLocationNameByCode(getContext(), PPHelper.getMyLocation(getContext())));


        // Setup an Adapter to create a list item for each project
        mCursorAdapter = new ProjectCursorAdapter(getActivity(), null);
        projectsView.setAdapter(mCursorAdapter);

        // Inflate the view that will show the search results
        mMainView = inflater.inflate(R.layout.results_header, projectsView, false);
        // Add the view with the results as the 1st item in the list
        projectsView.addHeaderView(mMainView);


        // Set up item click listener
        projectsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Do nothing if the click was on the header
                if (position != 0) {
                    // Create a new intent to go to {@link EditorActivity}
                    Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);

                    // Build the content URI that represents the specific project that was clicked on,
                    // by appending the "id" (passed as input to this method) onto the
                    Uri currentProjectUri = ContentUris.withAppendedId(ProjectEntry.CONTENT_URI, id);

                    // Set the URI on the data field of the intent
                    intent.setData(currentProjectUri);

                    // Send the intent to launch a new activity
                    startActivity(intent);
                }
            }
        });

        // Define a listener for changes in sharedPreferences
        SharedPreferences preferences =
                getActivity().getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // If initial date or crew positions have been changed, restart the loader
                // to reflect the new date and positions
                if (key.equals(MainActivity.PREF_DATE1) || key.equals(MainActivity.PREF_POSITIONS)) {
                    resetMyLoader();
                }
            }
        };
        // TODO: Is it necessary to unregister?
        // Register the listener
        preferences.registerOnSharedPreferenceChangeListener(prefListener);

        return rootView;
    }


    // Overrriding onReume just to call initLoader, as suggested at
    // https://stackoverflow.com/questions/15515799/should-we-really-call-getloadermanager-initloader-in-onactivitycreated-which
    @Override
    public void onResume() {
        super.onResume();
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //Log.v(DEBUG_TAG, "onResume MyMy, loader " + MYPOSITION_MYDATES_LOADER);
        getLoaderManager().initLoader(MYPOSITION_MYDATES_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about
        String[] projection = {
                ProjectEntry._ID,
                ProjectEntry.COLUMN_PROJECT_TITLE,
                ProjectEntry.COLUMN_PROJECT_PRODUCER,
                ProjectEntry.COLUMN_PROJECT_DATE1,
                ProjectEntry.COLUMN_PROJECT_DATE2,
                ProjectEntry.COLUMN_PROJECT_NEEDED_CREW};

        // set the initial date for listing projects
        mInitialDate = PPHelper.getMyDate(1, getContext());

        /*
         * The query condition ("WHERE") will be an "AND" of 2 parts:
         * part 1) date greater or equal the initial date, like "date1 >= 999999"
         * part 2) one or more positions, like "(position & DP) OR (position & GAFFER) ..."
         */

        // Build the first part of the query condition
        String selection1stPart = ProjectEntry.COLUMN_PROJECT_DATE1 + ">= ?";

        // To build the second part of the query condition we need to get an array with
        // the positions selected by the user
        ArrayList<Integer> positions = PPHelper.getMyPositions(getContext());

        // Then we parse the position to fill the second part of the query conditions
        // TODO: It assumes that at least 1 position is selected. I may need to review this.
        String selection2ndPart = " AND ((" + ProjectEntry.COLUMN_PROJECT_NEEDED_CREW + " & ?" + ")";
        int i = 1;
        while (i < positions.size()) {
            selection2ndPart += " OR (" + ProjectEntry.COLUMN_PROJECT_NEEDED_CREW + " & ?" + ")";
            i++;
        }
        selection2ndPart += ")";

        String selection = selection1stPart + selection2ndPart;

        // As we don't know in advance how many positions we'll deal with, we need
        // to declare selectionArgs[] in two steps
        String[] selectionArgs;
        // number os crew positions plus initial date
        selectionArgs = new String[positions.size() + 1];

        // Fist array element is the initial date
        selectionArgs[0] = Long.toString(mInitialDate);

        // Fill the rest of the array with crew positions selected by the user
        for (i = 0; i < positions.size(); i++) {
            selectionArgs[i + 1] = Integer.toString(positions.get(i));
        }

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(), ProjectEntry.CONTENT_URI,
                projection, selection, selectionArgs, ProjectEntry.COLUMN_PROJECT_DATE1);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update information to be shown on header by updateHeader() in MainActivity
        // The updateHeader() is called a new fragment is visible to the user
        // See viewPager.addOnPageChangeListener in MainActivity
        mCount = data.getCount();

        // Call the updateHeader method for situations when the loader is restarted due
        // changes in preferences
        ((MainActivity) getActivity()).updateHeader(mCount, mInitialDate, 0, mMainView);

        // Update {@link PetCursorAdapter} with this new cursor containing updated project data
        // (The framework will take care of closing the old cursor once we return.)
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the date needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    /*
     * Getter method to provide the last count of results
     */
    public static int getNumberOfResults() {
        return mCount;
    }

    /*
     * Getter method to provide the initial date
     */
    public static long getInitialDate() {
        return mInitialDate;
    }

    /*
     * Helper method to restart the loader when preferences are changed
     */
    public void resetMyLoader() {
        Loader<Cursor> loader;
        // Restart loader if it exists and if it's attached to the activity (checked by the isAdded);
        if (isAdded()){
            loader = getLoaderManager().getLoader(MYPOSITION_MYDATES_LOADER);
            if (loader != null) {
                getLoaderManager().restartLoader(MYPOSITION_MYDATES_LOADER, null, this);
            }
        }
    }

    /*
    * Helper method to return the view that shows the results.
    * It's being called when a ViewPager is made active, in order to update the results
    */
    public static View getResultView() {
        return mMainView;
    }
}
