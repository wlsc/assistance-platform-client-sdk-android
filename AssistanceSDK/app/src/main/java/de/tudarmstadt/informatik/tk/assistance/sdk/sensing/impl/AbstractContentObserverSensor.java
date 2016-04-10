package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

public abstract class AbstractContentObserverSensor extends AbstractSensor {

    private static final String TAG = AbstractContentObserverSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC = 120;
    // -----------------------------------------------------

    private SyncTimer mTimerTask;
    private Timer mTimer = new Timer();
    protected Observer mObserver = new Observer();

    private class SyncTimer extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "Syncing now...");

            mDaoSession.runInTx(AbstractContentObserverSensor.this::syncData);

            mTimer = null;
        }
    }

    public AbstractContentObserverSensor(Context context) {
        super(context);
    }

    @Override
    public void stopSensor() {

        try {

            if (context != null && mObserver != null) {
                context.getContentResolver().unregisterContentObserver(mObserver);
            }

        } finally {
            setRunning(false);
        }
    }

    @Nullable
    protected String getStringByColumnName(Cursor cur, String strColumnName) {
        int index = cur.getColumnIndex(strColumnName);
        return (index == -1) ? null : cur.getString(cur.getColumnIndex(strColumnName));
    }

    protected long getLongByColumnName(Cursor cur, String strColumnName) {
        int index = cur.getColumnIndex(strColumnName);
        return (index == -1) ? 0 : cur.getLong(cur.getColumnIndex(strColumnName));
    }

    protected int getIntByColumnName(Cursor cur, String strColumnName) {
        int index = cur.getColumnIndex(strColumnName);
        return (index == -1) ? 0 : cur.getInt(cur.getColumnIndex(strColumnName));
    }

    protected boolean getBoolByColumnName(Cursor cur, String strColumnName) {
        int index = cur.getColumnIndex(strColumnName);
        return (index != -1) && (cur.getInt(cur.getColumnIndex(strColumnName)) == 1);
    }

    protected boolean checkForDifference(Object obj1, Object obj2) {
        return !(obj1 == null && obj2 == null) && (obj1 == null || obj2 == null || !obj1.equals(obj2));
    }

    @Override
    public EPushType getPushType() {
        return EPushType.MANUALLY_WLAN_ONLY;
    }

    protected abstract void syncData();

    private class Observer extends ContentObserver {

        public Observer() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {

            Log.d(TAG, "change arrived!");

            if (mTimerTask != null) {
                mTimerTask.cancel();
            }

            mTimerTask = new SyncTimer();

            if (mTimer == null) {
                mTimer = new Timer(true);
            }

            mTimer.schedule(mTimerTask, TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC * 1_000);
        }
    }

}
