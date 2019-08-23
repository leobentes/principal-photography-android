package com.uarever.principalphotography;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uarever.principalphotography.data.PPContract.ProjectEntry;
import com.uarever.principalphotography.util.PPHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.text.NumberFormat;


/**
 * Created by leobentes on 11/28/17.
 */
/*
 * Show project details for a specific project
 */
public class ProjectDetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ProjectDetailsActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + ProjectDetailsActivity.class.getSimpleName() + "):";

    // TODO: make this constant global
    /* Constant for the otherInfo "table" */
    private static final String OTHERINFO = "otherInfo";

    // TODO: make this constant global
    /* Constant for the neededCrew "table" */
    private static final String NEEDEDCREW = "neededCrew";

    /**
     * Content URI for the chosen project
     */
    private Uri mChoosenProjectUri;

    /**
     * TextViews to be filled
     */
    private TextView mTittleTextView;
    private TextView mDatesTextView;
    private TextView mDaysTextView;
    private TextView mInfoTextView;
    private TextView mProducerTextView;
    private TextView mBudgetTextView;
    private TextView mCityTextView;
    private TextView mCrewTextView;
    private TextView mContactsNameTextView;
    private TextView mContactsPhoneTextView;
    private TextView mContactsEmailTextView;
    private LinearLayout mCrewLayout;

    /**
     * Constants IDs for TextViews created programmatically to show needed crew info
     */
    private static final int TVCREWPOSITION = 1001;
    private static final int TVCREWQUANTITY = 1002;
    private static final int TVCREWDAYS = 1003;
    private static final int TVCREWRATE = 1004;

    // TODO: it may change
    private String mCity;
    private String mNeededCrewPath;
    private String mOtherInfoPath;


    /**
     * Identifier for the project data loader
     */
    private static final int PROJECT_DETAILS_LOADER = 10;

    // Entry point for the app to access the firebase database
    private FirebaseDatabase mFirebaseDb;

    // The DatabaseReference is a class to reference a specific part of the database
    // In this case, the otherInfo and neededCrew portions of the database
    private DatabaseReference mOtherInfoDbReference;
    private DatabaseReference mNeededCrewDbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.project_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recover the intent that was used to launch this activity
        // in order to get the URI for the chosen project
        Intent intent = getIntent();
        mChoosenProjectUri = intent.getData();

        // Find all relevant views that we will filled with information
        mTittleTextView = (TextView) findViewById(R.id.details_tittle);
        mDatesTextView = (TextView) findViewById(R.id.details_dates);
        mDaysTextView = (TextView) findViewById(R.id.details_days);
        mInfoTextView = (TextView) findViewById(R.id.details_other_info);
        mProducerTextView = (TextView) findViewById(R.id.details_producer);
        mBudgetTextView = (TextView) findViewById(R.id.details_budget);
        mCityTextView = (TextView) findViewById(R.id.details_city);
        mCrewTextView = (TextView) findViewById(R.id.details_crew_list);
        mContactsNameTextView = (TextView) findViewById(R.id.details_contacts_name);
        mContactsPhoneTextView = (TextView) findViewById(R.id.details_contacts_phone);
        mContactsEmailTextView = (TextView) findViewById(R.id.details_contacts_email);
        mCrewLayout = (LinearLayout) findViewById(R.id.details_crew_layout);

        // TODO: get it through a function
        mCity = PPHelper.getMyLocation(this);
        mNeededCrewPath = mCity + "/" + NEEDEDCREW;
        mOtherInfoPath = mCity + "/" + OTHERINFO;

        // Change app bar to say "Project Details"
        setTitle(getString(R.string.details_activity_title));
        // Kick off the loader.
        getLoaderManager().initLoader(PROJECT_DETAILS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        // List of columns that need to be returned to fill information about
        // project details
        String[] projection = {
                ProjectEntry._ID,
                ProjectEntry.COLUMN_PROJECT_PUSH_ID,
                ProjectEntry.COLUMN_PROJECT_TITLE,
                ProjectEntry.COLUMN_PROJECT_PRODUCER,
                ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE,
                ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL,
                ProjectEntry.COLUMN_PROJECT_DATE1,
                ProjectEntry.COLUMN_PROJECT_DATE2,
                ProjectEntry.COLUMN_PROJECT_BUDGET,
                ProjectEntry.COLUMN_PROJECT_NEEDED_CREW};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mChoosenProjectUri,     // Query the content URI for the current project
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Return early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        final Context context = getApplicationContext();

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of projects attributes that we're interested in
            int pushIdColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_PUSH_ID);
            int tittleColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_TITLE);
            int producerColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_PRODUCER);
            int prodPhoneColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE);
            int prodEmailColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL);
            int date1ColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_DATE1);
            int date2ColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_DATE2);
            int budgetColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_BUDGET);
            int crewColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW);

            // Retrieve tittle, producer's info and budget
            String title = cursor.getString(tittleColumnIndex);
            String producer = cursor.getString(producerColumnIndex);
            String prodPhone = cursor.getString(prodPhoneColumnIndex);
            String prodEmail = cursor.getString(prodEmailColumnIndex);
            long budget = cursor.getLong(budgetColumnIndex);

            // Retrieve and format dates
            long date1 = cursor.getLong(date1ColumnIndex);
            long date2 = cursor.getLong(date2ColumnIndex);
            SimpleDateFormat sdf1 = new SimpleDateFormat(getString(R.string.date_format));
            SimpleDateFormat sdf2 = new SimpleDateFormat(getString(R.string.date_format));
            String currentDates = sdf1.format(date1) + " " + getString(R.string.to) + " " + sdf2.format(date2);
            String currentDays = "(" + (((date2 - date1) / 86400000) + 1) + " " + getString(R.string.days) + ")";

            // Set currency format
            final NumberFormat currency = NumberFormat.getCurrencyInstance();

            // Update the views on the screen with the values from the database
            mTittleTextView.setText(title);
            mProducerTextView.setText(getString(R.string.crew_producer) + ": " + producer);
            mDatesTextView.setText(currentDates);
            mDaysTextView.setText(currentDays);
            mBudgetTextView.setText(getString(R.string.text_budget) + ": " + currency.format(budget));
            mCityTextView.setText(getString(R.string.text_city) + ": " +
                    Locations.getLocationNameByCode(this, PPHelper.getMyLocation(this)));

            String pushId = cursor.getString(pushIdColumnIndex);

            // Initialize Firebase
            mFirebaseDb = FirebaseDatabase.getInstance();

            // Get the reference for "neededCrew/push_id" child
            mNeededCrewPath = mNeededCrewPath + "/" + pushId;
            mNeededCrewDbReference = mFirebaseDb.getReference().child(mNeededCrewPath);

            mNeededCrewDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot neededCrewSnapshot) {

                    // String crewList = "";
                    LinearLayout.LayoutParams positionParams = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 2);
                    LinearLayout.LayoutParams numbersParams = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1);

                    // Get padding, text size and text color values
                    int padding = (int) getResources().getDimension(R.dimen.card_padding);
                    int textSize = (int) getResources().getDimension(R.dimen.card_text);
                    int textColor = (int) getResources().getColor(R.color.cardPrimaryTextColor);

                    // Get positions chosen by the user
                    int myPositions =  PPHelper.getMyPositionsBitsMap(context);

                    // Hidden loading message
                    mCrewTextView.setVisibility(View.GONE);

                    for (DataSnapshot positionSnapshot: neededCrewSnapshot.getChildren()) {
                        String positionCode = positionSnapshot.getKey();
                        String positionDesc = CrewPosition.getDescriptionByCode(context, positionCode);
                        Long quantity = (Long) positionSnapshot.child("qty").getValue();
                        Long days = (Long) positionSnapshot.child("days").getValue();
                        Long rate = (Long) positionSnapshot.child("rate").getValue();
                        // crewList += positionDesc + "\n" + quantity + "x, " + days + " " + getString(R.string.days) + ", " + currency.format(rate) + "\n\n" ;

                        // Test if current crew position is one set by the user and sets the font style
                        int currentPositionBits = CrewPosition.getBitsByCode (context, positionCode);
                        int fontStyle = ((currentPositionBits & myPositions) == 0) ? Typeface.NORMAL : Typeface.BOLD;

                        // Create a new RelativeLayout to group the TextViews
                        RelativeLayout relativeLayout = new RelativeLayout(context);

                        // Define RelativeLayout parameters
                        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);

                        // Create TextViews for each value
                        TextView crewPositionTextView = new TextView(context);
                        crewPositionTextView.setId (TVCREWPOSITION);
                        crewPositionTextView.setPadding(padding, padding, 0, 0);
                        crewPositionTextView.setText(positionDesc);
                        crewPositionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        crewPositionTextView.setTextColor(textColor);
                        crewPositionTextView.setTypeface(null, fontStyle);

                        TextView crewQuantityTextView = new TextView(context);
                        crewQuantityTextView.setId (TVCREWQUANTITY);
                        crewQuantityTextView.setPadding(padding, padding, 0, 0);
                        if (quantity > 1) {
                            crewQuantityTextView.setText("(" + String.format("%d", quantity) + ")");
                            crewQuantityTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                            crewQuantityTextView.setTextColor(textColor);
                            crewQuantityTextView.setTypeface(null, fontStyle);
                        }

                        TextView crewDaysTextView = new TextView(context);
                        crewDaysTextView.setId (TVCREWDAYS);
                        crewDaysTextView.setPadding(padding, 0, 0, 0);
                        crewDaysTextView.setText(String.format("%d", days) + " " + getString(R.string.days));
                        crewDaysTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        crewDaysTextView.setTextColor(textColor);
                        crewDaysTextView.setTypeface(null, fontStyle);

                        TextView crewRateTextView = new TextView(context);
                        crewRateTextView.setId (TVCREWRATE);
                        crewRateTextView.setPadding(padding, 0, 0, 0);
                        crewRateTextView.setText(currency.format(rate) + " " + getString(R.string.per_day));
                        crewRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        crewRateTextView.setTextColor(textColor);
                        crewRateTextView.setTypeface(null, fontStyle);

                        // Defining the layout parameters of the crew position TextView
                        RelativeLayout.LayoutParams cptvParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        // Setting the parameters on the TextView for crew position
                        crewPositionTextView.setLayoutParams(cptvParams);

                        // Defining the layout parameters of the crew quantity TextView
                        RelativeLayout.LayoutParams cqtvParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        crewQuantityTextView.setLayoutParams(cqtvParams);
                        cqtvParams.addRule(RelativeLayout.RIGHT_OF, TVCREWPOSITION);

                        // Defining the layout parameters of the crew days TextView
                        RelativeLayout.LayoutParams cdtvParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        crewDaysTextView.setLayoutParams(cdtvParams);
                        cdtvParams.addRule(RelativeLayout.BELOW, TVCREWPOSITION);

                        // Defining the layout parameters of the crew rate TextView
                        RelativeLayout.LayoutParams crtvParams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                        crewRateTextView.setLayoutParams(crtvParams);
                        crtvParams.addRule(RelativeLayout.BELOW, TVCREWPOSITION);
                        crtvParams.addRule(RelativeLayout.RIGHT_OF, TVCREWDAYS);


                        // Adding TextViews to the RelativeLayout as a child
                        relativeLayout.addView(crewPositionTextView);
                        relativeLayout.addView(crewQuantityTextView);
                        relativeLayout.addView(crewDaysTextView);
                        relativeLayout.addView(crewRateTextView);

                        mCrewLayout.addView(relativeLayout);
                      }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mCrewTextView.setText(R.string.err_access_firebase);
                }
            });

            // Retrieve the details from firebase
            String currentOtherInfo = cursor.getString(pushIdColumnIndex);
            if (currentOtherInfo != null){
                // Get the reference for "otherInfo/push_id" child
                mOtherInfoPath = mOtherInfoPath + "/" + pushId;
                mOtherInfoDbReference = mFirebaseDb.getReference().child(mOtherInfoPath);

                mOtherInfoDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Marshall the data contained in the snapshot into OtherInfo class.
                        OtherInfo otherInfo = dataSnapshot.getValue(OtherInfo.class);
                        // Replace "|" for newline and update the view
                        // mInfoTextView.setText(dataSnapshot.getValue().toString().replaceAll("\\|", "\n"));
                        if (otherInfo != null){
                            mInfoTextView.setText(otherInfo.getInfo().replaceAll("\\|", "\n"));
                        } else {
                            mInfoTextView.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mInfoTextView.setText(R.string.err_access_firebase);
                    }
                });
            }


            // Update contact information
            mContactsNameTextView.setText(producer + " (" + getString(R.string.crew_producer) + ")");
            mContactsPhoneTextView.setText(prodPhone);
            mContactsEmailTextView.setText(prodEmail + "\n");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
