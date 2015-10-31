package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class BatteryUtils {

    private BatteryUtils() {
    }

    /**
     * Phone is plugged in with AC adapter
     *
     * @param context
     * @return
     */
    public static boolean isPluggedInWithAc(Context context) {

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, filter);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC;

        return isPlugged;
    }

    /**
     * Phone is plugged in with USB cable
     *
     * @param context
     * @return
     */
    public static boolean isPluggedInWithUsb(Context context) {

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, filter);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_USB;

        return isPlugged;
    }

    /**
     * Is phone plugged in with WIRELESS charger
     *
     * @param context
     * @return
     */
    public static boolean isPluggedInWithWirelessCharger(Context context) {

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, filter);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        return isPlugged;
    }

    /**
     * Battery is plugged in
     *
     * @param context
     * @return
     */
    public static boolean isPluggedIn(Context context) {

        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, filter);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = (plugged == BatteryManager.BATTERY_PLUGGED_AC) ||
                (plugged == BatteryManager.BATTERY_PLUGGED_USB) ||
                (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS);

        return isPlugged;
    }
}
