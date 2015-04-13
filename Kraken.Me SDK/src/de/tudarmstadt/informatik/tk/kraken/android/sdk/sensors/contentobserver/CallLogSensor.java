package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.contentobserver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;

import de.greenrobot.dao.Property;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorCallLog;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorCallLogDao;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractContentObserverSensor;

public class CallLogSensor extends AbstractContentObserverSensor {

	protected static final Uri URI_CALL_LOG = android.provider.CallLog.Calls.CONTENT_URI;

	public CallLogSensor(Context context) {
		super(context);
	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.SENSOR_CALLLOG;
	}

	@Override
	public void startSensor() {
		m_bIsRunning = true;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				syncData();
				m_context.getContentResolver().registerContentObserver(URI_CALL_LOG, true, m_observer);
			}
		});
		thread.setName("CallLogSensorThread");
		thread.start();
	}

	@Override
	protected void syncData() {

		if (m_daoSession == null)
			throw new IllegalAccessError("no valid daoSession available!");

		SensorCallLogDao daoObject = m_daoSession.getSensorCallLogDao();
		Property propId = SensorCallLogDao.Properties.CallId;
		SensorCallLog lastItem = daoObject.queryBuilder().orderDesc(propId).limit(1).unique();

		long longLastKnownCallLogId = -1;
		if (lastItem != null)
			longLastKnownCallLogId = lastItem.getCallId();

		ContentResolver cr = m_context.getContentResolver();
		Cursor cur = cr
				.query(android.provider.CallLog.Calls.CONTENT_URI, null, Calls._ID + ">?", new String[] { String.valueOf(longLastKnownCallLogId) }, null);
		if (cur == null || cur.getCount() == 0)
			return;

		// Iterate over event
		while (cur.moveToNext() && m_bIsRunning) {
			SensorCallLog callLog = new SensorCallLog();
			callLog.setCallId(getLongByColumnName(cur, Calls._ID));
			callLog.setType(getIntByColumnName(cur, Calls.TYPE));
			callLog.setNumber(getStringByColumnName(cur, Calls.NUMBER));
			callLog.setName(getStringByColumnName(cur, Calls.CACHED_NAME));
			callLog.setDate(getLongByColumnName(cur, Calls.DATE));
			callLog.setDuration(getLongByColumnName(cur, Calls.DURATION));
			callLog.setIsNew(true);
			callLog.setIsDeleted(false);
			callLog.setIsUpdated(false);
			handleDatabaseObject(callLog, false, true, false);
		}
		cur.close();

        String strFullqualifiedDatabaseClassName = getSensorType().getFullqualifiedDatabaseClassName();
        //SensorData callLogs = flushData(m_daoSession, strFullqualifiedDatabaseClassName);
        //ServerPushManager.getInstance(m_context).flushManually(callLogs);
        ApiMessage.DataWrapper callLogs = flushDataRetro(strFullqualifiedDatabaseClassName);
        if (callLogs != null) {
            RetroServerPushManager.getInstance(m_context).flushManually(getPushType(), callLogs);
        }
    }
}
