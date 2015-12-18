package de.tudarmstadt.informatik.tk.android.assistance.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.constant.PowerChargingStatus;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.constant.PowerChargingType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.HarvesterServiceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.service.HarvesterService;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.BatteryUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.ServiceUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStatusReceiver extends BroadcastReceiver {

    private static final String TAG = PowerStatusReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (context == null) {
            return;
        }

        final String action = intent.getAction();
        Log.d(TAG, "Power status has changed. Type: " + action);

        final boolean isServiceRunning = ServiceUtils
                .isServiceRunning(context.getApplicationContext(), HarvesterService.class);

        DbPowerStateEvent powerStateEvent = new DbPowerStateEvent();

        // default
        powerStateEvent.setChargingState(PowerChargingStatus.NONE);
        powerStateEvent.setPercent(BatteryUtils.getBatteryPercentage(context.getApplicationContext()));
        powerStateEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        boolean isAc = BatteryUtils.isPluggedInWithAc(context.getApplicationContext());
        boolean isUsb = BatteryUtils.isPluggedInWithUsb(context.getApplicationContext());
        boolean isWireless = BatteryUtils.isPluggedInWithWirelessCharger(context.getApplicationContext());

        if (isAc) {
            powerStateEvent.setChargingMode(PowerChargingType.AC_ADAPTER);
            powerStateEvent.setIsCharging(Boolean.TRUE);
        }

        if (isUsb) {
            powerStateEvent.setChargingMode(PowerChargingType.USB);
            powerStateEvent.setIsCharging(Boolean.TRUE);
        }

        if (isWireless) {
            powerStateEvent.setChargingMode(PowerChargingType.WIRELESS);
            powerStateEvent.setIsCharging(Boolean.TRUE);
        }

        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "Power connected");

            if (!isServiceRunning && ServiceUtils.isHarvesterAbleToRun(context.getApplicationContext())) {
                HarvesterServiceProvider
                        .getInstance(context.getApplicationContext())
                        .startSensingService();
            }

            // TODO: add cable malfunction state
        }

        if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "Power disconnected");

            powerStateEvent.setChargingMode(null);
        }

        if (Intent.ACTION_BATTERY_LOW.equals(action)) {
            Log.d(TAG, "Remaining battery is really low");

            if (isServiceRunning && ServiceUtils.isHarvesterAbleToRun(context.getApplicationContext())) {
                HarvesterServiceProvider
                        .getInstance(context.getApplicationContext())
                        .stopSensingService();
            }

            powerStateEvent.setChargingState(PowerChargingStatus.LOW);
        }

        if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
            Log.d(TAG, "Remaining battery is OKAY");

            if (!isServiceRunning && ServiceUtils.isHarvesterAbleToRun(context.getApplicationContext())) {
                HarvesterServiceProvider
                        .getInstance(context.getApplicationContext())
                        .startSensingService();
            }

            powerStateEvent.setChargingState(PowerChargingStatus.OKAY);
        }

        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            Log.d(TAG, "Remaining battery is FULL");

            powerStateEvent.setChargingState(PowerChargingStatus.FULL);
        }

        Log.d(TAG, "Insert entry");

        DaoProvider.getInstance(context.getApplicationContext())
                .getPowerStateEventDao()
                .insert(powerStateEvent);

        Log.d(TAG, "Finished");
    }
}
