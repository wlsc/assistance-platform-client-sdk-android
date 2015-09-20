package de.tudarmstadt.informatik.tk.android.kraken.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 20.09.2015
 */
public class HarvesterService extends Service {

    private static final String TAG = HarvesterService.class.getSimpleName();

    public static final String KRAKEN_HARVESTER_TIMER_NAME = "KrakenHarvester";

    private Timer timer;

    private TimerTask updateTask = new TimerTask() {
        @Override
        public void run() {
            Log.d(TAG, KRAKEN_HARVESTER_TIMER_NAME + " beginning its work.");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service creating...");

        timer = new Timer(KRAKEN_HARVESTER_TIMER_NAME);
        timer.schedule(updateTask, 1000L, 60 * 1000L);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
        timer = null;

        Log.d(TAG, "Service destroyed");
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p/>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
