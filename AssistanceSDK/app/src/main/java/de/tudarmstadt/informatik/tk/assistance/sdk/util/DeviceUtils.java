package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.content.Context;
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

        return sm.getDefaultSensor(sensorType) != null;
    }

}