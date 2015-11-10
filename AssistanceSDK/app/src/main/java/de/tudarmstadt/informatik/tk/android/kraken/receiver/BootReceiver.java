package de.tudarmstadt.informatik.tk.android.kraken.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.kraken.provider.HarvesterServiceProvider;
import de.tudarmstadt.informatik.tk.android.kraken.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.kraken.service.HarvesterService;
import de.tudarmstadt.informatik.tk.android.kraken.util.DeviceUtils;

/**
 * @author Karsten Planz
 * @edited on 19.09.2015 by  Wladimir Schmidt (wlsc.dev@gmail.com)
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        // check for proper action
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            boolean activated = PreferenceProvider.getInstance(context).getActivated();

            Log.d(TAG, "BootReceiver onReceive " + activated);

            if (activated) {

                Log.d(TAG, "Start on boot activated -> starting service...");

                if (!DeviceUtils.isServiceRunning(
                        context,
                        HarvesterService.class)) {

                    HarvesterServiceProvider.getInstance(context).startSensingService();
                }
            }
        }
    }
}
