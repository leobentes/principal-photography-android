package com.uarever.principalphotography.util;

/**
 * Created by leobentes on 11/25/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.uarever.principalphotography.CrewPosition;
import com.uarever.principalphotography.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

/*
 * Helper methods to be used throughout the app
 */
public class PPHelper {

    /*
     * @param context - this is necessary because getSharedPreferences needs a context
     * Return an ArrayList with crew positions currently selected by the user
     */
    public static ArrayList<Integer> getMyPositions(Context context) {
        // Get crew positions stored in sharedPreferences
        SharedPreferences preferences =
                context.getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);
        int userPositions = preferences.getInt(MainActivity.PREF_POSITIONS, 0);

        // ArrayList that with the positions to be returned by the this method
        ArrayList<Integer> positions = new ArrayList<Integer>();

        int currentPosition;

        // Get all positions in the app and loop through it to find which ones has its bit set
        // in the positions stored in sharedPreferences
        ArrayList<CrewPosition> allCrewPositions = CrewPosition.getAllPositions(context);
        for (int i = 0; i < allCrewPositions.size(); i++) {
            // Get current position ID
            currentPosition = allCrewPositions.get(i).getCrewPositionId();

            // If a bitwise "&" between the current position from all positions
            // and the positions in the project (read from sharedPreferences) eguals
            // the current position itself it means that such positional bit is on
            if ((currentPosition & userPositions) == currentPosition) {
                // Get current position ID
                positions.add(allCrewPositions.get(i).getCrewPositionId());
            }
        }

        return positions;
    }

    /*
     * @param context - To pass to getMyPositions()
     */
    public static int getMyPositionsBitsMap(Context context) {
        ArrayList<Integer> positions = getMyPositions(context);
        // "Insert" all user positiosn into one single integer variable
        int myPositionsBitsMap = 0;
        for (int i = 0; i < positions.size(); i++){
            myPositionsBitsMap += positions.get(i);
        }
        return myPositionsBitsMap;
    }

    /*
     * Return the dates currently selected by the user
     * @param dateType  - being 1 for initial date and 2 for end date
     * @param context - this is necessary because getSharedPreferences needs a context
     * @return The date retrieved from sharedPreferences
     */
    public static long getMyDate(int dateType, Context context) {
        String dateKey;
        SharedPreferences preferences =
                context.getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);
        switch (dateType) {
            case 1: // Initial date
                dateKey = MainActivity.PREF_DATE1;
                break;
            case 2:
                dateKey = MainActivity.PREF_DATE2;
                break;
            default:
                throw new IllegalArgumentException("Invalid argument passed to getMyDate");
        }
        return preferences.getLong(dateKey, System.currentTimeMillis());
    }

    /*
    * Return the location currently selected by the user
    * @param context - this is necessary because getSharedPreferences needs a context
    * @return The location retrieved from sharedPreferences
    */
    public static String getMyLocation(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(MainActivity.PREFS_FILE, Context.MODE_PRIVATE);
        return preferences.getString(MainActivity.PREF_LOCATION, null);
    }

    /*
     * Helper method to return an array list with crew positions descriptions
     *
     * @param crew - integer representing the bits relative to crew positions
     * @param context - necessary for getting the description
     * @return the arraylist with crew positions descriptions
     */
    // TODO: Move this method into CrewPosition class? Or it should be here because of positions parameter?
    public static ArrayList<String> getPositionsDescList(int projectPositions, Context context) {

        ArrayList<String> projectPositionsDesc = new ArrayList<String>();
        int currentPosition;

        // Get all positions and loop through it
        ArrayList<CrewPosition> allCrewPositions = CrewPosition.getAllPositions(context);
        for (int i = 0; i < allCrewPositions.size(); i++) {
            // Get current position ID
            currentPosition = allCrewPositions.get(i).getCrewPositionId();

            // If a bitwise "&" between the current position from all positions
            // and the positions in the project (received as a parameter) equals
            // the current position itself it means that such positional bit is on
            if ((currentPosition & projectPositions) == currentPosition) {
                // Get current position description
                projectPositionsDesc.add(allCrewPositions.get(i).getCrewPositionDesc());
            }
        }
        return projectPositionsDesc;
    }

    /*
     * Helper method to return an array list with crew positions codes
     *
     * @param crew - integer representing the bits relative to crew positions
     * @param context - necessary for getting the code
     * @return the arraylist with crew positions codes
     */
    // TODO: Move this method into CrewPosition class? Or it should be here because of positions parameter?
    public static ArrayList<String> getPositionsCodeList(int projectPositions, Context context) {

        ArrayList<String> projectPositionsCode = new ArrayList<String>();
        int currentPosition;

        // Get all positions and loop through it
        ArrayList<CrewPosition> allCrewPositions = CrewPosition.getAllPositions(context);
        for (int i = 0; i < allCrewPositions.size(); i++) {
            // Get current position ID
            currentPosition = allCrewPositions.get(i).getCrewPositionId();

            // If a bitwise "&" between the current position from all positions
            // and the positions in the project (received as a parameter) equals
            // the current position itself it means that such positional bit is on
            if ((currentPosition & projectPositions) == currentPosition) {
                // Get current position description
                projectPositionsCode.add(allCrewPositions.get(i).getCrewPositionCode());
            }
        }
        return projectPositionsCode;
    }

    /**
     * Helper method to set an epoch based date to that day at midnight
     *
     * @param date The long epoch date to be modified to represent that date at midnight
     * @return The new date at midnight in milliseconds
     */
    public static long setDateToMidnight(long date) {
        // Create a calendar object
        Calendar calendar = Calendar.getInstance();
        // Set calendar to the date and time received
        calendar.setTimeInMillis(date);
        // set calendar to midnight at the date received
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // Return modified date and time in milliseconds
        return (calendar.getTimeInMillis());
    }

}


