package com.uarever.principalphotography;

import android.content.ContentUris;
import android.content.Intent;
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

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPositionsAllDatesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static Long mInitialDate;

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = AllPositionsAllDatesFragment.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug ("+ AllPositionsAllDatesFragment.class.getSimpleName() +"):";

    ProjectCursorAdapter mCursorAdapter;

    private static final int ALLPOSITION_ALLDATES_LOADER = 3;

    private static int mCount;

    // View to show search results.
    private static View mMainView;

    public AllPositionsAllDatesFragment() {
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
                + " " +  dateFormat.format(PPHelper.setDateToMidnight(System.currentTimeMillis())));
              //  + "\n" + Locations.getLocationNameByCode(getContext(), PPHelper.getMyLocation(getContext())));

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

        return rootView;
    }



    // Overrriding onReume just to call initLoader, as suggested at
    // https://stackoverflow.com/questions/15515799/should-we-really-call-getloadermanager-initloader-in-onactivitycreated-which
    @Override
    public void onResume()
    {
        super.onResume();
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //Log.v (DEBUG_TAG, "onResume AllAll, loader " + ALLPOSITION_ALLDATES_LOADER);
        getLoaderManager().initLoader(ALLPOSITION_ALLDATES_LOADER, null, this);
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

        mInitialDate = PPHelper.setDateToMidnight(System.currentTimeMillis());

        // Set the initial date for listing the projects
        // Select Date1 column for condition (WHERE DATE1 >= ...)
        String selection = ProjectEntry.COLUMN_PROJECT_DATE1 + ">=?";

        // The only array element is the initial date
        String [] selectionArgs = { Long.toString(mInitialDate) };

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
        ((MainActivity) getActivity()).updateHeader(mCount, mInitialDate, 3, mMainView);

        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        // (The framework will take care of closing the old cursor once we return.)
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the date needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    /*
     * Getter method to retrieve last count of results
     */
    public static int getNumberOfResults(){
        return mCount;
    }

    /*
     * Getter method to provide the initial date
     */
    public static long getInitialDate(){
        return mInitialDate;
    }

    /*
      * Helper method to return the view that shows the results.
      * It's being called when a ViewPager is made active, in order to update the results
      */
    public static View getResultView() {
        return mMainView;
    }
}

