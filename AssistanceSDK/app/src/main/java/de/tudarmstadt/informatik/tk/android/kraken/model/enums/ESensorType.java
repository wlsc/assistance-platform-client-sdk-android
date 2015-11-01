package de.tudarmstadt.informatik.tk.android.kraken.model.enums;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbLoudnessEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.R;

public enum ESensorType {
    MEASUREMENT_LOG,
    ACCELEROMETER_SENSOR,
    MOTION_ACTIVITY_EVENT,
    CONNECTION_EVENT,
    SENSOR_LIGHT,
    SENSOR_LOCATION,
    SENSOR_RINGTONE,
    ONE_TIME_SENSOR_ACCOUNT_READER,
    ONE_TIME_SENSOR_RUNNING_PROCESSES,
    ONE_TIME_SENSOR_RUNNING_SERVICES,
    ONE_TIME_SENSOR_RUNNING_TASKS,
    SENSOR_CONTACTS,
    SENSOR_CALENDAR,
    SENSOR_LOUDNESS,
    SENSOR_CALL_LOG,
    SENSOR_BROWSER_HISTORY,
    SENSOR_FOREGROUND_EVENT,
    SENSOR_NETWORK_TRAFFIC,
    SENSOR_BACKGROUND_TRAFFIC;

    private static final String TAG = ESensorType.class.getSimpleName();

    public String getSensorName() {
        switch (this) {
//            case MEASUREMENT_LOG:
//                return "Measurement Log";
//            case ONE_TIME_SENSOR_ACCOUNT_READER:
//                return "Account Reader";
//            case ONE_TIME_SENSOR_RUNNING_PROCESSES:
//                return "Running Processes";
//            case ONE_TIME_SENSOR_RUNNING_SERVICES:
//                return "Running Services";
//            case ONE_TIME_SENSOR_RUNNING_TASKS:
//                return "Running Tasks";
            case ACCELEROMETER_SENSOR:
                return DbAccelerometerSensor.class.getSimpleName();
            case MOTION_ACTIVITY_EVENT:
                return DbMotionActivityEvent.class.getSimpleName();
//            case SENSOR_CALENDAR:
//                return "Calendar Event";
            case CONNECTION_EVENT:
                return DbConnectionEvent.class.getSimpleName();
//            case SENSOR_CONTACTS:
//                return "Contact";
//            case SENSOR_LIGHT:
//                return "Light";
            case SENSOR_LOCATION:
                return DbPositionSensor.class.getSimpleName();
//            case SENSOR_RINGTONE:
//                return "Ringtone";
            case SENSOR_LOUDNESS:
                return DbLoudnessEvent.class.getSimpleName();
//            case SENSOR_CALL_LOG:
//                return "Call Log";
//            case SENSOR_BROWSER_HISTORY:
//                return "Browser History";
//            case SENSOR_FOREGROUND_EVENT:
//                return "Foreground Event";
//            case SENSOR_NETWORK_TRAFFIC:
//                return "Network Traffic";
//            case SENSOR_BACKGROUND_TRAFFIC:
//                return "Network Traffic";
            default:
                return "";
        }
    }

    public String getDisplayName(Context context) {
        switch (this) {
            case MEASUREMENT_LOG:
                return context.getString(R.string.measurement_log);
            case ONE_TIME_SENSOR_ACCOUNT_READER:
                return context.getString(R.string.one_time_sensor_account_reader);
            case ONE_TIME_SENSOR_RUNNING_PROCESSES:
                return context.getString(R.string.one_time_sensor_running_processes);
            case ONE_TIME_SENSOR_RUNNING_SERVICES:
                return context.getString(R.string.one_time_sensor_running_services);
            case ONE_TIME_SENSOR_RUNNING_TASKS:
                return context.getString(R.string.one_time_sensor_running_tasks);
            case ACCELEROMETER_SENSOR:
                return context.getString(R.string.sensor_accelerometer);
            case MOTION_ACTIVITY_EVENT:
                return context.getString(R.string.sensor_activity);
            case SENSOR_CALENDAR:
                return context.getString(R.string.event_calendar);
            case CONNECTION_EVENT:
                return context.getString(R.string.sensor_connection);
            case SENSOR_CONTACTS:
                return context.getString(R.string.sensor_contacts);
            case SENSOR_LIGHT:
                return context.getString(R.string.sensor_light);
            case SENSOR_LOCATION:
                return context.getString(R.string.sensor_location);
            case SENSOR_RINGTONE:
                return context.getString(R.string.sensor_ringtone);
            case SENSOR_LOUDNESS:
                return context.getString(R.string.sensor_loudness);
            case SENSOR_CALL_LOG:
                return context.getString(R.string.event_calllog);
            case SENSOR_BROWSER_HISTORY:
                return context.getString(R.string.event_browser_history);
            case SENSOR_FOREGROUND_EVENT:
                return context.getString(R.string.event_foreground_event);
            case SENSOR_NETWORK_TRAFFIC:
                return context.getString(R.string.event_network_traffic);
            case SENSOR_BACKGROUND_TRAFFIC:
                return "Traffic usage by each app";
            default:
                return "";
        }
    }
}

