package de.tudarmstadt.informatik.tk.android.kraken;


import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;

public class KrakenSdkSettings {

    /*
    *   Assistance login server. CAUTION: Please write this address without "/" at the end!
    */
    public static final String ASSISTANCE_ENDPOINT = "https://130.83.163.115";  // production
//    public static final String ASSISTANCE_ENDPOINT = "http://192.168.56.1";   // development: genymotion emulator
//    public static final String ASSISTANCE_ENDPOINT = "http://192.168.1.102";  // development: localhost

    public static final String SERVER_URL = "https://kraken.me";

    public static final String DEVICE_REGISTRATION_ENDPOINT = "/devices/register_for_messaging";

    public static final String DEVICE_LIST_ENDPOINT = "/devices/list";

    public static final String DEVICE_SET_USER_DEFINED_NAME_ENDPOINT = "/devices/set_user_defined_name";

    public static final String PREFERENCES_NAME = "KrakenSDKPreferences";

    public static final String DATABASE_NAME = "assistance.sqlite";

    public static final String PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX = "_disabledByUser";
    public static final String PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX = "_lastPushedTimestamp";

    public static final ESensorType[] SENSORS_PROFILE_BASIC = {
            ESensorType.ACCELEROMETER_SENSOR,
            ESensorType.CONNECTION_EVENT,
            ESensorType.SENSOR_LIGHT,
            // ESensorType.SENSOR_LOUDNESS,
            ESensorType.MOTION_ACTIVITY_EVENT,
            ESensorType.SENSOR_RINGTONE,
            ESensorType.SENSOR_FOREGROUND_EVENT,
            ESensorType.SENSOR_BROWSER_HISTORY
    };

    public static final ESensorType[] SENSORS_PROFILE_FULL = {
            ESensorType.SENSOR_CALENDAR,
            ESensorType.SENSOR_LOCATION,
            ESensorType.SENSOR_CALL_LOG,
            ESensorType.SENSOR_CONTACTS,
            ESensorType.SENSOR_NETWORK_TRAFFIC,
            ESensorType.SENSOR_BACKGROUND_TRAFFIC
    };

    public static final int KRAKEN_NOTIFICATION_ID = 7331;

    public static final String INTENT_EXTRA_SHOW_ICON = "showIcon";
}
