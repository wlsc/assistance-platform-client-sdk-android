package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.api.sensors;

import android.content.res.Resources;

import de.tudarmstadt.informatik.tk.kraken.sdk.R;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class SensorType {

    /**
     * Types of hard and soft sensors
     */


    public static final int POSITION = 1;

    public static final int GYROSCOPE = 2;

    public static final int ACCELEROMETER = 3;

    public static final int MAGNETIC_FIELD = 4;

    public static final int MOTION_ACTIVITY = 5;

    public static final int CONNECTION_EVENT = 6;

    public static final int WIFI_CONNECTION_EVENT = 7;

    public static final int MOBILE_DATA_CONNECTION_EVENT = 8;

    public static final int LOUDNESS_EVENT = 9;

    public static final int MAGNETIC_FIELD_UNCALIBRATED = 10;

    public static final int GYROSCOPE_UNCALIBRATED = 11;


    private SensorType() {
    }

    public static final String getName(int type, Resources res) {
        switch (type) {
            case POSITION:
                return res.getString(R.string.sensor_position);
            case GYROSCOPE:
                return res.getString(R.string.sensor_gyroscope);
            case ACCELEROMETER:
                return res.getString(R.string.sensor_accelerometer);
            case MAGNETIC_FIELD:
                return res.getString(R.string.sensor_magnetic_field);
            case MOTION_ACTIVITY:
                return res.getString(R.string.sensor_motion_activity);
            case CONNECTION_EVENT:
                return res.getString(R.string.sensor_connection);
            case WIFI_CONNECTION_EVENT:
                return res.getString(R.string.sensor_wifi_connection);
            case MOBILE_DATA_CONNECTION_EVENT:
                return res.getString(R.string.sensor_mobile_connection);
            case LOUDNESS_EVENT:
                return res.getString(R.string.sensor_loudness);
            case MAGNETIC_FIELD_UNCALIBRATED:
                return res.getString(R.string.sensor_magnetic_field_uncalibrated);
            case GYROSCOPE_UNCALIBRATED:
                return res.getString(R.string.sensor_gyroscope_uncalibrated);
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
            case POSITION:
                return "position";
            case GYROSCOPE:
                return "gyroscope";
            case ACCELEROMETER:
                return "accelerometer";
            case MAGNETIC_FIELD:
                return "magneticfield";
            case MOTION_ACTIVITY:
                return "motionactivity";
            case CONNECTION_EVENT:
                return "connection";
            case WIFI_CONNECTION_EVENT:
                return "wificonnection";
            case MOBILE_DATA_CONNECTION_EVENT:
                return "mobileconnection";
            case LOUDNESS_EVENT:
                return "loudness";
            case MAGNETIC_FIELD_UNCALIBRATED:
                return "magnetic_field_uncalibrated";
            case GYROSCOPE_UNCALIBRATED:
                return "gyroscope_uncalibrated";
            default:
                return "";
        }
    }
}
