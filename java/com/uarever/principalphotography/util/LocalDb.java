package com.uarever.principalphotography.util;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.uarever.principalphotography.R;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.uarever.principalphotography.data.PPContract.ProjectEntry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


/**
 * Created by leobentes on 1/18/18.
 */

public class LocalDb {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = LocalDb.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + LocalDb.class.getSimpleName() + "):";

    private static String FILENAME = "last_db_update";

    // update frequency in hours
    private static int updateFrequency = 12;

    // Total projects in firebase DB for choosen city
    private static int numberOfProjects = 0;

    public static void rebuildLocalDb (Context c, String city, final boolean resetScheduler){

        final Context context = c;

        // TODO: make this constant global
        /* Constant for the projects "table" */
        final String PROJECTS = "projects";


        // TODO: make this constant global
        /* Constants for projects fields */
        final String BUDGET = "budget";
        final String CREWBITS = "crewBits";
        final String DATE1 = "date1";
        final String DATE2 = "date2";
        final String CITY = "city";
        final String PRODEMAIL = "prodEmail";
        final String PRODPHONE = "prodPhone";
        final String PRODUCER = "producer";
        final String TITLE = "title";


        String projectsPath = city + "/" + PROJECTS;

        // Entry point and initialization for the app to access the firebase database
        final FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance();;

        // Entry point and references for projects root entries
        DatabaseReference projectsInfoDbReference = firebaseDb.getReference().child(projectsPath);

        // Read (async/listener) projects tree
        projectsInfoDbReference.addListenerForSingleValueEvent(new ValueEventListener() {

            String projectsPushId;
            Long budget;
            Long crewBits;
            Long date1;
            Long date2;
            String city;
            String prodEmail;
            String prodPhone;
            String producer;
            String title;

            @Override
            public void onDataChange(DataSnapshot projectsSnapshot) {

                // Drop the database before creating new items
                int rowsDeleted = context.getContentResolver().delete(ProjectEntry.CONTENT_URI, null, null);
                //Log.v(DEBUG_TAG, rowsDeleted + " rows deleted from project database");

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                Uri uri;

                // Total projects in firebase DB for choosen city
                numberOfProjects = 0;

                for (DataSnapshot projectSnapshot: projectsSnapshot.getChildren()) {
                    projectsPushId = projectSnapshot.getKey();
                    budget = (Long) projectSnapshot.child(BUDGET).getValue();
                    crewBits = (Long) projectSnapshot.child(CREWBITS).getValue();
                    date1 = (Long) projectSnapshot.child(DATE1).getValue();
                    date2 = (Long) projectSnapshot.child(DATE2).getValue();
                    city = (String) projectSnapshot.child(CITY).getValue();
                    prodEmail = (String) projectSnapshot.child(PRODEMAIL).getValue();
                    prodPhone = (String) projectSnapshot.child(PRODPHONE).getValue();
                    producer = (String) projectSnapshot.child(PRODUCER).getValue();
                    title = (String) projectSnapshot.child(TITLE).getValue();

                    /*Log.v (DEBUG_TAG, "ID: " + projectsPushId);
                    Log.v (DEBUG_TAG, "budget: " + budget);
                    Log.v (DEBUG_TAG, "crewBits: " + crewBits);
                    Log.v (DEBUG_TAG, "date1: " + date1);
                    Log.v (DEBUG_TAG, "date2: " + date2);
                    Log.v (DEBUG_TAG, "city: " + city);
                    Log.v (DEBUG_TAG, "prodPhone: " + prodPhone);
                    Log.v (DEBUG_TAG, "prodEmail: " + prodEmail);
                    Log.v (DEBUG_TAG, "producer: " + producer);
                    Log.v (DEBUG_TAG, "Title: " + title);*/

                    values.put(ProjectEntry.COLUMN_PROJECT_PUSH_ID, projectsPushId);
                    values.put(ProjectEntry.COLUMN_PROJECT_TITLE, title);
                    values.put(ProjectEntry.COLUMN_PROJECT_PRODUCER, producer);
                    values.put(ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE, prodPhone);
                    values.put(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL, prodEmail);
                    values.put(ProjectEntry.COLUMN_PROJECT_DATE1, date1);
                    values.put(ProjectEntry.COLUMN_PROJECT_DATE2, date2);
                    values.put(ProjectEntry.COLUMN_PROJECT_BUDGET, budget);
                    values.put(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW, crewBits);

                    // Insert a new row using the ContentResolver.
                    uri = context.getContentResolver().insert(ProjectEntry.CONTENT_URI, values);
                    ++numberOfProjects;
                }

                /*long currentTimeInMills = Calendar.getInstance().getTimeInMillis();
                Log.v (DEBUG_TAG, "Local DB updated at: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(currentTimeInMills));*/

                setLastUpdate(Calendar.getInstance().getTimeInMillis(), context);
                if (resetScheduler) {
                    setScheduler(context, updateFrequency);
                }

                //Log.d (DEBUG_TAG, "number of projects  = " + numberOfProjects);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v (LOG_TAG, context.getString(R.string.err_access_firebase));
                // TODO: Error message to the user
                //mCrewTextView.setText(R.string.err_access_firebase);
            }
        });

    }


