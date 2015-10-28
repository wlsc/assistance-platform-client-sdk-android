package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractContentObserverEvent;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class CallLogEvent extends AbstractContentObserverEvent {

    protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

    public CallLogEvent(Context context) {
        super(context);
    }

    @Override
    public void dumpData() {

    }

    @Override
    public int getType() {
        return SensorType.CALL_LOG;
    }

    @Override
    public void reset() {

    }

    @Override
    public void startSensor() {

        setRunning(true);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                syncData();
                context.getContentResolver().registerContentObserver(URI_CALL_LOG, true, mObserver);
            }
        });

        thread.setName("CallLogSensorThread");
        thread.start();
    }

    @Override
    protected void syncData() {

        if (context == null) {
            return;
        }

        long longLastKnownCallLogId = -1;

        DbCallLogEvent lastItem = dbProvider.getLastCallLogEvent();

        if (lastItem != null) {
            longLastKnownCallLogId = lastItem.getCallId();
        }

        Cursor cur = null;

        try {

            ContentResolver cr = context.getContentResolver();

            cur = cr.query(android.provider.CallLog.Calls.CONTENT_URI,
                    null,
                    CallLog.Calls._ID + ">?",
                    new String[]{String.valueOf(longLastKnownCallLogId)},
                    null);

            if (cur == null || cur.getCount() == 0) {
                return;
            }

            // Iterate over event
            while (cur.moveToNext() && isRunning()) {

                DbCallLogEvent callLogEvent = new DbCallLogEvent();

                callLogEvent.setCallId(getLongByColumnName(cur, CallLog.Calls._ID));
                callLogEvent.setType(getIntByColumnName(cur, CallLog.Calls.TYPE));
                callLogEvent.setNumber(getStringByColumnName(cur, CallLog.Calls.NUMBER));
                callLogEvent.setName(getStringByColumnName(cur, CallLog.Calls.CACHED_NAME));
                callLogEvent.setDate(getLongByColumnName(cur, CallLog.Calls.DATE));
                callLogEvent.setDuration(getLongByColumnName(cur, CallLog.Calls.DURATION));
                callLogEvent.setIsNew(true);
                callLogEvent.setIsDeleted(false);
                callLogEvent.setIsUpdated(false);

                dbProvider.insertEventEntry(callLogEvent, getType());
            }
        } finally {
            cur.close();
        }
    }
}
