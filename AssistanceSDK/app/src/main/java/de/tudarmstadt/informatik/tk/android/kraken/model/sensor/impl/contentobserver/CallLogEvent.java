package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver;

import android.content.Context;
import android.net.Uri;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;

public class CallLogEvent extends AbstractContentObserverEvent {

    protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

    public CallLogEvent(Context context) {
        super(context);
    }

    @Override
    protected void dumpData() {

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
        isRunning = true;
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

        if (mDaoSession == null) {
            mDaoSession = DbProvider.getInstance(context).getDaoSession();
        }
        //throw new IllegalAccessError("no valid daoSession available!");

//		SensorCallLogDao daoObject = mDaoSession.getSensorCallLogDao();
//		Property propId = SensorCallLogDao.Properties.CallId;
//		SensorCallLog lastItem = daoObject.queryBuilder().orderDesc(propId).limit(1).unique();

//		long longLastKnownCallLogId = -1;
//		if (lastItem != null)
//			longLastKnownCallLogId = lastItem.getCallId();
//
//		ContentResolver cr = context.getContentResolver();
//		Cursor cur = cr
//				.query(android.provider.CallLog.Calls.CONTENT_URI, null, Calls._ID + ">?", new String[] { String.valueOf(longLastKnownCallLogId) }, null);
//		if (cur == null || cur.getCount() == 0)
//			return;
//
//		// Iterate over event
//		while (cur.moveToNext() && isRunning) {
//			SensorCallLog callLog = new SensorCallLog();
//			callLog.setCallId(getLongByColumnName(cur, Calls._ID));
//			callLog.setType(getIntByColumnName(cur, Calls.TYPE));
//			callLog.setNumber(getStringByColumnName(cur, Calls.NUMBER));
//			callLog.setName(getStringByColumnName(cur, Calls.CACHED_NAME));
//			callLog.setDate(getLongByColumnName(cur, Calls.DATE));
//			callLog.setDuration(getLongByColumnName(cur, Calls.DURATION));
//			callLog.setIsNew(true);
//			callLog.setIsDeleted(false);
//			callLog.setIsUpdated(false);
//			handleDBEntry(callLog, false, true, false);
//		}
//		cur.close();

//        String strFullqualifiedDatabaseClassName = getType().getFullqualifiedDatabaseClassName();
//        //SensorData callLogs = flushData(mDaoSession, strFullqualifiedDatabaseClassName);
//        //ServerPushManager.getInstance(context).flushManually(callLogs);
//        ApiMessage.DataWrapper callLogs = flushDataRetro(strFullqualifiedDatabaseClassName);
//        if (callLogs != null) {
//            RetroServerPushManager.getInstance(context).flushManually(getPushType(), callLogs);
//        }
    }
}
