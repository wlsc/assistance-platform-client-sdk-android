package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;
import android.media.AudioManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRingtoneEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.RingtoneEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RingtoneEvent extends AbstractPeriodicEvent {

    private static final String TAG = RingtoneEventDao.class.getSimpleName();

    private int UPDATE_INTERVAL_IN_SEC = 60;

    private AudioManager audioManager;
    private int lastRingerMode = -1;

    public RingtoneEvent(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
        audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void dumpData() {

        DbRingtoneEvent ringtoneEvent = new DbRingtoneEvent();

        ringtoneEvent.setMode(lastRingerMode);
        ringtoneEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getRingtoneEventDao().insert(ringtoneEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.RINGTONE;
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
     *
     * @param event
     */
    @Override
    public void onEvent(UpdateSensorIntervalEvent event) {

        // only accept this sensor topic type
        if (event.getTopic() != getType()) {
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