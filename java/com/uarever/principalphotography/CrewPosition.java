package com.uarever.principalphotography;

/**
 * Created by leobentes on 11/28/17.
 */

import android.content.Context;

import java.util.ArrayList;

public class CrewPosition {
    // Crew position code
    String mCrewPositionCode;
    // Crew position description;
    String mCrewPositionDesc;
    // Bits for the crew position as defined in {@link PPContract}
    int mCrewPositionBits;

    // Crew positions code "table"
    private final static String CREW_CODE_DIRECTOR = "DIR";
    private final static String CREW_CODE_1STAD = "1STAD";
    private final static String CREW_CODE_2NDAD = "2NDAD";
    private final static String CREW_CODE_SCRIPT = "SCRPT";
    private final static String CREW_CODE_PA = "PA";
    private final static String CREW_CODE_DP = "DP";
    private final static String CREW_CODE_CAMOP = "CAMOP";
    private final static String CREW_CODE_1STAC = "1STAC";
    private final static String CREW_CODE_2NDAC = "2NDAC";
    private final static String CREW_CODE_DIT = "DIT";
    private final static String CREW_CODE_STEADICAM = "STCAM";
    private final static String CREW_CODE_FOCUSPULLER = "FOCUS";
    private final static String CREW_CODE_GAFFER = "GAFFR";
    private final static String CREW_CODE_ELECTRIC = "ELTRC";
    private final static String CREW_CODE_KEYGRIP = "KGRIP";
    private final static String CREW_CODE_GRIP = "GRIP";
    private final static String CREW_CODE_STILLPHOTO = "STILL";
    private final static String CREW_CODE_PRODSOUNDMIXER = "SDMIX";
    private final static String CREW_CODE_BOOMOP = "BOOM";
    private final static String CREW_CODE_PRODDESIGNER = "PRDES";
    private final static String CREW_CODE_ARTDIRECTOR = "ARTDR";
    private final static String CREW_CODE_SETDESIGNER = "SETDS";
    private final static String CREW_CODE_SETDECORATOR = "DECOR";
    private final static String CREW_CODE_COSTDESIGNER = "COSTM";
    private final static String CREW_CODE_MAKEUP = "MAKUP";
    private final static String CREW_CODE_HAIR = "HAIR";
    private final static String CREW_CODE_EFXSUPER = "EFXSP";
    private final static String CREW_CODE_EFXASSIST = "EFXAS";
    private final static String CREW_CODE_STUNT = "STUNT";
    private final static String CREW_CODE_OTHER = "OTHER";

    /*
     * Crew positions constants
     * Theses value identify each position and must consider that only a single
     * bit will be "1" (all others must be 0);
     * Type: INTEGER
     */
    public final static int CREW_BITS_DIRECTOR = 1; // 00000001
    public final static int CREW_BITS_1STAD = 2;    // 00000010
    public final static int CREW_BITS_2NDAD = 4;    // 00000100
    public final static int CREW_BITS_SCRIPT = 8;   // 00001000
    public final static int CREW_BITS_PA = 16;
    public final static int CREW_BITS_DP = 32;
    public final static int CREW_BITS_CAMOP = 64;
    public final static int CREW_BITS_1STAC = 128;
    public final static int CREW_BITS_2NDAC = 256;
    public final static int CREW_BITS_DIT = 512;
    public final static int CREW_BITS_STEADICAM = 1024;
    public final static int CREW_BITS_FOCUSPULLER = 2048;
    public final static int CREW_BITS_GAFFER = 4096;
    public final static int CREW_BITS_ELECTRIC = 8192;
    public final static int CREW_BITS_KEYGRIP = 16384;
    public final static int CREW_BITS_GRIP = 32768;
    public final static int CREW_BITS_STILLPHOTO = 65536;
    public final static int CREW_BITS_PRODSOUNDMIXER = 131072;
    public final static int CREW_BITS_BOOMOP = 262144;
    public final static int CREW_BITS_PRODDESIGNER = 524288;
    public final static int CREW_BITS_ARTDIRECTOR = 1048576;
    public final static int CREW_BITS_SETDESIGNER = 2097152;
    public final static int CREW_BITS_SETDECORATOR = 4194304;
    public final static int CREW_BITS_COSTDESIGNER = 8388608;
    public final static int CREW_BITS_MAKEUP = 16777216;
    public final static int CREW_BITS_HAIR = 33554432;
    public final static int CREW_BITS_EFXSUPER = 67108864;
    public final static int CREW_BITS_EFXASSIST = 134217728;
    public final static int CREW_BITS_STUNT = 268435456;
    public final static int CREW_BITS_OTHER = 536870912;
    public final static int CREW_BITS_ALL = Integer.MAX_VALUE; // all ones

    /*
     * Create a new CrewPosition object.
     *
     * @param crewPositionBits is the id for the position as defined in {@link PPContract}
     * @param crewPositionDesc is the description of the position
     */
    public CrewPosition (String crewPositionCode, String crewPositionDesc, int crewPositionBits){
        mCrewPositionCode = crewPositionCode;
        mCrewPositionDesc = crewPositionDesc;
        mCrewPositionBits = crewPositionBits;
    }

