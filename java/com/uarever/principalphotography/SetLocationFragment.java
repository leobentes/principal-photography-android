package com.uarever.principalphotography;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetLocationFragment extends Fragment {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SetLocationFragment.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug ("+ SetLocationFragment.class.getSimpleName() +"):";

    /**
     * Spinner for selecting the location
     */
    private Spinner mLocationSpinner;

    public SetLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.v(DEBUG_TAG, "dentro de onCreateView (fragment)");
        // Inflate the layout for this fragment
        View locationView = inflater.inflate(R.layout.fragment_set_location, container, false);
        //Log.v(DEBUG_TAG, "depois de inflar (fragment)");

        mLocationSpinner = (Spinner) locationView.findViewById(R.id.location_spinner2);

        chooseLocation();

        return locationView;
    }

    private void chooseLocation() {

        // Create adapter for the spinner. The list options are from the String array it will use
        // The spinner will use a custom layout
        ArrayAdapter locationSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_location_options, R.layout.custom_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        locationSpinnerAdapter.setDropDownViewResource(R.layout.custom_simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mLocationSpinner.setAdapter(locationSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String location = (String) parent.getItemAtPosition(position);
                //Log.v (DEBUG_TAG, "location [" + location + "] selected");
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

}
