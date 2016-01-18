package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.09.2015
 */
public class GcmUtils {

    private static final String TAG = GcmUtils.class.getSimpleName();

    private GcmUtils() {
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean isPlayServicesInstalled(Activity activity) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (apiAvailability.isUserResolvableError(resultCode)) {

                apiAvailability
                        .getErrorDialog(activity, resultCode, Config.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported!");
            }

            return false;
        }

        return true;
    }

    /**
     * Starts a periodic task with GCM
     *
     * @param context
     * @param periodSecs
     * @param flexSecs
     * @param tag
     */
    public static void startPeriodicTask(Context context, Class clazz, long periodSecs, long flexSecs, String tag) {

        Log.d(TAG, "Scheduling periodic task...");

        PeriodicTask task = new PeriodicTask.Builder()
                .setService(clazz)
                .setPeriod(periodSecs)
                .setFlex(flexSecs)
                .setTag(tag)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();

        GcmNetworkManager.getInstance(context).schedule(task);

        Log.d(TAG, "Periodic task was scheduled!");
    }

    /**
     * Cancels all GCM Network Manager tasks
     */
    public static void cancelAllTasks(Context context, Class clazz) {
        GcmNetworkManager.getInstance(context).cancelAllTasks(clazz);
    }

    /**
     * Cancels default periodic task
     */
    public static void cancel(Context context, String taskTagDefault, Class clazz) {
        GcmNetworkManager.getInstance(context).cancelTask(taskTagDefault, clazz);
    }

    /**
     * Cancels GCM Network Manager periodic task
     */
    public static void cancelByTag(Context context, @NonNull String tag, Class clazz) {
        GcmNetworkManager.getInstance(context).cancelTask(tag, clazz);
    }
}