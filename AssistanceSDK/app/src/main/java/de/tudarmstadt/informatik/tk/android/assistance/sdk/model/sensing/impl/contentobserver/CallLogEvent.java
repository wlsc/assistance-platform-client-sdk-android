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

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class CallLogEvent extends AbstractContentObserverEvent {

    private static final String TAG = CallLogEvent.class.getSimpleName();

    protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

    private AsyncTask<Void, Void, Void> syncingTask;

    public CallLogEvent(Context context) {
        super(context);
    }

    @Override
    public void dumpData() {

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

        long longLastKnownCallLogId = -1;

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

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

                Log.d(TAG, "Insert entry");

                daoProvider.getCallLogEventDao().insert(callLogEvent);

                Log.d(TAG, "Finished");
            }
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }
}
