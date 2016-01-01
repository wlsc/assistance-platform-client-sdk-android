package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver;

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

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class CallLogEvent extends AbstractContentObserverEvent {

    private static final String TAG = CallLogEvent.class.getSimpleName();

    private static CallLogEvent INSTANCE;

    protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

    private AsyncTask<Void, Void, Void> syncingTask;

    private List<DbCallLogEvent> events = new ArrayList<>();

    private CallLogEvent(Context context) {
        super(context);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static CallLogEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new CallLogEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        Log.d(TAG, "Insert entries");

        daoProvider.getCallLogEventDao().insert(events);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.CALL_LOG;
    }

    @Override
    public void reset() {

    }

    @Override
    public void startSensor() {

        syncingTask = new AsyncTask<Void, Void, Void>() {

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

        syncingTask = null;
        setRunning(false);
    }

    @Override
    protected void syncData() {

        if (context == null) {
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

        DbCallLogEvent lastItem = daoProvider.getCallLogEventDao().getLastCallLogEvent();

        if (lastItem != null) {
            longLastKnownCallLogId = lastItem.getCallId();
        }

        ContentResolver cr = context.getContentResolver();
        Cursor cur = null;

        try {

            cur = cr.query(android.provider.CallLog.Calls.CONTENT_URI,
                    null,
                    CallLog.Calls._ID + ">?",
                    new String[]{String.valueOf(longLastKnownCallLogId)},
                    null);

            if (cur == null || cur.getCount() == 0) {
                return;
            }

            // clear previous events
            events.clear();

            // Iterate over event
            while (cur.moveToNext() && isRunning()) {

                DbCallLogEvent callLogEvent = new DbCallLogEvent();

                callLogEvent.setCallId(getLongByColumnName(cur, CallLog.Calls._ID));
                callLogEvent.setType(getIntByColumnName(cur, CallLog.Calls.TYPE));
                callLogEvent.setNumber(getStringByColumnName(cur, CallLog.Calls.NUMBER));
                callLogEvent.setName(getStringByColumnName(cur, CallLog.Calls.CACHED_NAME));
                callLogEvent.setDate(getLongByColumnName(cur, CallLog.Calls.DATE));
                callLogEvent.setDuration(getLongByColumnName(cur, CallLog.Calls.DURATION));
                callLogEvent.setIsNew(Boolean.TRUE);
                callLogEvent.setIsDeleted(Boolean.FALSE);
                callLogEvent.setIsUpdated(Boolean.FALSE);
                callLogEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

                events.add(callLogEvent);
            }

            dumpData();

        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }
}