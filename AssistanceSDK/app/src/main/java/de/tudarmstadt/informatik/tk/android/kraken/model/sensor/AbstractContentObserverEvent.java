package de.tudarmstadt.informatik.tk.android.kraken.model.sensor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.EPushType;

public abstract class AbstractContentObserverEvent extends AbstractSensor {

    private static final String TAG = AbstractContentObserverEvent.class.getSimpleName();

    // ------------------- Configuration -------------------
    private int TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC = 120;
    // -----------------------------------------------------

    private SyncTimer mTimerTask = null;
    private Timer mTimer = new Timer();
    protected Observer mObserver = new Observer();

    private class SyncTimer extends TimerTask {
        @Override
        public void run() {
            Log.d(TAG, "Syncing now...");

            mDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    syncData();
                }
            });

            mTimer = null;
        }
    }

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

            mTimer.schedule(mTimerTask, TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC * 1000);
        }
    }

    public AbstractContentObserverEvent(Context context) {
        super(context);
    }

    @Override
    public void stopSensor() {
        isRunning = false;
        context.getContentResolver().unregisterContentObserver(mObserver);
    }

    protected String getStringByColumnName(Cursor cur, String strColumnName) {
        int index = cur.getColumnIndex(strColumnName);
        return (index == -1) ? null : cur.getString(cur.getColumnIndex(strColumnName));
    }

    @Override
    public EPushType getPushType() {
        return EPushType.MANUALLY_WLAN_ONLY;
    }

    abstract protected void syncData();

}
