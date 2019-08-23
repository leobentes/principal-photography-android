package com.uarever.principalphotography;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.uarever.principalphotography.util.LocalDb;
import com.uarever.principalphotography.util.PPHelper;

/**
 * Created by leobentes on 01/29/18.
 */
/*
 * Set the location selected by the user
 */
public class SetLocationActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SetLocationActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + SetLocationActivity.class.getSimpleName() + "):";

    String mLocationCode = null;

    /**
     * Spinner for selecting the location
     */
    private Spinner mLocationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.location_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.location_select_activity_title));

        mLocationSpinner = (Spinner) findViewById(R.id.location_spinner);

        mLocationCode = PPHelper.getMyLocation(this);
        // Log.v(DEBUG_TAG, "Cidade atual = " + mLocationCode);

        chooseLocation();
    }


    private void chooseLocation() {

        // Create adapter for the spinner. The list options are from the String array it will use
        // The spinner will use a custom layout
        ArrayAdapter locationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_location_options, R.layout.custom_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        locationSpinnerAdapter.setDropDownViewResource(R.layout.custom_simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mLocationSpinner.setAdapter(locationSpinnerAdapter);

        if (mLocationCode != null) {
            mLocationSpinner.setSelection(Locations.getIndexByCode(this, mLocationCode));
        }


        // Set the integer mSelected to the constant values
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String location = (String) parent.getItemAtPosition(position);
                mLocationCode = Locations.getLocationCodeByName(getApplicationContext(), location);
                // Log.v (DEBUG_TAG, "location [" + location + "] selected = " + mLocationCode);

            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mLocationCode = null;
            }
        });
    }

    /**
     * This method is called when the cancel button is clicked.
     * It doesn't need to do anything so it only finishes the activity.
     */
    public void cancelLocationSelection(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * This method is called when the save button is clicked.
     */
    public void saveLocationSelection(View view) {

        if (mLocationCode == null) {
            Toast.makeText (this, getText(R.string.please_select_location), Toast.LENGTH_SHORT).show();
            return;
        }

        // Save crew positions preferences
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putString(MainActivity.PREF_LOCATION, mLocationCode);
        editorPref.commit();

        // Rebuild DB
        LocalDb.rebuildLocalDb(this, PPHelper.getMyLocation(this), true);

        setResult(RESULT_OK);

        // Exit the activity
        finish();
    }


}
