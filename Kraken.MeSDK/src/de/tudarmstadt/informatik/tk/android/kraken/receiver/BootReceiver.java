package de.tudarmstadt.informatik.tk.android.kraken.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.kraken.preference.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.service.KrakenServiceManager;

/**
 * @author Karsten Planz
 * @edited on 19.09.2015 by  Wladimir Schmidt (wlsc.dev@gmail.com)
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean activated = PreferenceManager.getInstance(context).getActivated();

        Log.d(TAG, "BootReceiver onReceive " + activated);

        if (activated) {

            final KrakenServiceManager service = KrakenServiceManager.getInstance(context);
            service.startKrakenService();
        }
    }
}
