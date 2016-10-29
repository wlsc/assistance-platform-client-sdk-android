package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic;

import android.content.Context;
import android.media.AudioManager;

import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RingtoneSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class RingtoneSensor extends AbstractPeriodicSensor {

    private static final String TAG = RingtoneSensorDao.class.getSimpleName();

    private int UPDATE_INTERVAL_IN_SEC = 60;

    private AudioManager audioManager;
    private int lastRingerMode = -1;

    @Inject
    public RingtoneSensor(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
        audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void dumpData() {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        DbRingtoneSensor ringtoneEvent = new DbRingtoneSensor();

        ringtoneEvent.setMode(lastRingerMode);
        ringtoneEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
        ringtoneEvent.setDeviceId(deviceId);

        Log.d(TAG, "Insert entry");

        daoProvider.getRingtoneSensorDao().insert(ringtoneEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return SensorApiType.RINGTONE;
    }

    @Override
    public void reset() {

        lastRingerMode = -1;
    }

    @Override
    protected void getData() {

        Log.d(TAG, "getData invoked");

        if (audioManager == null) {
            Log.e(TAG, "audioManager is NULL!");
            return;
        }

        int intRingerMode = audioManager.getRingerMode();

        if (intRingerMode != lastRingerMode) {

            lastRingerMode = intRingerMode;

            dumpData();
        }
    }

    /**
     * Update intervals
     */
    @Override
    public void updateSensorInterval(Double collectionInterval) {

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = collectionInterval.intValue();

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        this.UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
        setDataIntervalInSec(newUpdateIntervalInSec);
    }
}