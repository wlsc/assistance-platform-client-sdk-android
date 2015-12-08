package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelEvent;
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

    private static final int INIT_DATA_INTERVAL_IN_SEC = 900;

    private float lastPercentValue;

    public PowerLevelEvent(Context context) {
        super(context);

        setDataIntervalInSec(INIT_DATA_INTERVAL_IN_SEC);
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
}
