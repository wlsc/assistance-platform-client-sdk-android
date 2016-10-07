package de.tudarmstadt.informatik.tk.assistance.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.OneoffTask.Builder;
import com.google.android.gms.gcm.Task;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.service.LogsUploadService;
import de.tudarmstadt.informatik.tk.assistance.sdk.service.SensorUploadService;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 12.10.2015
 */
public class WifiStateReceiver extends BroadcastReceiver {

    static final String TAG = WifiStateReceiver.class.getSimpleName();

    // Earliest point in time in the future
    // from which your task might start executing
    private static final long UPLOAD_ALL_TASKS_START_SECS = 0l;

    // latest point in time in the future
    // at which your task must have executed
    private static final long UPLOAD_ALL_TASKS_END_SECS = 3600l;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (context == null) {
            return;
        }

        Log.d(TAG, "Connectivity has changed");

        if (ConnectionUtils.isConnectedWifi(context.getApplicationContext())) {
            Log.d(TAG, "WI-FI is connected");

            // check for internet connection
            if (ConnectionUtils.isOnline(context.getApplicationContext())) {
                Log.d(TAG, "Internet is ONLINE");

                // checking that user is logged in the app
                String userToken = PreferenceProvider
                        .getInstance(context.getApplicationContext())
                        .getUserToken();

                if (userToken.isEmpty()) {
                    // do nothing. do not start upload
                    return;
                }

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        Log.d(TAG, "Starting background uploader task...");
                        uploadAllEvents(context.getApplicationContext());

                        return null;
                    }

                }.execute();

                /**
                 * Send all sensor upload logs
                 */
                scheduleSensorLogsUploadTask(context.getApplicationContext());

            } else {
                Log.d(TAG, "Internet is OFFLINE");
            }
        } else {
            Log.d(TAG, "WI-FI is NOT connected");
        }
    }

    /**
     * Uploads all sensors ans events to server
     */
    void uploadAllEvents(Context context) {

        Log.d(TAG, "Initializing...");

        PreferenceProvider mPreferenceProvider = PreferenceProvider.getInstance(context);
        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(context);
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken != null && !userToken.isEmpty()) {
            scheduleSensorDataUploadTask(context);
        } else {
            Log.d(TAG, "User is not logged in. Scheduled task won't start");
        }

        Log.d(TAG, "Finished!");
    }

    /**
     * Schedule all sensor data upload one time task
     *
     * @param context
     */
    private void scheduleSensorDataUploadTask(Context context) {

        SensorUploadService.scheduleOneTimeTask(context,
                UPLOAD_ALL_TASKS_START_SECS,
                UPLOAD_ALL_TASKS_END_SECS,
                "onetimetag | 1");
    }

    /**
     * Schedule all sensor LOGS upload one time task
     *
     * @param context
     */
    private void scheduleSensorLogsUploadTask(Context context) {

        Log.d(TAG, "Scheduling logs upload one time task...");

        OneoffTask oneTimeTask = new Builder()
                .setService(LogsUploadService.class)
                .setExecutionWindow(UPLOAD_ALL_TASKS_START_SECS,
                        UPLOAD_ALL_TASKS_END_SECS)
                .setTag("onetimetag | 2")
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();

        GcmNetworkManager.getInstance(context).schedule(oneTimeTask);

        Log.d(TAG, "Logs upload task was scheduled!");
    }
}