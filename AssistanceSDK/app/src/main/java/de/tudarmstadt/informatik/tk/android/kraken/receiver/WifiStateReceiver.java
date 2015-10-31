package de.tudarmstadt.informatik.tk.android.kraken.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.kraken.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.kraken.service.EventUploaderService;
import de.tudarmstadt.informatik.tk.android.kraken.util.ConnectionUtils;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 12.10.2015
 */
public class WifiStateReceiver extends BroadcastReceiver {

    private static final String TAG = WifiStateReceiver.class.getSimpleName();

    // Earliest point in time in the future
    // from which your task might start executing
    private long UPLOAD_ALL_TASL_START_SECS = 0L;

    // latest point in time in the future
    // at which your task must have executed
    private long UPLOAD_ALL_TASL_END_SECS = 3600L;

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * {@link Context#registerReceiver(BroadcastReceiver,
     * IntentFilter, String, Handler)}. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     * <p/>
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b>  This means you should not perform any operations that
     * return a result to you asynchronously -- in particular, for interacting
     * with services, you should use
     * {@link Context#startService(Intent)} instead of
     * {@link Context#bindService(Intent, ServiceConnection, int)}.  If you wish
     * to interact with a service that is already running, you can use
     * {@link #peekService}.
     * <p/>
     * <p>The Intent filters used in {@link Context#registerReceiver}
     * and in application manifests are <em>not</em> guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, {@link #onReceive(Context, Intent) onReceive()}
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "Connectivity has changed");

        if (ConnectionUtils.isConnectedWifi(context)) {
            Log.d(TAG, "WI-FI is connected");

            // check for internet connection
            if (ConnectionUtils.isOnline(context)) {
                Log.d(TAG, "Internet is ONLINE");

                // checking that user is logged in the app
                String userToken = PreferenceProvider.getInstance(context).getUserToken();

                if (userToken.isEmpty()) {
                    // do nothing. do not start upload
                    return;
                }

                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.d(TAG, "Starting background uploader task...");
                        uploadAllEvents(context);
                        return null;
                    }
                };

                asyncTask.execute();

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
    private void uploadAllEvents(Context context) {

        Log.d(TAG, "Initializing...");

        PreferenceProvider mPreferenceProvider = PreferenceProvider.getInstance(context);
        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(context);
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken != null && !userToken.isEmpty()) {
            scheduleTask(context);
        } else {
            Log.d(TAG, "User is not logged in. Scheduled task won't start");
        }

        Log.d(TAG, "Finished!");
    }

    /**
     * Starts scheduling of one time task
     *
     * @param context
     */
    private void scheduleTask(Context context) {

        EventUploaderService.scheduleOneTimeTask(context,
                UPLOAD_ALL_TASL_START_SECS,
                UPLOAD_ALL_TASL_END_SECS,
                "onetimetag | 1");
    }
}
