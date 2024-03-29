package com.uarever.principalphotography.data;

/**
 * Created by leobentes on 11/22/17.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.uarever.principalphotography.R;

import com.uarever.principalphotography.data.PPContract.ProjectEntry;

/**
 * {@link ContentProvider} for Principal Photography app.
 */

public class PPProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PPProvider.class.getSimpleName();

    /** Database helper providing access to the database */
    private PPDbHelper mDbHelper;

    /** URI matcher code for the content URI for the productions table */
    private static final int PRODUCTIONS = 10;

    /** URI matcher code for the content URI for a single production */
    private static final int PRODUCTION_ID = 20;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
      */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(PPContract.CONTENT_AUTHORITY, PPContract.PATH_PROJECTS, PRODUCTIONS);
        sUriMatcher.addURI(PPContract.CONTENT_AUTHORITY, PPContract.PATH_PROJECTS + "/#", PRODUCTION_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new PPDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTIONS:
                // For the PRODUCTIONS code, query the projects table directly with the given
                // projection, selection, selection arguments, and sort order.
                cursor = database.query(ProjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCTION_ID:
                // Get the ID from the URI.
                selection = ProjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ProjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTIONS:
                return insertProduction(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a project into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertProduction(Uri uri, ContentValues values) {

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_TITLE)){
            String title = values.getAsString(ProjectEntry.COLUMN_PROJECT_TITLE);
            if (title == null|| title.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_production_title));
            }
        }

        Long date1 = 0L;
        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_DATE1)){
            date1 = values.getAsLong(ProjectEntry.COLUMN_PROJECT_DATE1);
            if (date1 == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_starting_date));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_DATE2)){
            Long date2 = values.getAsLong(ProjectEntry.COLUMN_PROJECT_DATE2);
            if (date2 == null || date2 < date1) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_ending_date));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_PRODUCER)){
            String producer = values.getAsString(ProjectEntry.COLUMN_PROJECT_PRODUCER);
            if (producer == null|| producer.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_producer_name));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL)){
            String prodEmail = values.getAsString(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL);
            if (prodEmail == null|| prodEmail.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_producer_email));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_BUDGET)){
            int budget = values.getAsInteger(ProjectEntry.COLUMN_PROJECT_BUDGET);
            if (budget < 0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_budget));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW)){
            int neededCrew = values.getAsInteger(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW);
            if (neededCrew < 0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_needed_crew));
            }
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ProjectEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTIONS:
                return updateProduction (uri, contentValues, selection, selectionArgs);
            case PRODUCTION_ID:
                selection = ProjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduction(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update productions in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments.
     * Return the number of rows that were successfully updated.
     */
    private int updateProduction (Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_TITLE)){
            String title = values.getAsString(ProjectEntry.COLUMN_PROJECT_TITLE);
            if (title == null|| title.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_production_title));
            }
        }

        Long date1 = 0L;
        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_DATE1)){
            date1 = values.getAsLong(ProjectEntry.COLUMN_PROJECT_DATE1);
            if (date1 == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_starting_date));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_DATE2)){
            Long date2 = values.getAsLong(ProjectEntry.COLUMN_PROJECT_DATE2);
            if (date2 == null || date2 < date1) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_ending_date));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_PRODUCER)){
            String producer = values.getAsString(ProjectEntry.COLUMN_PROJECT_PRODUCER);
            if (producer == null|| producer.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_producer_name));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE)){
            String prodPhone = values.getAsString(ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE);
            if (prodPhone == null|| prodPhone.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_producer_phone));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL)){
            String prodEmail = values.getAsString(ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL);
            if (prodEmail == null|| prodEmail.length()==0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_missing_producer_email));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_BUDGET)){
            int budget = values.getAsInteger(ProjectEntry.COLUMN_PROJECT_BUDGET);
            if (budget < 0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_budget));
            }
        }

        if (values.containsKey(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW)){
            int neededCrew = values.getAsInteger(ProjectEntry.COLUMN_PROJECT_NEEDED_CREW);
            if (neededCrew < 0) {
                throw new IllegalArgumentException(getContext().getString(R.string.provider_invalid_needed_crew));
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ProjectEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTIONS:
                rowsDeleted = database.delete(ProjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTION_ID:
                selection = ProjectEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTIONS:
                return PPContract.ProjectEntry.CONTENT_LIST_TYPE;
            case PRODUCTION_ID:
                return PPContract.ProjectEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
