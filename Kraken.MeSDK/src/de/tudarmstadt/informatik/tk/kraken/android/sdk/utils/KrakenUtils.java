package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import android.content.Context;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.SdkAuthentication;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.preference.PreferenceManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.SensorManager;

import static android.provider.Settings.Secure;

/**
 * @author Karsten Planz
 */
public class KrakenUtils {


//    private static ObjectMapper mMapper;

    public static void initDataProfile(Context context, String dataProfile) {

        if(dataProfile.equals(PreferenceManager.KRAKEN_DATA_PROFILE_BASIC)) {
            enableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_BASIC);
            disableSensors(context, KrakenSdkSettings.SENSORS_PROFILE_FULL);
        }
        else if(dataProfile.equals(PreferenceManager.KRAKEN_DATA_PROFILE_FULL)) {
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

    public static Long getGlobalId(Context context, Long localId) {

        String kroken = SdkAuthentication.getInstance(context).getKroken();
        String deviceId = getDeviceId(context);
        String globalId = kroken + deviceId + localId;

        Integer hashCode = globalId.hashCode();

        return Long.valueOf(hashCode);
    }

//    public static ObjectMapper getJacksonObjectMapper() {
//        if (mMapper == null) {
//            mMapper = new ObjectMapper();
//            mMapper.registerModule(new JsonOrgModule());
//        }
//        return mMapper;
//    }

}
