package com.uarever.principalphotography;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by leobentes on 11/24/17.
 */

/**
 * {@link ProjectsFragmentAdapter} is a {@link FragmentPagerAdapter} that can provide the layout for
 * each list item based on the data source.
 */
public class ProjectsFragmentAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ProjectsFragmentAdapter.class.getSimpleName();
    public static final String DEBUG_TAG = "PPDebug ("+ ProjectsFragmentAdapter.class.getSimpleName() +"):";


    /**
     * Create a new {@link ProjectsFragmentAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public ProjectsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        //Log.d (DEBUG_TAG, "position = " + position);
        switch (position) {
            case 0:
                //Log.d (DEBUG_TAG, "instanciando My My");
                return new MyPositionsMyDatesFragment();
            case 1:
                //Log.d (DEBUG_TAG, "instanciando My All");
                return new MyPositionsAllDatesFragment();
            case 2:
                //Log.d (DEBUG_TAG, "instanciando All My");
                return new AllPositionsMyDatesFragment();
            case 3:
                //Log.d (DEBUG_TAG, "instanciando All All");
                return new AllPositionsAllDatesFragment();
            default:
                throw new IllegalArgumentException("Invalid Fragment getItem position");
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_mypos_mydates);
            case 1:
                return mContext.getString(R.string.tab_mypos_alldates);
            case 2:
                return mContext.getString(R.string.tab_allpos_mydates);
            case 3:
                return mContext.getString(R.string.all_mypos_alldates);
            default:
                throw new IllegalArgumentException("Invalid CharSequence getPageTitle position");
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
            return 4;
    }
}
