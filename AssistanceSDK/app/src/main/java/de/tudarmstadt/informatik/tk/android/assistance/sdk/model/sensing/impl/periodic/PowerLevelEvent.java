package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.BatteryUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
public class PowerLevelEvent extends AbstractPeriodicEvent {

    private static final String TAG = PowerLevelEvent.class.getSimpleName();

    private static PowerLevelEvent INSTANCE;

    private int UPDATE_INTERVAL_IN_SEC = 900;

    private float lastPercentValue;

    private PowerLevelEvent(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static PowerLevelEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new PowerLevelEvent(context);
        }

        return INSTANCE;
    }

    @Override
    protected void getData() {

        Log.d(TAG, "getData invoked");

        lastPercentValue = BatteryUtils.getBatteryPercentage(context);

        dumpData();
    }

    @Override
    public int getType() {
        return DtoType.POWER_LEVEL;
    }

    @Override
    public void reset() {

        this.lastPercentValue = 0.0f;
    }

    @Override
    public void dumpData() {

        DbPowerLevelEvent powerLevelEvent = new DbPowerLevelEvent();

        powerLevelEvent.setPercent(lastPercentValue);
        powerLevelEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getPowerLevelEventDao().insert(powerLevelEvent);

        Log.d(TAG, "Finished");
    }

    /**
     * Update intervals
     *
     * @param event
     */
    @Override
    public void onEvent(UpdateSensorIntervalEvent event) {

        // only accept this sensor topic type
        if (event.getDtoType() != getType()) {
            return;
        }

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = (int) Math.round(1.0 / event.getCollectionFrequency());

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        this.UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
        setDataIntervalInSec(newUpdateIntervalInSec);
    }
}