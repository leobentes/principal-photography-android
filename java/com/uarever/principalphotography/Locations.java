package com.uarever.principalphotography;

/**
 * Created by leobentes on 01/29/18.
 */

import android.content.Context;

import java.util.ArrayList;

public class Locations {
    String mLocationCode;
    String mLocationName;

    // Locations codes "table"
    private final static String LOCATION_CODE_BSB = "BSB";
    private final static String LOCATION_CODE_RJO = "RJO";
    private final static String LOCATION_CODE_SPO = "SPO";
    private final static String LOCATION_CODE_ATL = "ATL";
    private final static String LOCATION_CODE_LAX = "LAX";
    private final static String LOCATION_CODE_NYC = "NYC";


    /*
     * Create a new Location object.
     *
     */
    // TODO: Rethink this class about using attibutes instead of getters (i.e object.code instead of getCrewPositionCode())
    public Locations(String locationCode, String locationName){
        mLocationCode = locationCode;
        mLocationName = locationName;
    }

    /*
     * Get the location code
     */
    public String getLocationCode() {

        return mLocationCode;
    }

    /*
     * Get the location name
     */
    public String getLocationName() {

        return mLocationName;
    }


    /*
     * Get the location name based on the location code
     */
    public static String getLocationNameByCode (Context context, String locationCode) {
        // Get all positions and loop through it
        ArrayList<Locations> allLocations = getAllLocations(context);
        for (int i = 0; i < allLocations.size(); i++) {
            if (allLocations.get(i).getLocationCode().equals(locationCode)){
                return allLocations.get(i).getLocationName();
            }
        }
        return null;
    }

    /*
    * Get the location based on the location name
    */
    public static String getLocationCodeByName (Context context, String locationName) {
        // Get all positions and loop through it
        ArrayList<Locations> allLocations = getAllLocations(context);
        for (int i = 0; i < allLocations.size(); i++) {
            if (allLocations.get(i).getLocationName().equals(locationName)){
                return allLocations.get(i).getLocationCode();
            }
        }
        return null;
    }

    /*
    * Get the array index based on the location code
    */
    public static int getIndexByCode (Context context, String locationCode) {
        // Get all positions and loop through it
        ArrayList<Locations> allLocations = getAllLocations(context);
        for (int i = 0; i < allLocations.size(); i++) {
            if (allLocations.get(i).getLocationCode().equals(locationCode)){
                return i;
            }
        }
        return 0;
    }

    /*
    * Method to return an array list with all locations available in app.
    * This method makes maintenance easier, being one place for include or exclude positions
    * in the app.
    *
    * @param context - necessary for getting the description
    * @return the arraylist with Locations objects, composed by crew positions integer
    * constants and its descriptions
    */
    public static ArrayList<Locations> getAllLocations(Context context) {
        // List for all locations in the app
        ArrayList<Locations> allLocations = new ArrayList<Locations>();

        allLocations.add(new Locations(Locations.LOCATION_CODE_BSB, context.getString(R.string.location_BR_brasilia)));
        allLocations.add(new Locations(Locations.LOCATION_CODE_RJO, context.getString(R.string.location_BR_rio)));
        allLocations.add(new Locations(Locations.LOCATION_CODE_SPO, context.getString(R.string.location_BR_sao_paulo)));
        allLocations.add(new Locations(Locations.LOCATION_CODE_ATL, context.getString(R.string.location_US_atlanta)));
        allLocations.add(new Locations(Locations.LOCATION_CODE_LAX, context.getString(R.string.location_US_los_angeles)));
        allLocations.add(new Locations(Locations.LOCATION_CODE_NYC, context.getString(R.string.location_US_new_york)));

        return allLocations;
    }

}
