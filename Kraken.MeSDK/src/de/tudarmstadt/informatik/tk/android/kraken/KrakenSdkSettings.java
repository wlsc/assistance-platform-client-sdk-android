package de.tudarmstadt.informatik.tk.android.kraken;


import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;

public class KrakenSdkSettings {

    public static final String SERVER_URL = "https://kraken.me";

    public static final String DEVICE_REGISTRATION_ENDPOINT = "/devices/register_for_messaging";

    public static final String DEVICE_LIST_ENDPOINT = "/devices/list";

    public static final String PREFERENCES_NAME = "KrakenSDKPreferences";

    public static final String DATABASE_NAME = "assistance.sqlite";

    public static final String PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX = "_disabledByUser";
    public static final String PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX = "_lastPushedTimestamp";

    public static final ESensorType[] SENSORS_PROFILE_BASIC = {
            ESensorType.SENSOR_ACCELEROMETER,
            ESensorType.SENSOR_CONNECTION,
            ESensorType.SENSOR_LIGHT,
            // ESensorType.SENSOR_LOUDNESS,
            ESensorType.SENSOR_ACTIVITY,
            ESensorType.SENSOR_RINGTONE,
            ESensorType.SENSOR_FOREGROUND_EVENT,
            ESensorType.SENSOR_BROWSER_HISTORY
    };

    public static final ESensorType[] SENSORS_PROFILE_FULL = {
            ESensorType.SENSOR_CALENDAR,
            ESensorType.SENSOR_LOCATION,
            ESensorType.SENSOR_CALLLOG,
            ESensorType.SENSOR_CONTACTS,
            ESensorType.SENSOR_NETWORK_TRAFFIC,
            ESensorType.SENSOR_BACKGROUND_TRAFFIC
    };
}
