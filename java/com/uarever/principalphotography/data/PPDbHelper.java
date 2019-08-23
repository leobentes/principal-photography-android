package com.uarever.principalphotography.data;

/**
 * Created by leobentes on 11/22/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uarever.principalphotography.data.PPContract.ProjectEntry;

/**
 * Database helper for Principal Photography app.
 * Database creation and version management.
 */
public class PPDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = PPDbHelper.class.getSimpleName();

    // Mandatory constants for database name and database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pp.db";

    /**
     * Constructs a new instance of {@link PPDbHelper}.
     *
     * @param context of the app
     */
    public PPDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PROJECTS_TABLE =
                "CREATE TABLE " + ProjectEntry.TABLE_NAME + " ("
                        + ProjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ProjectEntry.COLUMN_PROJECT_PUSH_ID + " TEXT NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_TITLE + " TEXT NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_DATE1 + " INTEGER NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_DATE2 + " INTEGER NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_PRODUCER + " TEXT NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_PRODUCER_PHONE + " TEXT NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_PRODUCER_EMAIL + " TEXT NOT NULL, "
                        + ProjectEntry.COLUMN_PROJECT_BUDGET + " INTEGER, "
                        + ProjectEntry.COLUMN_PROJECT_NEEDED_CREW + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PROJECTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
