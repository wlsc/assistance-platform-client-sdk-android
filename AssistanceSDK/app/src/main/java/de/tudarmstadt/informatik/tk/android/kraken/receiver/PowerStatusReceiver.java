package de.tudarmstadt.informatik.tk.android.kraken.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.constant.PowerState;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.kraken.provider.HarvesterServiceProvider;
import de.tudarmstadt.informatik.tk.android.kraken.service.HarvesterService;
import de.tudarmstadt.informatik.tk.android.kraken.util.BatteryUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.DeviceUtils;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStatusReceiver extends BroadcastReceiver {

    private static final String TAG = PowerStatusReceiver.class.getSimpleName();

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
     * <p>
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b>  This means you should not perform any operations that
     * return a result to you asynchronously -- in particular, for interacting
     * with services, you should use
     * {@link Context#startService(Intent)} instead of
     * {@link Context#bindService(Intent, ServiceConnection, int)}.  If you wish
     * to interact with a service that is already running, you can use
     * {@link #peekService}.
     * <p>
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
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        Log.d(TAG, "Power status has changed. Type: " + action);

        DbPowerStateEvent powerStateEvent = new DbPowerStateEvent();
        powerStateEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        boolean isAc = BatteryUtils.isPluggedInWithAc(context);
        boolean isUsb = BatteryUtils.isPluggedInWithUsb(context);
        boolean isWireless = BatteryUtils.isPluggedInWithWirelessCharger(context);

        if (isAc) {
            powerStateEvent.setState(PowerState.AC_ADAPTER);
        }

        if (isUsb) {
            powerStateEvent.setState(PowerState.USB);
        }

        if (isWireless) {
            powerStateEvent.setState(PowerState.WIRELESS);
        }

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "Power connected");

            boolean isRunning = DeviceUtils.isServiceRunning(context, HarvesterService.class);

            if (!isRunning) {
                HarvesterServiceProvider.getInstance(context).startSensingService();
            }
        }

        if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Power disconnected");

            powerStateEvent.setState(PowerState.NONE);
        }

        if (Intent.ACTION_BATTERY_LOW.equals(action)) {
            Log.d(TAG, "Remaining battery is really low");

            HarvesterServiceProvider.getInstance(context).stopSensingService();

            powerStateEvent.setIsLow(true);
            powerStateEvent.setIsOkay(false);
        }

        if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
            Log.d(TAG, "Remaining battery is OKAY");

            HarvesterServiceProvider.getInstance(context).startSensingService();

            powerStateEvent.setIsLow(false);
            powerStateEvent.setIsOkay(true);
        }

        DaoProvider.getInstance(context).getPowerStateEventDao().insert(powerStateEvent);
    }
}
