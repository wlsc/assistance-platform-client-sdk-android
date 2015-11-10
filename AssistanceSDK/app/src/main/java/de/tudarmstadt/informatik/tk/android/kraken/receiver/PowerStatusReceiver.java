package de.tudarmstadt.informatik.tk.android.kraken.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.constant.PowerChargingStatus;
import de.tudarmstadt.informatik.tk.android.kraken.model.constant.PowerChargingType;
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

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        Log.d(TAG, "Power status has changed. Type: " + action);

        final boolean isServiceRunning = DeviceUtils
                .isServiceRunning(context, HarvesterService.class);

        DbPowerStateEvent powerStateEvent = new DbPowerStateEvent();

        // default
        powerStateEvent.setState(PowerChargingType.NONE);
        powerStateEvent.setChargingStatus(PowerChargingStatus.NONE);
        powerStateEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        boolean isAc = BatteryUtils.isPluggedInWithAc(context);
        boolean isUsb = BatteryUtils.isPluggedInWithUsb(context);
        boolean isWireless = BatteryUtils.isPluggedInWithWirelessCharger(context);

        if (isAc) {
            powerStateEvent.setState(PowerChargingType.AC_ADAPTER);
        }

        if (isUsb) {
            powerStateEvent.setState(PowerChargingType.USB);
        }

        if (isWireless) {
            powerStateEvent.setState(PowerChargingType.WIRELESS);
        }

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "Power connected");

            if (!isServiceRunning) {
                HarvesterServiceProvider.getInstance(context).startSensingService();
            }
        }

        if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Power disconnected");

            powerStateEvent.setState(PowerChargingType.NONE);
        }

        if (Intent.ACTION_BATTERY_LOW.equals(action)) {
            Log.d(TAG, "Remaining battery is really low");

            if (isServiceRunning) {
                HarvesterServiceProvider.getInstance(context).stopSensingService();
            }

            powerStateEvent.setChargingStatus(PowerChargingStatus.LOW);
        }

        if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
            Log.d(TAG, "Remaining battery is OKAY");

            if (!isServiceRunning) {
                HarvesterServiceProvider.getInstance(context).startSensingService();
            }

            powerStateEvent.setChargingStatus(PowerChargingStatus.OKAY);
        }

        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            Log.d(TAG, "Remaining battery is FULL");

            powerStateEvent.setChargingStatus(PowerChargingStatus.FULL);
        }

        DaoProvider.getInstance(context).getPowerStateEventDao().insert(powerStateEvent);
    }
}
