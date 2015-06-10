package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;

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
    SENSOR_FOREGROUND_TRAFFIC,
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
        case SENSOR_FOREGROUND_TRAFFIC:
            return "Sensor Foreground Traffic Event";
        case SENSOR_BACKGROUND_TRAFFIC:
            return "Sensor Background Traffic Event";
		default:
			return "";
		}
	}
	
	public String getFullqualifiedDatabaseClassName()
	{
		return "de.tudarmstadt.informatik.tk.kraken.android.sdk.db.Sensor" + getSensorName().replaceAll(" ", "");
	}

    @SuppressWarnings("unchecked")
    public String getServerClassName() {
        try {
            Class<? extends IDbSensor> sensor = (Class<? extends IDbSensor>) Class.forName(getFullqualifiedDatabaseClassName());
            String strClassNameForServer = sensor.getSimpleName();
            // handle special case Location
            if(strClassNameForServer.equals("SensorLocation")) {
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

