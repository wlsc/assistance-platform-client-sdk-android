package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
     * Generates new random UUID string
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
}