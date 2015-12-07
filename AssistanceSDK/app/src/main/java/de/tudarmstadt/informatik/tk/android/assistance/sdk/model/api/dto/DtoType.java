package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto;

import android.content.res.Resources;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;

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

    public static final int CONTACTS = 20;

    public static final int CALL_LOG = 21;

    public static final int CALENDAR = 22;

    public static final int BROWSER_HISTORY = 23;

    public static final int FOREGROUND_TRAFFIC = 24;

    public static final int CALENDAR_REMINDER = 25;

    public static final int CONTACT_EMAIL = 26;

    public static final int CONTACT_NUMBER = 27;

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
            case CONTACTS:
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
}