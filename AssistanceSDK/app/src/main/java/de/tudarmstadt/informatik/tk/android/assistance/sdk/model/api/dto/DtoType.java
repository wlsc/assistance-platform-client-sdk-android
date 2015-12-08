package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class DtoType {

    // holder of API string to DTO type mappings
    private static final Map<String, Integer> mappings = new HashMap<>();

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

    public static final int CALENDAR_REMINDER = 25;

    public static final int CONTACT_EMAIL = 26;

    public static final int CONTACT_NUMBER = 27;

    static {
        mappings.put(getApiName(LOCATION), LOCATION);
        mappings.put(getApiName(GYROSCOPE), GYROSCOPE);
        mappings.put(getApiName(ACCELEROMETER), ACCELEROMETER);
        mappings.put(getApiName(MAGNETIC_FIELD), MAGNETIC_FIELD);
        mappings.put(getApiName(MOTION_ACTIVITY), MOTION_ACTIVITY);
        mappings.put(getApiName(CONNECTION), CONNECTION);
        mappings.put(getApiName(WIFI_CONNECTION), WIFI_CONNECTION);
        mappings.put(getApiName(MOBILE_DATA_CONNECTION), MOBILE_DATA_CONNECTION);
        mappings.put(getApiName(LOUDNESS), LOUDNESS);
        mappings.put(getApiName(POWER_STATE), POWER_STATE);
        mappings.put(getApiName(POWER_LEVEL), POWER_LEVEL);
        mappings.put(getApiName(FOREGROUND), FOREGROUND);
        mappings.put(getApiName(LIGHT), LIGHT);
        mappings.put(getApiName(RUNNING_SERVICES), RUNNING_SERVICES);
        mappings.put(getApiName(ACCOUNT_READER), ACCOUNT_READER);
        mappings.put(getApiName(RUNNING_TASKS), RUNNING_TASKS);
        mappings.put(getApiName(RUNNING_PROCESSES), RUNNING_PROCESSES);
        mappings.put(getApiName(RINGTONE), RINGTONE);
        mappings.put(getApiName(BACKGROUND_TRAFFIC), BACKGROUND_TRAFFIC);
        mappings.put(getApiName(CONTACT), CONTACT);
        mappings.put(getApiName(CALL_LOG), CALL_LOG);
        mappings.put(getApiName(CALENDAR), CALENDAR);
        mappings.put(getApiName(BROWSER_HISTORY), BROWSER_HISTORY);
        mappings.put(getApiName(FOREGROUND_TRAFFIC), FOREGROUND_TRAFFIC);
        mappings.put(getApiName(CALENDAR_REMINDER), CALENDAR_REMINDER);
        mappings.put(getApiName(CONTACT_EMAIL), CONTACT_EMAIL);
        mappings.put(getApiName(CONTACT_NUMBER), CONTACT_NUMBER);
    }

    private DtoType() {
    }

    public static String getName(int type, Resources res) {
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
                return res.getString(R.string.event_foreground_event);
            case LIGHT:
                return res.getString(R.string.sensor_light);
            case RINGTONE:
                return res.getString(R.string.event_ringtone);
            case POWER_STATE:
                return res.getString(R.string.event_power_state);
            case POWER_LEVEL:
                return res.getString(R.string.event_power_level);
            case CALENDAR:
                return res.getString(R.string.event_calendar);
            case CALENDAR_REMINDER:
                return res.getString(R.string.event_calendar_reminder);
            case CALL_LOG:
                return res.getString(R.string.event_calllog);
            case FOREGROUND_TRAFFIC:
                return res.getString(R.string.event_network_traffic);
            case BACKGROUND_TRAFFIC:
                return res.getString(R.string.event_background_traffic);
            case CONTACT:
                return res.getString(R.string.event_contacts);
            case BROWSER_HISTORY:
                return res.getString(R.string.event_browser_history);
            case RUNNING_SERVICES:
                return res.getString(R.string.event_running_services);
            case ACCOUNT_READER:
                return res.getString(R.string.event_account_reader);
            case RUNNING_TASKS:
                return res.getString(R.string.event_running_tasks);
            case RUNNING_PROCESSES:
                return res.getString(R.string.event_running_processes);
            case CONTACT_EMAIL:
                return res.getString(R.string.event_contact_email);
            case CONTACT_NUMBER:
                return res.getString(R.string.event_contact_number);
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
            case CALENDAR_REMINDER:
                return "calendarreminder";
            case CALL_LOG:
                return "call_log";
            case FOREGROUND_TRAFFIC:
                return "networktraffic";
            case BACKGROUND_TRAFFIC:
                return "backgroundtraffic";
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
            case CONTACT_EMAIL:
                return "contactemail";
            case CONTACT_NUMBER:
                return "contactnumber";
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

        return DtoType.mappings.get(apiName).intValue();
    }
}