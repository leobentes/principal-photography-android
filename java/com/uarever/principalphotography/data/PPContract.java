package com.uarever.principalphotography.data;

/**
 * Created by leobentes on 11/22/17.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Principal Photography app.
 */
public final class PPContract {
    // Private constructor to prevent someone from accidentally instantiating
    // the PPContract class
    private PPContract() {
    }

    /**
     * Content authority to be used in URIs
     */
    public static final String CONTENT_AUTHORITY = "com.uarever.principalphotography";

    /**
     * Base all content provider URIs
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths to be used, like content://com.example.android.principalphotography/projects/
     */
    public static final String PATH_PROJECTS = "projects";

    /* Inner class defining "projects" table */
    public static class ProjectEntry implements BaseColumns {

        /**
         * Content URI to access projects data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PROJECTS);

        /**
         * MIME type of the {@link #CONTENT_URI} for a list of projects
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECTS;

        /**
         * MIME type of the {@link #CONTENT_URI} for a single project
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECTS;

        /**
         * Name of database table for projects
         */
        public final static String TABLE_NAME = "projects";

        /**
         * Unique ID number for the project inside the DB
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Project ID, generated by firebase
         * Type: TEXT
         */
        public final static String COLUMN_PROJECT_PUSH_ID = "pushid";

        /**
         * Title of the project
         * Type: TEXT
         */
        public final static String COLUMN_PROJECT_TITLE = "title";

        /**
         * Start date for principal photography
         * Type: LONG
         */
        public final static String COLUMN_PROJECT_DATE1 = "date1";

        /**
         * End date for principal photography
         * Type: LONG
         */
        public final static String COLUMN_PROJECT_DATE2 = "date2";

        /**
         * Filmmaker (usually the producer) in charge of the project
         * Type: TEXT
         */
        public final static String COLUMN_PROJECT_PRODUCER = "producer";

        /**
         * Producer's phone number
         * Type: TEXT
         */
        public final static String COLUMN_PROJECT_PRODUCER_PHONE = "prodPhone";

        /**
         * Producer's email address
         * Type: TEXT
         */
        public final static String COLUMN_PROJECT_PRODUCER_EMAIL = "prodEmail";

        /**
         * Positions need for the project
         * This value will be operate on as its a sequence of 0's and 1's
         * Type: INTEGER
         */
        public final static String COLUMN_PROJECT_NEEDED_CREW = "neededcrew";

        /**
         * Project's budget
         * Type: INTEGER
         */
        public final static String COLUMN_PROJECT_BUDGET = "budget";

    }
}
