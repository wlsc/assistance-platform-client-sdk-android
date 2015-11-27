package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;
import android.media.AudioManager;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRingtoneEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.RingtoneEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RingtoneEvent extends AbstractPeriodicEvent implements ISensor {

    private static final String TAG = RingtoneEventDao.class.getSimpleName();

    private static final int INIT_DATA_INTERVALL = 60;
    private AudioManager audioManager;
    private int lastRingerMode = -1;

    public RingtoneEvent(Context context) {
        super(context);

        setDataIntervalInSec(INIT_DATA_INTERVALL);
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

}
