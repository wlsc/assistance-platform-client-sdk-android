package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 12.10.2015
 */
public class DeviceUtils {

    private DeviceUtils() {
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
