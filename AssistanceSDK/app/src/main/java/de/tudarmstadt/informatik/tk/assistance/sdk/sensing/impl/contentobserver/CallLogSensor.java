package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractContentObserverSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class CallLogSensor extends AbstractContentObserverSensor {

    private static final String TAG = CallLogSensor.class.getSimpleName();

    private static CallLogSensor INSTANCE;

    protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

    private List<DbCallLogSensor> events;

    private AsyncTask<Void, Void, Void> asyncTask;

    private CallLogSensor(Context context) {
        super(context);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static CallLogSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new CallLogSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        Log.d(TAG, "Insert entries");

        daoProvider.getCallLogSensorDao().insert(events);

        Log.d(TAG, "Finished");
    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }

    @Override
    public int getType() {
        return SensorApiType.CALL_LOG;
    }

    @Override
    public void reset() {

    }

    @Override
    public void startSensor() {

        asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                syncData();
                context.getContentResolver().registerContentObserver(URI_CALL_LOG, true, mObserver);

                return null;
            }

        }.execute();

        setRunning(true);
    }

    @Override
    public void stopSensor() {

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }

        asyncTask = null;

        setRunning(false);
    }

    @Override
    protected void syncData() {

        if (context == null || !isRunning()) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Permission was NOT granted!");
            setRunning(false);

            return;
        }

        long longLastKnownCallLogId = -1;

        DbCallLogSensor lastItem = daoProvider.getCallLogSensorDao().getLastCallLogEvent();

        if (lastItem != null) {
            longLastKnownCallLogId = lastItem.getCallId();
        }

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;

        try {

            cursor = cr.query(android.provider.CallLog.Calls.CONTENT_URI,
                    null,
                    CallLog.Calls._ID + ">?",
                    new String[]{String.valueOf(longLastKnownCallLogId)},
                    null);

            if (cursor == null || cursor.getCount() <= 0) {
                return;
            }

            // clear previous events
            events = new ArrayList<>(cursor.getCount());

            String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

            // Iterate over event
            while (cursor.moveToNext() && isRunning()) {

                DbCallLogSensor callLogEvent = new DbCallLogSensor();

                callLogEvent.setCallId(getLongByColumnName(cursor, CallLog.Calls._ID));
                callLogEvent.setType(getIntByColumnName(cursor, CallLog.Calls.TYPE));
                callLogEvent.setNumber(getStringByColumnName(cursor, CallLog.Calls.NUMBER));
                callLogEvent.setName(getStringByColumnName(cursor, CallLog.Calls.CACHED_NAME));
                callLogEvent.setDate(getLongByColumnName(cursor, CallLog.Calls.DATE));
                callLogEvent.setDuration(getLongByColumnName(cursor, CallLog.Calls.DURATION));
                callLogEvent.setIsNew(Boolean.TRUE);
                callLogEvent.setIsDeleted(Boolean.FALSE);
                callLogEvent.setIsUpdated(Boolean.TRUE);
                callLogEvent.setDeviceId(deviceId);
                callLogEvent.setCreated(created);

                events.add(callLogEvent);
            }

            dumpData();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}