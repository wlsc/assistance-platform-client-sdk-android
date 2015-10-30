package de.tudarmstadt.informatik.tk.android.kraken.model.api.dto;

import android.content.res.Resources;

import de.tudarmstadt.informatik.tk.android.kraken.R;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class DtoType {

    /**
     * Type of sensors and events
     */

    public static final int LOCATION = 1;

    public static final int GYROSCOPE = 2;

    public static final int ACCELEROMETER = 3;

    public static final int MAGNETIC_FIELD = 4;

    public static final int MOTION_ACTIVITY = 5;

    public static final int CONNECTION = 6;

    public static final int WIFI_CONNECTION = 7;

    public static final int MOBILE_DATA_CONNECTION = 8;

    public static final int LOUDNESS = 9;

    public static final int FOREGROUND = 12;

    public static final int LIGHT = 13;

    public static final int ONE_TIME_SENSOR_RUNNING_SERVICES = 14;

    public static final int ONE_TIME_SENSOR_ACCOUNT_READER = 15;

    public static final int ONE_TIME_SENSOR_RUNNING_TASKS = 16;

    public static final int ONE_TIME_SENSOR_RUNNING_PROCESSES = 17;

    public static final int RINGTONE = 18;

    public static final int BACKGROUND_TRAFFIC = 19;

    public static final int CONTACTS = 20;

    public static final int CALL_LOG = 21;

    public static final int CALENDAR = 22;

    public static final int BROWSER_HISTORY = 23;

    public static final int NETWORK_TRAFFIC = 24;

    public static final int CALENDAR_REMINDER = 25;

    private DtoType() {
    }

    public static final String getName(int type, Resources res) {
        switch (type) {
            case LOCATION:
                return res.getString(R.string.sensor_position);
            case GYROSCOPE:
                return res.getString(R.string.sensor_gyroscope);
            case ACCELEROMETER:
                return res.getString(R.string.sensor_accelerometer);
            case MAGNETIC_FIELD:
                return res.getString(R.string.sensor_magnetic_field);
            case MOTION_ACTIVITY:
                return res.getString(R.string.sensor_motion_activity);
            case CONNECTION:
                return res.getString(R.string.sensor_connection);
            case WIFI_CONNECTION:
                return res.getString(R.string.sensor_wifi_connection);
            case MOBILE_DATA_CONNECTION:
                return res.getString(R.string.sensor_mobile_connection);
            case LOUDNESS:
                return res.getString(R.string.sensor_loudness);
            case FOREGROUND:
                return res.getString(R.string.sensor_foreground_event);
            case LIGHT:
                return res.getString(R.string.sensor_light);
            default:
                return "";
        }
    }

    /**
     * Returns proper name for an API
     *
     * @param type
     * @return
     */
    public static final String getApiName(int type) {

        switch (type) {
            case LOCATION:
                return "position";
            case GYROSCOPE:
                return "gyroscope";
            case ACCELEROMETER:
                return "accelerometer";
            case MAGNETIC_FIELD:
                return "magneticfield";
            case MOTION_ACTIVITY:
                return "motionactivity";
            case CONNECTION:
                return "connection";
            case WIFI_CONNECTION:
                return "wificonnection";
            case MOBILE_DATA_CONNECTION:
                return "mobileconnection";
            case LOUDNESS:
                return "loudness";
            case FOREGROUND:
                return "foreground";
            case LIGHT:
                return "light";
            default:
                return "";
        }
    }
}
