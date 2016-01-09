package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.os.Build;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class SensorApiType {

    // holder of API type string to DTO type mappings
    private static final Map<String, Integer> apiNameToDtoTypeMap = new HashMap<>();

    // mapper for Android sensors to DTO types
    private static final SparseIntArray androidSensorToDtoTypeMap = new SparseIntArray();

    private static List<String> allPossibleTypes = new ArrayList<>();

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

    public static final int POWER_STATE = 10;

    public static final int POWER_LEVEL = 11;

    public static final int FOREGROUND = 12;

    public static final int LIGHT = 13;

    public static final int RUNNING_SERVICES = 14;

    public static final int ACCOUNT_READER = 15;

    public static final int RUNNING_TASKS = 16;

    public static final int RUNNING_PROCESSES = 17;

    public static final int RINGTONE = 18;

    public static final int BACKGROUND_TRAFFIC = 19;

    public static final int CONTACT = 20;

    public static final int CALL_LOG = 21;

    public static final int CALENDAR = 22;

    public static final int BROWSER_HISTORY = 23;

    public static final int FOREGROUND_TRAFFIC = 24;

    public static final int UNI_TUCAN = 25;

    public static final int SOCIAL_FACEBOOK = 26;

    static {

        // always give here the last one
        createAllPossibleModuleTypes(SOCIAL_FACEBOOK);

        apiNameToDtoTypeMap.put(getApiName(LOCATION), LOCATION);
        apiNameToDtoTypeMap.put(getApiName(GYROSCOPE), GYROSCOPE);
        apiNameToDtoTypeMap.put(getApiName(ACCELEROMETER), ACCELEROMETER);
        apiNameToDtoTypeMap.put(getApiName(MAGNETIC_FIELD), MAGNETIC_FIELD);
        apiNameToDtoTypeMap.put(getApiName(MOTION_ACTIVITY), MOTION_ACTIVITY);
        apiNameToDtoTypeMap.put(getApiName(CONNECTION), CONNECTION);
        apiNameToDtoTypeMap.put(getApiName(WIFI_CONNECTION), WIFI_CONNECTION);
        apiNameToDtoTypeMap.put(getApiName(MOBILE_DATA_CONNECTION), MOBILE_DATA_CONNECTION);
        apiNameToDtoTypeMap.put(getApiName(LOUDNESS), LOUDNESS);
        apiNameToDtoTypeMap.put(getApiName(POWER_STATE), POWER_STATE);
        apiNameToDtoTypeMap.put(getApiName(POWER_LEVEL), POWER_LEVEL);
        apiNameToDtoTypeMap.put(getApiName(FOREGROUND), FOREGROUND);
        apiNameToDtoTypeMap.put(getApiName(LIGHT), LIGHT);
        apiNameToDtoTypeMap.put(getApiName(RUNNING_SERVICES), RUNNING_SERVICES);
        apiNameToDtoTypeMap.put(getApiName(ACCOUNT_READER), ACCOUNT_READER);
        apiNameToDtoTypeMap.put(getApiName(RUNNING_TASKS), RUNNING_TASKS);
        apiNameToDtoTypeMap.put(getApiName(RUNNING_PROCESSES), RUNNING_PROCESSES);
        apiNameToDtoTypeMap.put(getApiName(RINGTONE), RINGTONE);
        apiNameToDtoTypeMap.put(getApiName(BACKGROUND_TRAFFIC), BACKGROUND_TRAFFIC);
        apiNameToDtoTypeMap.put(getApiName(CALL_LOG), CALL_LOG);
        apiNameToDtoTypeMap.put(getApiName(BROWSER_HISTORY), BROWSER_HISTORY);
        apiNameToDtoTypeMap.put(getApiName(FOREGROUND_TRAFFIC), FOREGROUND_TRAFFIC);
        apiNameToDtoTypeMap.put(getApiName(CALENDAR), CALENDAR);
        apiNameToDtoTypeMap.put(getApiName(CONTACT), CONTACT);
        apiNameToDtoTypeMap.put(getApiName(UNI_TUCAN), UNI_TUCAN);
        apiNameToDtoTypeMap.put(getApiName(SOCIAL_FACEBOOK), SOCIAL_FACEBOOK);

        androidSensorToDtoTypeMap.put(Sensor.TYPE_ACCELEROMETER, ACCELEROMETER);
        androidSensorToDtoTypeMap.put(Sensor.TYPE_GYROSCOPE, GYROSCOPE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            androidSensorToDtoTypeMap.put(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, GYROSCOPE);
        }

        androidSensorToDtoTypeMap.put(Sensor.TYPE_MAGNETIC_FIELD, MAGNETIC_FIELD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            androidSensorToDtoTypeMap.put(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, MAGNETIC_FIELD);
        }

        androidSensorToDtoTypeMap.put(Sensor.TYPE_LIGHT, LIGHT);
    }

    private SensorApiType() {
    }

    public static String getName(int type, Resources res) {
        switch (type) {
            case LOCATION:
                return res.getString(R.string.sensor_location);
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
                return res.getString(R.string.sensor_foreground);
            case LIGHT:
                return res.getString(R.string.sensor_light);
            case RINGTONE:
                return res.getString(R.string.sensor_ringtone);
            case POWER_STATE:
                return res.getString(R.string.sensor_power_state);
            case POWER_LEVEL:
                return res.getString(R.string.sensor_power_level);
            case CALENDAR:
                return res.getString(R.string.sensor_calendar);
            case CALL_LOG:
                return res.getString(R.string.sensor_calllog);
            case FOREGROUND_TRAFFIC:
                return res.getString(R.string.sensor_network_traffic);
            case BACKGROUND_TRAFFIC:
                return res.getString(R.string.sensor_background_traffic);
            case CONTACT:
                return res.getString(R.string.sensor_contacts);
            case BROWSER_HISTORY:
                return res.getString(R.string.sensor_browser_history);
            case RUNNING_SERVICES:
                return res.getString(R.string.sensor_running_services);
            case ACCOUNT_READER:
                return res.getString(R.string.sensor_account_reader);
            case RUNNING_TASKS:
                return res.getString(R.string.sensor_running_tasks);
            case RUNNING_PROCESSES:
                return res.getString(R.string.sensor_running_processes);
            case UNI_TUCAN:
                return res.getString(R.string.sensor_uni_tucan);
            case SOCIAL_FACEBOOK:
                return res.getString(R.string.sensor_social_facebook);
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
    public static String getApiName(int type) {

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
            case RINGTONE:
                return "ringtone";
            case POWER_STATE:
                return "powerstate";
            case POWER_LEVEL:
                return "powerlevel";
            case CALENDAR:
                return "calendar";
            case CALL_LOG:
                return "call_log";
            case FOREGROUND_TRAFFIC:
                return "networktraffic";
            case BACKGROUND_TRAFFIC:
                return "backgroundtraffic";
            case CONTACT:
                return "contact";
            case BROWSER_HISTORY:
                return "browserhistory";
            case RUNNING_SERVICES:
                return "runningservice";
            case ACCOUNT_READER:
                return "accountreader";
            case RUNNING_TASKS:
                return "runningtask";
            case RUNNING_PROCESSES:
                return "runningprocess";
            case UNI_TUCAN:
                return "tucan";
            case SOCIAL_FACEBOOK:
                return "facebooktoken";
            default:
                return "";
        }
    }

    /**
     * Returns DTO type bei its api name
     *
     * @param apiName
     * @return
     */
    public static int getDtoType(String apiName) {

        if (apiName == null || apiName.isEmpty()) {
            return -1;
        }

        return SensorApiType.apiNameToDtoTypeMap.get(apiName).intValue();
    }

    /**
     * Returns DTO type by Android sensor type
     * (in case of available mapping)
     *
     * @param androidSensorType
     * @return
     */
    public static int getDtoType(int androidSensorType) {
        return androidSensorToDtoTypeMap.get(androidSensorType);
    }

    /**
     * Creates all possible module types
     *
     * @param lastTypeNumber
     */
    private static void createAllPossibleModuleTypes(int lastTypeNumber) {

        for (int i = 1; i <= lastTypeNumber; i++) {

            String apiName = getApiName(i);

            if (!apiName.isEmpty()) {
                allPossibleTypes.add(apiName);
            }
        }
    }
    
    public static List<String> getAllPossibleModuleTypes() {
        return allPossibleTypes;
    }
}