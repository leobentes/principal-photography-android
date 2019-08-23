package com.uarever.principalphotography;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.uarever.principalphotography.util.PPHelper;

import java.util.ArrayList;


/**
 * Created by leobentes on 11/29/17.
 */

/*
 * Allow selection of crew positions
 */
public class SetCrewActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SetCrewActivity.class.getSimpleName();
    public static final String DEBUG_TAG =
            "PPDebug (" + SetCrewActivity.class.getSimpleName() + "):";

    // Define all checkboxes
    CheckBox directorCheckBox;
    CheckBox ad1stCheckBox;
    CheckBox ad2ndCheckBox;
    CheckBox scriptCheckBox;
    CheckBox dpCheckBox;
    CheckBox ac1stCheckBox;
    CheckBox ac2ndCheckBox;
    CheckBox camopCheckBox;
    CheckBox steadicamCheckBox;
    CheckBox gafferCheckBox;
    CheckBox bestboyCheckBox;
    CheckBox electricCheckBox;
    CheckBox keygripCheckBox;
    CheckBox gripCheckBox;
    CheckBox dollygripCheckBox;
    CheckBox soundmixerCheckBox;
    CheckBox boomCheckBox;
    CheckBox proddesignerCheckBox;
    CheckBox artdirectorCheckBox;
    CheckBox setdesignerCheckBox;
    CheckBox setdecorCheckBox;
    CheckBox costumeCheckBox;
    CheckBox makeupCheckBox;
    CheckBox hairCheckBox;
    CheckBox efxsuperCheckBox;
    CheckBox efxassistCheckBox;
    CheckBox stuntCheckBox;
    CheckBox ditCheckBox;
    CheckBox paCheckBox;
    CheckBox otherCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_crew);

        Toolbar toolbar = (Toolbar) findViewById(R.id.crew_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Change app bar to say "Select Your Crew Positions"
        setTitle(getString(R.string.crew_select_activity_title));

        // Get all CheckBoxes
        directorCheckBox = (CheckBox) findViewById(R.id.crew_director);
        ad1stCheckBox = (CheckBox) findViewById(R.id.crew_1st_ad);
        ad2ndCheckBox = (CheckBox) findViewById(R.id.crew_2nd_ad);
        scriptCheckBox = (CheckBox) findViewById(R.id.crew_script);
        dpCheckBox = (CheckBox) findViewById(R.id.crew_dp);
        ac1stCheckBox = (CheckBox) findViewById(R.id.crew_1st_ac);
        ac2ndCheckBox = (CheckBox) findViewById(R.id.crew_2nd_ac);
        camopCheckBox = (CheckBox) findViewById(R.id.crew_cam_op);
        steadicamCheckBox = (CheckBox) findViewById(R.id.crew_steadicam);
        gafferCheckBox = (CheckBox) findViewById(R.id.crew_gaffer);
        bestboyCheckBox = (CheckBox) findViewById(R.id.crew_best_boy);
        electricCheckBox = (CheckBox) findViewById(R.id.crew_electric);
        keygripCheckBox = (CheckBox) findViewById(R.id.crew_key_grip);
        gripCheckBox = (CheckBox) findViewById(R.id.crew_grip);
        dollygripCheckBox = (CheckBox) findViewById(R.id.crew_dolly_grip);
        soundmixerCheckBox = (CheckBox) findViewById(R.id.crew_sound_mixer);
        boomCheckBox = (CheckBox) findViewById(R.id.crew_boom);
        proddesignerCheckBox = (CheckBox) findViewById(R.id.crew_prod_designer);
        artdirectorCheckBox = (CheckBox) findViewById(R.id.crew_art_director);
        setdesignerCheckBox = (CheckBox) findViewById(R.id.crew_set_designer);
        setdecorCheckBox = (CheckBox) findViewById(R.id.crew_set_decor);
        costumeCheckBox = (CheckBox) findViewById(R.id.crew_costume);
        makeupCheckBox = (CheckBox) findViewById(R.id.crew_makeup);
        hairCheckBox = (CheckBox) findViewById(R.id.crew_hair);
        efxsuperCheckBox = (CheckBox) findViewById(R.id.crew_efx_super);
        efxassistCheckBox = (CheckBox) findViewById(R.id.crew_efx_assist);
        stuntCheckBox = (CheckBox) findViewById(R.id.crew_stunt);
        ditCheckBox = (CheckBox) findViewById(R.id.crew_dit);
        paCheckBox = (CheckBox) findViewById(R.id.crew_pa);
        otherCheckBox = (CheckBox) findViewById(R.id.crew_other);

        // Get an ArrayList with the positions set in the sharedPreferences
        ArrayList<Integer> positions = PPHelper.getMyPositions(this);

        // For each crew position in the array, set as checked it's respective CheckBox
        for (int i = 0; i < positions.size(); i++) {
            switch (positions.get(i)) {
                case CrewPosition.CREW_BITS_DIRECTOR:
                    directorCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_1STAD:
                    ad1stCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_2NDAD:
                    ad2ndCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_SCRIPT:
                    scriptCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_PA:
                    paCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_DP:
                    dpCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_CAMOP:
                    camopCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_1STAC:
                    ac1stCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_2NDAC:
                    ac2ndCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_DIT:
                    ditCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_STEADICAM:
                    steadicamCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_GAFFER:
                    gafferCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_FOCUSPULLER:
                    bestboyCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_ELECTRIC:
                    electricCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_KEYGRIP:
                    keygripCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_GRIP:
                    gripCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_STILLPHOTO:
                    dollygripCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_PRODSOUNDMIXER:
                    soundmixerCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_BOOMOP:
                    boomCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_PRODDESIGNER:
                    proddesignerCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_ARTDIRECTOR:
                    artdirectorCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_SETDESIGNER:
                    setdesignerCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_SETDECORATOR:
                    setdecorCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_COSTDESIGNER:
                    costumeCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_MAKEUP:
                    makeupCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_HAIR:
                    hairCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_EFXSUPER:
                    efxsuperCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_EFXASSIST:
                    efxassistCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_STUNT:
                    stuntCheckBox.setChecked(true);
                    break;
                case CrewPosition.CREW_BITS_OTHER:
                    otherCheckBox.setChecked(true);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid argument passed to getMyDate");
            }
        }

    }

    /**
     * This method is called when the cancel button is clicked.
     * It doesn't need to do anything so it only finishes the activity.
     */
    public void cancelCrewSelection(View view) {
        // Just ends the activity
        finish();
    }

    /**
     * This method is called when the save button is clicked.
     */
    public void saveCrewSelection(View view) {
        // Variable to store the positions checked by the user
        int checkedPositions = 0;

        // Get all CheckBoxes
        directorCheckBox = (CheckBox) findViewById(R.id.crew_director);
        ad1stCheckBox = (CheckBox) findViewById(R.id.crew_1st_ad);
        ad2ndCheckBox = (CheckBox) findViewById(R.id.crew_2nd_ad);
        scriptCheckBox = (CheckBox) findViewById(R.id.crew_script);
        dpCheckBox = (CheckBox) findViewById(R.id.crew_dp);
        ac1stCheckBox = (CheckBox) findViewById(R.id.crew_1st_ac);
        ac2ndCheckBox = (CheckBox) findViewById(R.id.crew_2nd_ac);
        camopCheckBox = (CheckBox) findViewById(R.id.crew_cam_op);
        steadicamCheckBox = (CheckBox) findViewById(R.id.crew_steadicam);
        gafferCheckBox = (CheckBox) findViewById(R.id.crew_gaffer);
        bestboyCheckBox = (CheckBox) findViewById(R.id.crew_best_boy);
        electricCheckBox = (CheckBox) findViewById(R.id.crew_electric);
        keygripCheckBox = (CheckBox) findViewById(R.id.crew_key_grip);
        gripCheckBox = (CheckBox) findViewById(R.id.crew_grip);
        dollygripCheckBox = (CheckBox) findViewById(R.id.crew_dolly_grip);
        soundmixerCheckBox = (CheckBox) findViewById(R.id.crew_sound_mixer);
        boomCheckBox = (CheckBox) findViewById(R.id.crew_boom);
        proddesignerCheckBox = (CheckBox) findViewById(R.id.crew_prod_designer);
        artdirectorCheckBox = (CheckBox) findViewById(R.id.crew_art_director);
        setdesignerCheckBox = (CheckBox) findViewById(R.id.crew_set_designer);
        setdecorCheckBox = (CheckBox) findViewById(R.id.crew_set_decor);
        costumeCheckBox = (CheckBox) findViewById(R.id.crew_costume);
        makeupCheckBox = (CheckBox) findViewById(R.id.crew_makeup);
        hairCheckBox = (CheckBox) findViewById(R.id.crew_hair);
        efxsuperCheckBox = (CheckBox) findViewById(R.id.crew_efx_super);
        efxassistCheckBox = (CheckBox) findViewById(R.id.crew_efx_assist);
        stuntCheckBox = (CheckBox) findViewById(R.id.crew_stunt);
        ditCheckBox = (CheckBox) findViewById(R.id.crew_dit);
        paCheckBox = (CheckBox) findViewById(R.id.crew_pa);
        otherCheckBox = (CheckBox) findViewById(R.id.crew_other);

        // For each check position, "add" it to checkedPositions variable.
        if (directorCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_DIRECTOR;
        }
        if (ad1stCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_1STAD;
        }
        if (ad2ndCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_2NDAD;
        }
        if (scriptCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_SCRIPT;
        }
        if (paCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_PA;
        }
        if (dpCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_DP;
        }
        if (camopCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_CAMOP;
        }
        if (ac1stCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_1STAC;
        }
        if (ac2ndCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_2NDAC;
        }
        if (ditCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_DIT;
        }
        if (steadicamCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_STEADICAM;
        }
        if (gafferCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_GAFFER;
        }
        if (bestboyCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_FOCUSPULLER;
        }
        if (electricCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_ELECTRIC;
        }
        if (keygripCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_KEYGRIP;
        }
        if (gripCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_GRIP;
        }
        if (dollygripCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_STILLPHOTO;
        }
        if (soundmixerCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_PRODSOUNDMIXER;
        }
        if (boomCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_BOOMOP;
        }
        if (proddesignerCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_PRODDESIGNER;
        }
        if (artdirectorCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_ARTDIRECTOR;
        }
        if (setdesignerCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_SETDESIGNER;
        }
        if (setdecorCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_SETDECORATOR;
        }
        if (costumeCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_COSTDESIGNER;
        }
        if (makeupCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_MAKEUP;
        }
        if (hairCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_HAIR;
        }
        if (efxsuperCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_EFXSUPER;
        }
        if (efxassistCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_EFXASSIST;
        }
        if (stuntCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_STUNT;
        }
        if (otherCheckBox.isChecked()) {
            checkedPositions += CrewPosition.CREW_BITS_OTHER;
        }

        // Save crew positions preferences
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putInt(MainActivity.PREF_POSITIONS, checkedPositions);
        editorPref.commit();

        // Exit the activity
        setResult(RESULT_OK);
        finish();
    }
}

