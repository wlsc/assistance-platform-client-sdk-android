package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceProvider {

    public static final String PREFERENCES_NAME = "AssistanceSDKPreferences";

    // Kraken prefs
    private static final String KRAKEN_PREFS = "KrakenPrefs";

    public static final String PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX = "_disabledByUser";
    public static final String PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX = "_lastPushedTimestamp";

    public static final String KRAKEN_FIRST_START = "KrakenFirstStart";
    public static final String KRAKEN_ACCEPT_DISCLAIMER = "KrakenAcceptDisclaimer";

    public static final boolean DEFAULT_KRAKEN_FIRST_START = true;
    public static final boolean DEFAULT_KRAKEN_ACCEPT_DISCLAIMER = false;

    // GCM related
    private static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    private static final String ASSISTANCE_ACCESSIBILITY_SERVICE_IGNORED_BY_USER = "accessibility_ignored_by_user";

    // Last known user location
    private static final String ASSISTANCE_LAST_LATITUDE = "lastKnownLatitude";
    private static final String ASSISTANCE_LAST_LONGITUDE = "lastKnownLongitude";

    // custom endpoint url
    private static final String ASSISTANCE_CUSTOM_ENDPOINT_PREF = "custom_endpoint";

    // user settings
    public static final String KRAKEN_CURRENT_DEVICE_ID = "current_device_id";
    public static final String KRAKEN_SERVER_DEVICE_ID = "server_device_id";
    public static final String KRAKEN_USER_TOKEN = "user_token";
    public static final String KRAKEN_USER_EMAIL = "user_email";
    public static final String KRAKEN_USER_PASSWORD = "user_password";

    // Default prefs
    public static final String KRAKEN_ACTIVATED = "KrakenActivated";
    public static final String KRAKEN_SHOW_NOTIFICATION = "KrakenShowNotification";
    public static final String KRAKEN_DATA_PROFILE = "KrakenDataProfile";

    public static final String KRAKEN_DATA_PROFILE_BASIC = "KrakenDataProfileBasic";
    public static final String KRAKEN_DATA_PROFILE_FULL = "KrakenDataProfileFull";

    public static final boolean DEFAULT_KRAKEN_ACTIVATED = false;
    public static final boolean DEFAULT_KRAKEN_SHOW_NOTIFICATION = false;
    public static final String DEFAULT_KRAKEN_DATA_PROFILE = KRAKEN_DATA_PROFILE_BASIC;


    private SharedPreferences prefs;
    private SharedPreferences defaultPrefs;
    private static PreferenceProvider manager;

    public PreferenceProvider(Context context) {
        this.prefs = context.getSharedPreferences(KRAKEN_PREFS, Context.MODE_PRIVATE);
        this.defaultPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceProvider getInstance(Context context) {
        if (manager == null) {
            manager = new PreferenceProvider(context);
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

    public boolean getSentGCMTokenToServer() {
        return this.prefs.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public void setSentTokenToServer(boolean value) {
        this.prefs.edit().putBoolean(SENT_TOKEN_TO_SERVER, value).apply();
    }

    // Default prefs

    public String getDataProfile() {
        return this.defaultPrefs.getString(KRAKEN_DATA_PROFILE, DEFAULT_KRAKEN_DATA_PROFILE);
    }

    public void setDataProfile(String dataProfile) {
        this.defaultPrefs.edit().putString(KRAKEN_DATA_PROFILE, dataProfile).apply();
    }

    public long getCurrentDeviceId() {
        return this.defaultPrefs.getLong(KRAKEN_CURRENT_DEVICE_ID, -1);
    }

    public void setCurrentDeviceId(long deviceId) {
        this.defaultPrefs.edit().putLong(KRAKEN_CURRENT_DEVICE_ID, deviceId).apply();
    }

    public long getServerDeviceId() {
        return this.defaultPrefs.getLong(KRAKEN_SERVER_DEVICE_ID, -1);
    }

    public void setServerDeviceId(long deviceId) {
        this.defaultPrefs.edit().putLong(KRAKEN_SERVER_DEVICE_ID, deviceId).apply();
    }

    public boolean getActivated() {
        return this.defaultPrefs.getBoolean(KRAKEN_ACTIVATED, DEFAULT_KRAKEN_ACTIVATED);
    }

    public void setActivated(boolean activated) {
        this.defaultPrefs.edit().putBoolean(KRAKEN_ACTIVATED, activated).apply();
    }

    public String getUserToken() {
        return this.defaultPrefs.getString(KRAKEN_USER_TOKEN, "");
    }

    public void setUserToken(String value) {
        this.defaultPrefs.edit().putString(KRAKEN_USER_TOKEN, value).apply();
    }

    public boolean getShowNotification() {
        return this.defaultPrefs.getBoolean(KRAKEN_SHOW_NOTIFICATION, DEFAULT_KRAKEN_SHOW_NOTIFICATION);
    }

    public void setShowNotification(boolean showNotification) {
        this.defaultPrefs.edit().putBoolean(KRAKEN_SHOW_NOTIFICATION, showNotification).apply();
    }

    /**
     * Returns if user has ignored accessibility tutorial
     */
    public boolean getAccessibilityServiceIgnoredByUser() {
        return prefs.getBoolean(ASSISTANCE_ACCESSIBILITY_SERVICE_IGNORED_BY_USER, false);
    }

    /**
     * Set user has ignored accessibility tutorial
     */
    public void setAccessibilityServiceIgnoredByUser(boolean isIgnored) {
        prefs.edit().putBoolean(ASSISTANCE_ACCESSIBILITY_SERVICE_IGNORED_BY_USER, isIgnored).apply();
    }

    /**
     * Returns last known location latitude
     */
    public double getLastLatitude() {
        return Double.longBitsToDouble(prefs.getLong(ASSISTANCE_LAST_LATITUDE, 0));
    }

    /**
     * Saves last known location latitude
     */
    public void setLastLatitude(double latitude) {
        prefs.edit().putLong(ASSISTANCE_LAST_LATITUDE, Double.doubleToLongBits(latitude)).apply();
    }

    /**
     * Returns last known location longitude
     */
    public double getLastLongitude() {
        return Double.longBitsToDouble(prefs.getLong(ASSISTANCE_LAST_LONGITUDE, 0));
    }

    /**
     * Saves last known location longitude
     */
    public void setLastLongitude(double longitude) {
        prefs.edit().putLong(ASSISTANCE_LAST_LONGITUDE, Double.doubleToLongBits(longitude)).apply();
    }

    public SharedPreferences getDefaultPreferences() {
        return defaultPrefs;
    }

    public String getCustomEndpointUrl() {
        return this.defaultPrefs.getString(ASSISTANCE_CUSTOM_ENDPOINT_PREF, "");
    }

    public void setCustomEndpointUrl(String value) {
        this.defaultPrefs.edit().putString(ASSISTANCE_CUSTOM_ENDPOINT_PREF, value).apply();
    }

    /**
     * Returns user email saved in SharedPreferences
     *
     * @return
     */
    public String getUserEmail() {
        return this.defaultPrefs.getString(KRAKEN_USER_EMAIL, "");
    }

    /**
     * Saves user email to preferences
     *
     * @param value
     */
    public void setUserEmail(String value) {
        this.defaultPrefs.edit().putString(KRAKEN_USER_EMAIL, value).apply();
    }

    /**
     * Returns user password saved in SharedPreferences
     *
     * @return
     */
    public String getUserPassword() {
        return this.defaultPrefs.getString(KRAKEN_USER_PASSWORD, "");
    }

    /**
     * Saves user password to preferences
     *
     * @param value
     */
    public void setUserPassword(String value) {
        this.defaultPrefs.edit().putString(KRAKEN_USER_PASSWORD, value).apply();
    }
}
