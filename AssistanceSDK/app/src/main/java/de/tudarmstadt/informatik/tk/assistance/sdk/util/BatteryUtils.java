package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public final class BatteryUtils {

    private BatteryUtils() {
    }

    /**
     * Phone is plugged in with AC adapter
     *
     * @param context
     * @return
     */
    public static boolean isPluggedInWithAc(Context context) {

        Intent intent = getBatteryChangedIntent(context);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC;

        return isPlugged;
    }

    private static Intent getBatteryChangedIntent(Context context) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return context.registerReceiver(null, filter);
    }

    /**
     * Phone is plugged in with USB cable
     *
     * @param context
     * @return
     */
    public static boolean isPluggedInWithUsb(Context context) {

        Intent intent = getBatteryChangedIntent(context);
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

        Intent intent = getBatteryChangedIntent(context);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        }

        return isPlugged;
    }

    /**
     * Battery is plugged in
     *
     * @param context
     * @return
     */
    public static boolean isPluggedIn(Context context) {

        Intent intent = getBatteryChangedIntent(context);
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isPlugged = false;

        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC ||
                plugged == BatteryManager.BATTERY_PLUGGED_USB;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isPlugged = isPlugged || (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS);
        }

        return isPlugged;
    }

    /**
     * Returns battery percentage status (0.0-1.0)
     *
     * @param context
     * @return
     */
    public static float getBatteryPercentage(Context context) {

        Intent intent = getBatteryChangedIntent(context);
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale;

        return batteryPct;
    }

    /**
     * Checks of power save mode is enabled
     *
     * @param context
     * @return
     */
    public static boolean isPowerSaveMode(Context context) {

        if (context == null) {
            return false;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (pm == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return pm.isPowerSaveMode();
        } else {
            return false;
        }
    }
}