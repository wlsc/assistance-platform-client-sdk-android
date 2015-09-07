package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors;

import android.content.Context;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.sdk.R;

public enum ESensorType {
    MEASUREMENT_LOG,
    SENSOR_ACCELEROMETER,
    SENSOR_ACTIVITY,
    SENSOR_CONNECTION,
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
    SENSOR_CALLLOG,
    SENSOR_BROWSER_HISTORY,
    SENSOR_FOREGROUND_EVENT,
    SENSOR_NETWORK_TRAFFIC,
    SENSOR_BACKGROUND_TRAFFIC;

    public String getSensorName() {
        switch (this) {
            case MEASUREMENT_LOG:
                return "Measurement Log";
            case ONE_TIME_SENSOR_ACCOUNT_READER:
                return "Account Reader";
            case ONE_TIME_SENSOR_RUNNING_PROCESSES:
                return "Running Processes";
            case ONE_TIME_SENSOR_RUNNING_SERVICES:
                return "Running Services";
            case ONE_TIME_SENSOR_RUNNING_TASKS:
                return "Running Tasks";
            case SENSOR_ACCELEROMETER:
                return "Accelerometer";
            case SENSOR_ACTIVITY:
                return "Activity";
            case SENSOR_CALENDAR:
                return "Calendar Event";
            case SENSOR_CONNECTION:
                return "Connection";
            case SENSOR_CONTACTS:
                return "Contact";
            case SENSOR_LIGHT:
                return "Light";
            case SENSOR_LOCATION:
                return "Location";
            case SENSOR_RINGTONE:
                return "Ringtone";
            case SENSOR_LOUDNESS:
                return "Loudness";
            case SENSOR_CALLLOG:
                return "Call Log";
            case SENSOR_BROWSER_HISTORY:
                return "Browser History";
            case SENSOR_FOREGROUND_EVENT:
                return "Foreground Event";
            case SENSOR_NETWORK_TRAFFIC:
                return "Network Traffic";
            case SENSOR_BACKGROUND_TRAFFIC:
                return "Network Traffic";
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
            case SENSOR_ACCELEROMETER:
                return context.getString(R.string.sensor_accelerometer);
            case SENSOR_ACTIVITY:
                return context.getString(R.string.sensor_activity);
            case SENSOR_CALENDAR:
                return context.getString(R.string.sensor_calendar);
            case SENSOR_CONNECTION:
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
            case SENSOR_CALLLOG:
                return context.getString(R.string.sensor_calllog);
            case SENSOR_BROWSER_HISTORY:
                return context.getString(R.string.sensor_browser_history);
            case SENSOR_FOREGROUND_EVENT:
                return context.getString(R.string.sensor_foreground_event);
            case SENSOR_NETWORK_TRAFFIC:
                return context.getString(R.string.sensor_network_traffic);
            case SENSOR_BACKGROUND_TRAFFIC:
                return "Traffic usage by each app";
            default:
                return "";
        }
    }

    public String getFullqualifiedDatabaseClassName() {
        return "de.tudarmstadt.informatik.tk.kraken.android.sdk.db.Sensor" + getSensorName().replaceAll(" ", "");
    }

    @SuppressWarnings("unchecked")
    public String getServerClassName() {
        try {
            Class<? extends IDbSensor> sensor = (Class<? extends IDbSensor>) Class.forName(getFullqualifiedDatabaseClassName());
            String strClassNameForServer = sensor.getSimpleName();
            // handle special case Location
            if (strClassNameForServer.equals("SensorLocation")) {
                return "Location";
            }
            if (strClassNameForServer.startsWith("Sensor")) {
                strClassNameForServer = strClassNameForServer.substring("Sensor".length());
            }
            strClassNameForServer = "Android" + strClassNameForServer;
            return strClassNameForServer;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends IDbSensor> getAndroidClass(String serverClassName) {
        String androidClassName = null;
        if (serverClassName.startsWith("Android")) {
            androidClassName = serverClassName.substring("Android".length());
        }
        androidClassName = androidClassName + "Sensor";
        try {
            return (Class<? extends IDbSensor>) Class.forName(androidClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}

