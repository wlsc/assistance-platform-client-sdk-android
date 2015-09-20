package de.tudarmstadt.informatik.tk.android.kraken.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    // Kraken prefs
    private static final String KRAKEN_PREFS = "KrakenPrefs";

    public static final String KRAKEN_FIRST_START = "KrakenFirstStart";
    public static final String KRAKEN_ACCEPT_DISCLAIMER = "KrakenAcceptDisclaimer";

    public static final boolean DEFAULT_KRAKEN_FIRST_START = true;
    public static final boolean DEFAULT_KRAKEN_ACCEPT_DISCLAIMER = false;


    // Default prefs
    public static final String KRAKEN_ACTIVATED = "KrakenActivated";
    public static final String KRAKEN_SHOW_NOTIFICATION = "KrakenShowNotification";
    public static final String KRAKEN_DATA_PROFILE = "KrakenDataProfile";

    public static final String KRAKEN_DATA_PROFILE_BASIC = "KrakenDataProfileBasic";
    public static final String KRAKEN_DATA_PROFILE_FULL = "KrakenDataProfileFull";

    public static final boolean DEFAULT_KRAKEN_ACTIVATED = true;
    public static final boolean DEFAULT_KRAKEN_SHOW_NOTIFICATION = true;
    public static final String DEFAULT_KRAKEN_DATA_PROFILE = KRAKEN_DATA_PROFILE_BASIC;


    private SharedPreferences prefs;
    private SharedPreferences defaultPrefs;
    private static PreferenceManager manager;

    public PreferenceManager(Context context) {
        this.prefs = context.getSharedPreferences(KRAKEN_PREFS, Context.MODE_PRIVATE);
        this.defaultPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceManager getInstance(Context context) {
        if (manager == null) {
            manager = new PreferenceManager(context);
        }
        return manager;
    }

    // Kraken prefs

    public boolean getFirstStart() {
        return this.prefs.getBoolean(KRAKEN_FIRST_START, DEFAULT_KRAKEN_FIRST_START);
    }

    public void setFirstStart(boolean firstStart) {
        this.prefs.edit().putBoolean(KRAKEN_FIRST_START, firstStart).apply();
    }

    public boolean getAcceptDisclaimer() {
        return this.prefs.getBoolean(KRAKEN_ACCEPT_DISCLAIMER, DEFAULT_KRAKEN_ACCEPT_DISCLAIMER);
    }

    public void setAcceptDisclaimer(boolean acceptDisclaimer) {
        this.prefs.edit().putBoolean(KRAKEN_ACCEPT_DISCLAIMER, acceptDisclaimer).apply();
    }

    // Default prefs

    public String getDataProfile() {
        return this.defaultPrefs.getString(KRAKEN_DATA_PROFILE, DEFAULT_KRAKEN_DATA_PROFILE);
    }

    public void setDataProfile(String dataProfile) {
        this.defaultPrefs.edit().putString(KRAKEN_DATA_PROFILE, dataProfile).apply();
    }

    public boolean getActivated() {
        return this.defaultPrefs.getBoolean(KRAKEN_ACTIVATED, DEFAULT_KRAKEN_ACTIVATED);
    }

    public void setActivated(boolean activated) {
        this.defaultPrefs.edit().putBoolean(KRAKEN_ACTIVATED, activated).apply();
    }

    public boolean getShowNotification() {
        return this.defaultPrefs.getBoolean(KRAKEN_SHOW_NOTIFICATION, DEFAULT_KRAKEN_SHOW_NOTIFICATION);
    }

    public void setShowNotification(boolean showNotification) {
        this.defaultPrefs.edit().putBoolean(KRAKEN_SHOW_NOTIFICATION, showNotification).apply();
    }


    public SharedPreferences getDefaultPreferences() {
        return defaultPrefs;
    }

}
