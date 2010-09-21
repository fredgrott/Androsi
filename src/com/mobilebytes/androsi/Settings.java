package com.mobilebytes.androsi;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

    // ///////////////////// CONSTANTS ///////////////////////////////////////
    /**
     * The key for the show allowed position value
     */
    private static final String SHOW_ALLOWED_POS  = "show_allowed_positions";

    /**
     * This key is used to know if is selected 1 or 2 players
     */
    private static final String IS_DROID_OPPONENT = "is_droid_opponent";

    /**
     * Gets the setting to know if the opponent is Android
     *
     * @param context
     * @return
     */
    public static boolean getIsDroidOpponent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_DROID_OPPONENT, true);
    }

    // ///////////////////// ACCESSORS ///////////////////////////////////////

    /**
     * Get the 'show allowed positions' setting
     */
    public static boolean getShowAllowedPositions(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SHOW_ALLOWED_POS, true);
    }

    // ///////////////////// OVERRIDES //////////////////////////////////////
    /**
     * adding preferences from res
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addPreferencesFromResource(R.xml.settings);
    }

}