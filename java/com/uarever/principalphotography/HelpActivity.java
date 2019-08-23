package com.uarever.principalphotography;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = HelpActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + HelpActivity.class.getSimpleName() + "):";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Faq> mFaqList = new ArrayList<Faq>();
    String mlangCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.help_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change app bar to say "Select Your Crew Positions"
        setTitle(getString(R.string.help_activity_title));

        // Entry point for the app to access the firebase database
        FirebaseDatabase mFirebaseDb;

        DatabaseReference faqDbReference;
        String faqPath = "/zFAQ/android/";
        mlangCode = Locale.getDefault().getLanguage();
        if (!mlangCode.equals("pt")) {
            mlangCode = "en-US";
        }

        // Obtain a handle to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.help_recycler_view);
        // Without this, the FAQ list (RecyclerView inside NestedScrollView) wasn't scrolling smoothly
        mRecyclerView.setNestedScrollingEnabled(false);

        // Find and set empty view on the Recycler, so that it only shows when the list has 0 items.
        final View emptyView = findViewById(R.id.help_empty_view);

        // This setting improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // Initialize Firebase
        mFirebaseDb = FirebaseDatabase.getInstance();

        faqDbReference = mFirebaseDb.getReference().child(faqPath);

        faqDbReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot faqSnapshot) {

                // Get each FAQ entry containing a Q&A pair for each language
                for (DataSnapshot positionSnapshot: faqSnapshot.getChildren()) {
                    // Get the Q&A pair for the current devices's language
                    DataSnapshot itemSnapshot = positionSnapshot.child(mlangCode);
                    String question = (String) itemSnapshot.child("Q").getValue();
                    String answer = (String) itemSnapshot.child("a").getValue();
                    mFaqList.add(new Faq (question, answer));
                }

                // Hide the empty view
                emptyView.setVisibility(View.GONE);

                // Specify an adapter
                mAdapter = new HelpRecyclerAdapter(mFaqList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Show and/or log error message - maybe along with progress info
                //mInfoTextView.setText(R.string.err_access_firebase);
            }
        });

    }

}


class Faq {
    private String  mQuestion;
    private String mAnswer;

    Faq (String q, String a) {
        mQuestion = q;
        mAnswer = a;
    }

    public String getQuestion () {
        return mQuestion;
    }

    public String getAnswer () {
        return mAnswer;
    }
}
