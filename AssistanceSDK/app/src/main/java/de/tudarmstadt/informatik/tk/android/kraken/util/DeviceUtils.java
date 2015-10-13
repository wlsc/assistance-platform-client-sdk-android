package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 12.10.2015
 */
public class DeviceUtils {

    private DeviceUtils() {
    }

    /**
     * Returns device's network information state
     *
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetwork(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * Checks if mobile is connected / active
     *
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context) {

        NetworkInfo activeNetwork = getActiveNetwork(context);

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if Wi-Fi is connected / active
     *
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context) {

        NetworkInfo activeNetwork = getActiveNetwork(context);

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if device is connected to the internet
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {

        NetworkInfo networkInfo = getActiveNetwork(context);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Checks if airplane mode enabled right now
     *
     * @param context
     * @return
     */
    public static boolean isAirplaneModeEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            return Settings.System.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON,
                    0) == 1;
        } else {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON,
                    0) == 1;
        }
    }

    /**
     * Checks if given sensor contains physically on current device
     *
     * @param context
     * @return
     */
    public static boolean hasSensor(Context context, int sensorType) {

        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(sensorType);

        return sensor != null;
    }

    /**
     * Checks if some given service is currently running
     *
     * @param context
     * @param clazz
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> clazz) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // getting all services
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (clazz.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
