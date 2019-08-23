package com.uarever.principalphotography;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import com.uarever.principalphotography.data.PPContract.ProjectEntry;
import com.uarever.principalphotography.util.PPHelper;


/**
 * Created by leobentes on 11/23/17.
 */

public class ProjectCursorAdapter extends CursorAdapter {

    public static final String LOG_TAG = ProjectCursorAdapter.class.getSimpleName();

    /**
     * Constructs a new {@link ProjectCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProjectCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Return the list item view
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvTitle = (TextView) view.findViewById(R.id.project_title);
        TextView tvProducer = (TextView) view.findViewById(R.id.project_producer);
        TextView tvDates = (TextView) view.findViewById(R.id.project_dates);
        TextView tvPositions = (TextView) view.findViewById(R.id.project_positions);

        // Figure out the index of each column
        int titleColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_TITLE);
        int producerColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_PRODUCER);
        int date1ColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_DATE1);
        int date2ColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_DATE2);
        int neededCrewColumnIndex = cursor.getColumnIndex(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW);


        // Extract properties from cursor
        String currentTitle = cursor.getString(titleColumnIndex);
        String currentProducer = cursor.getString(producerColumnIndex);
        long currentDate1 = cursor.getLong(date1ColumnIndex);
        long currentDate2 = cursor.getLong(date2ColumnIndex);
        int neededCrew = cursor.getInt(neededCrewColumnIndex);

        SimpleDateFormat sdf1 = new SimpleDateFormat(context.getString(R.string.date_format));
        SimpleDateFormat sdf2 = new SimpleDateFormat(context.getString(R.string.date_format));

        String currentDate = sdf1.format(currentDate1) + " " + context.getString(R.string.to) +
                " " + sdf2.format(currentDate2);

        // Populate fields with extracted properties
        currentTitle ="(" + (cursor.getPosition()+1) + ") " + currentTitle;
        tvTitle.setText(currentTitle);
        tvProducer.setText(context.getString(R.string.crew_producer) + ": " + currentProducer);
        tvDates.setText(currentDate);
        //tvDays.setText(currentDays);

        // Get positions chosen by the user
        int myPositions =  PPHelper.getMyPositionsBitsMap(context);

        // Make a "boolean sum" (bitwise operation) to create a variable with only user positions
        // that are needed in the project
        int myNeededPositions = neededCrew & myPositions;

        String neededCrewDesc;
        if (myNeededPositions != 0){
            // Construct needed crew string based on what bits are "on" (that is, set to 1)
            // in the neededCrew integer variable
            neededCrewDesc = context.getString(R.string.looking_for);

            // Get the descriptions of the needed crew positions
            ArrayList<String> projectPositionsDesc = PPHelper.getPositionsDescList(myNeededPositions, context);

            // Build the list to be inserted in the TextView
            for (int i = 0; i < projectPositionsDesc.size(); i++) {
                    neededCrewDesc += "\n";
                neededCrewDesc += projectPositionsDesc.get(i);
            }
            tvPositions.setVisibility(View.VISIBLE);
            tvPositions.setText(neededCrewDesc);
        } else {
            neededCrewDesc = "";
            tvPositions.setVisibility(View.GONE);
        }
    }
}