    private static void setLastUpdate(Long lastUpdate, Context context){

        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Log.v (DEBUG_TAG, "Gravando: " + dateFormat.format(lastUpdate));

        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(lastUpdate.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.v (LOG_TAG, "Error writing to file " + FILENAME);
        } catch (IOException e) {
            Log.v (LOG_TAG, "Caught IOException (write): " + e.getMessage());
        }

    }

    private static long getLastUpdateOnFile(Context context) {

        byte lastUpdate[] = new byte[13];
        int result = -1;

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            result = fis.read(lastUpdate);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.v (LOG_TAG, "Error reading file " + FILENAME);
        }  catch (IOException e) {
            Log.v (LOG_TAG, "Caught IOException (read): " + e.getMessage());
        }

        if (result < 0){
            return 0L;
        }

        String strLastUpdate = new String(lastUpdate);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Log.v (DEBUG_TAG, "Local DB updated on " + dateFormat.format(Long.parseLong(strLastUpdate)));

        return (Long.parseLong(strLastUpdate));
    }

    public static long getLastUpdate(Context context) {
        LocalDb.getNextUpdate(context);
        return getLastUpdateOnFile(context);
    }

    public static long getNextUpdate(Context context) {
        long lastUpdate = getLastUpdateOnFile(context);
        long nextUpdate = lastUpdate + (long) TimeUnit.HOURS.toMillis(updateFrequency);
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Log.v (DEBUG_TAG, "Local DB next schedule update: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(nextUpdate));
        return nextUpdate;
    }

    public static void manualUpdate(Context context) {
        rebuildLocalDb(context, PPHelper.getMyLocation(context), false);
        String message = context.getString(R.string.project_list_updated) + "\n"
                        + context.getString(R.string.next_project_list_update) + "\n"
                        + new SimpleDateFormat(context.getString(R.string.date_hour_format)).format (getNextUpdate(context));
        Toast toast = Toast.makeText (context,  message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public static void setScheduler(Context context, int hours){
        // arguments for recurring job for every "hours" hours periodicity expressed as seconds
        final int periodicity = (int) TimeUnit.HOURS.toSeconds(hours);
        final int toleranceInterval = (int)TimeUnit.MINUTES.toSeconds(3);

        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job job = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(UpdateLocalDbScheduler.class)
                // uniquely identifies the job
                .setTag("update-db-tag")
                // only run on an unmetered network
                .setConstraints(
                        Constraint.ON_UNMETERED_NETWORK)
                .setTrigger(Trigger.executionWindow(periodicity, periodicity + toleranceInterval))
                // persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .build();

        int result = dispatcher.schedule(job);
        if (result != FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS) {
            Log.w (LOG_TAG, "Error scheduling locald DB update: " + result);
        }
    }

    public static int getNumberOfProjects (){
        return numberOfProjects;
    }
}

