package com.uarever.principalphotography.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.uarever.principalphotography.MainActivity;
import com.uarever.principalphotography.R;

import java.util.Calendar;


/**
 * Created by leobentes on 11/23/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String LOG_TAG = DatePickerDialog.class.getSimpleName();
    public static final String DEBUG_TAG = "PPDebug ("+ DatePickerDialog.class.getSimpleName() +"):";


    private static Long mInitialDate = 0L;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get today's date
        final Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();

        // Use the preferred initial date as the default date in the picker
        SharedPreferences preferences =
                getActivity().getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);
        calendar.setTimeInMillis(preferences.getLong(MainActivity.PREF_DATE1, today));

        // Set year, month and day to be passed to the picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        // "android.R.style.Theme_Holo_Dialog_MinWidth" is used to have the picker as spinner
        // instead of calendar
        DatePickerDialog dPicker = new DatePickerDialog(getActivity(),
                android.R.style.Theme_Holo_Dialog_MinWidth, this, year, month, day);
        dPicker.setTitle(getString(R.string.datepicker_select_initial_date));
        return dPicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Set the date to the one chose by the user and time to midnight
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Set the initial date in the preferences file
        SharedPreferences preferences =
                getActivity().getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putLong(MainActivity.PREF_DATE1, calendar.getTimeInMillis());
        editorPref.commit();

        // Force the loader to be restart in order to show a the project list
        // considering the new initial date
        //((MainActivity)getActivity()).restartLoader();
    }

    /**
     * Helper method to set the initial date for listing projects
     */
    public static void setInitialDate (Calendar calendar){
        mInitialDate = calendar.getTimeInMillis();
    }
    /**
     * Helper method to read the initial date for listing projects
     */
    public static Long getInitialDate (){
        return mInitialDate;
    }
}
