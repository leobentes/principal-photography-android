package com.uarever.principalphotography;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FirstSetupActivity extends AppCompatActivity {

    public static final String LOG_TAG = FirstSetupActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + FirstSetupActivity.class.getSimpleName() + "):";

    static final int RC_SET_LOCATION = 1;
    static final int RC_SET_POSITIONS = 2;

    private Context that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);


        that = getApplication();

        // Create an AlertDialog.Builder, set the message and click listeners for the options
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_preferences_set);

        builder.setNeutralButton(R.string.text_continue, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // Asks the user to set the location (city)
                Intent intent = new Intent(that, SetLocationActivity.class);

                // Send the intent to launch a new activity
                startActivityForResult(intent, RC_SET_LOCATION);
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RC_SET_LOCATION) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // City was selected, now it's time to select crew positions
                Intent intent = new Intent(that, SetCrewActivity.class);
                startActivityForResult(intent, RC_SET_POSITIONS);
            }
        }

        // Check which request we're responding to
        if (requestCode == RC_SET_POSITIONS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Positions were selected, now it's time to select dates
                // TODO: At the moment the dates are selected inside main activivity. Move it to here.
                finish();
            }
        }
    }
}
