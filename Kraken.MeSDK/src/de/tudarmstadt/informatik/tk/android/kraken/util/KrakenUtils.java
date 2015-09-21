package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.android.kraken.preference.PreferenceManager;

import static android.provider.Settings.Secure;

/**
 * @author Karsten Planz
 */
public class KrakenUtils {


//    private static ObjectMapper mMapper;

    public static void initDataProfile(Context context, String dataProfile) {

        if (dataProfile.equals(PreferenceManager.KRAKEN_DATA_PROFILE_BASIC)) {
            enableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_BASIC);
            disableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_FULL);
        } else if (dataProfile.equals(PreferenceManager.KRAKEN_DATA_PROFILE_FULL)) {
            enableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_BASIC);
            enableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_FULL);
        }
    }

    public static void enableSensors(Context context, ESensorType[] sensorTypes) {

        SensorManager sensorManager = SensorManager.getInstance(context);
        for (ESensorType sensorType : sensorTypes) {
            sensorManager.getSensor(sensorType).setDisabledByUser(false);
            sensorManager.getSensor(sensorType).startSensor();
        }
    }

    public static void disableSensors(Context context, ESensorType[] sensorTypes) {

        SensorManager sensorManager = SensorManager.getInstance(context);
        for (ESensorType sensorType : sensorTypes) {
            sensorManager.getSensor(sensorType).setDisabledByUser(true);
            sensorManager.getSensor(sensorType).stopSensor();
        }
    }

    public static String getDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

}