    /*
     * Get the crew position code
     */
    public String getCrewPositionCode() {

        return mCrewPositionCode;
    }

    /*
     * Get the crew position description
     */
    public String getCrewPositionDesc() {

        return mCrewPositionDesc;
    }

    /*
     * Get the crew position ID
     */
    public int getCrewPositionId () {

        return mCrewPositionBits;
    }

    /*
     * Get the crew position description based on the crew position code
     */
    public static String getDescriptionByCode (Context context, String crewCode) {
        // Get all positions and loop through it
        ArrayList<CrewPosition> allCrewPositions = getAllPositions(context);
        for (int i = 0; i < allCrewPositions.size(); i++) {
            if (allCrewPositions.get(i).getCrewPositionCode().equals(crewCode)){
                return allCrewPositions.get(i).getCrewPositionDesc();
            }
        }
        return null;
    }

    /*
    * Get the crew position bits based on the crew position code
    */
    public static int getBitsByCode (Context context, String crewCode) {
        // Get all positions and loop through it
        ArrayList<CrewPosition> allCrewPositions = getAllPositions(context);
        for (int i = 0; i < allCrewPositions.size(); i++) {
            if (allCrewPositions.get(i).getCrewPositionCode().equals(crewCode)){
                return allCrewPositions.get(i).getCrewPositionId();
            }
        }
        return 0;
    }

    /*
    * Method to return an array list with all crew positions available in app.
    * This method makes maintenance easier, being one place for include or exclude positions
    * in the app.
    *
    * @param context - necessary for getting the description
    * @return the arraylist with CrewPositions objects, composed by crew positions integer
    * constants and its descriptions
    */
    // TODO: Make this method private and refactor the code to access this information through other methods of this class
    public static ArrayList<CrewPosition> getAllPositions(Context context) {
        // List for all positions in the app
        ArrayList<CrewPosition> allPositions = new ArrayList<CrewPosition>();

        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_DIRECTOR,
                context.getString(R.string.crew_director), CREW_BITS_DIRECTOR));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_1STAD,
                context.getString(R.string.crew_ad_1st), CREW_BITS_1STAD));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_2NDAD,
                context.getString(R.string.crew_ad_2nd), CREW_BITS_2NDAD));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_SCRIPT,
                context.getString(R.string.crew_script), CREW_BITS_SCRIPT));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_PA,
                context.getString(R.string.crew_pa), CREW_BITS_PA));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_DP,
                context.getString(R.string.crew_dp), CREW_BITS_DP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_CAMOP,
                context.getString(R.string.crew_cam_op), CREW_BITS_CAMOP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_1STAC,
                context.getString(R.string.crew_ac_1st), CREW_BITS_1STAC));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_2NDAC,
                context.getString(R.string.crew_ac_2nd), CREW_BITS_2NDAC));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_DIT,
                context.getString(R.string.crew_dit), CREW_BITS_DIT));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_STEADICAM,
                context.getString(R.string.crew_steadicam), CREW_BITS_STEADICAM));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_GAFFER,
                context.getString(R.string.crew_gaffer), CREW_BITS_GAFFER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_FOCUSPULLER,
                context.getString(R.string.crew_focus_puller), CREW_BITS_FOCUSPULLER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_ELECTRIC,
                context.getString(R.string.crew_electric), CREW_BITS_ELECTRIC));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_KEYGRIP,
                context.getString(R.string.crew_key_grip), CREW_BITS_KEYGRIP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_GRIP,
                context.getString(R.string.crew_grip), CREW_BITS_GRIP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_STILLPHOTO,
                context.getString(R.string.crew_still_photo), CREW_BITS_STILLPHOTO));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_PRODSOUNDMIXER,
                context.getString(R.string.crew_sound), CREW_BITS_PRODSOUNDMIXER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_BOOMOP,
                context.getString(R.string.crew_boom), CREW_BITS_BOOMOP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_PRODDESIGNER,
                context.getString(R.string.crew_prod_designer), CREW_BITS_PRODDESIGNER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_ARTDIRECTOR,
                context.getString(R.string.crew_art_director), CREW_BITS_ARTDIRECTOR));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_SETDESIGNER,
                context.getString(R.string.crew_set_designer), CREW_BITS_SETDESIGNER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_SETDECORATOR,
                context.getString(R.string.crew_set_decor), CREW_BITS_SETDECORATOR));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_COSTDESIGNER,
                context.getString(R.string.crew_costume), CREW_BITS_COSTDESIGNER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_MAKEUP,
                context.getString(R.string.crew_makeup), CREW_BITS_MAKEUP));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_HAIR,
                context.getString(R.string.crew_hair), CREW_BITS_HAIR));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_EFXSUPER,
                context.getString(R.string.crew_efx_super), CREW_BITS_EFXSUPER));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_EFXASSIST,
                context.getString(R.string.crew_efx_assist), CREW_BITS_EFXASSIST));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_STUNT,
                context.getString(R.string.crew_stunt), CREW_BITS_STUNT));
        allPositions.add(new CrewPosition(CrewPosition.CREW_CODE_OTHER,
                context.getString(R.string.crew_other), CREW_BITS_OTHER));

        return allPositions;
    }

}
