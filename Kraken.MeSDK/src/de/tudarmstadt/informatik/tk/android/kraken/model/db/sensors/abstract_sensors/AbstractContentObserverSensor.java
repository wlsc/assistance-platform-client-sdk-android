package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.dao.AbstractDao;
import de.tudarmstadt.informatik.tk.android.kraken.communication.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbUpdatableSensor;

public abstract class AbstractContentObserverSensor extends AbstractSensor {

    private static final String TAG = AbstractContentObserverSensor.class.getSimpleName();

    public enum Configuration {
        TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC;
    }

    // ------------------- Configuration -------------------
    private int TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC = 120;
    // -----------------------------------------------------

    private SyncTimer m_timerTask = null;
    private Timer m_timer = new Timer();
    protected Observer m_observer = new Observer();

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

            m_timer = null;
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

            if (m_timerTask != null) {
                m_timerTask.cancel();
            }

            m_timerTask = new SyncTimer();

            if (m_timer == null) {
                m_timer = new Timer(true);
            }

            m_timer.schedule(m_timerTask, TIME_TO_WAIT_BEFORE_SYNCING_IN_SEC * 1000);
        }

    }

    public AbstractContentObserverSensor(Context context) {
        super(context);
    }

    @Override
    public void stopSensor() {
        isRunning = false;
        context.getContentResolver().unregisterContentObserver(m_observer);
    }

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
        return (index == -1) ? false : (cur.getInt(cur.getColumnIndex(strColumnName)) == 1);
    }

    protected boolean checkForDifference(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null)
            return false;

        if (obj1 == null || obj2 == null)
            return true;

        return !obj1.equals(obj2);
    }

    /**
     * if it's a main element the process is stopped immediately, if the sensor is stopped.
     * but we do not stop, if it's just a sub-element. So we ensure that every main element is
     * deleted completely.
     *
     * @param map
     * @param bMainElement
     */
    protected boolean deleteRemainingEntries(HashMap<?, ? extends IDbUpdatableSensor> map, boolean bMainElement) {
        boolean bSomethingDeleted = false;
        for (IDbUpdatableSensor entry : map.values()) {
            if (bMainElement && !isRunning)
                return bSomethingDeleted;
            entry.setIsDeleted(true);
            entry.setIsNew(false);
            entry.setIsUpdated(false);
            handleDBEntry(entry, true);
            if (!bSomethingDeleted)
                bSomethingDeleted = true;
        }
        return bSomethingDeleted;
    }

    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("unchecked")
    protected <T, E extends IDbSensor> HashMap<T, E> getAllExistingEntries(Class<E> sensorClass, Method getKeyMethod) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        AbstractDao<?, Long> daoObject = getDaoEntry(sensorClass);
        List<E> list = (List<E>) daoObject.queryBuilder().list();

        HashMap<T, E> map = new HashMap<T, E>();
        for (E event : list)
            map.put((T) getKeyMethod.invoke(event, new Object[]{}), event);
        return map;
    }

    protected <T, E extends IDbUpdatableSensor> boolean checkForChange(HashMap<T, E> map, E newItem, Method getKeyMethod, Method checkForDifferenceMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        @SuppressWarnings("unchecked")
        T id = (T) getKeyMethod.invoke(newItem, new Object[]{});

        E existingItem = map.remove(id);
        if (existingItem == null) {
            newItem.setIsNew(true);
            newItem.setIsUpdated(false);
            newItem.setIsDeleted(false);
            return true;
        } else if ((Boolean) checkForDifferenceMethod.invoke(this, existingItem, newItem)) {
            newItem.setIsNew(false);
            newItem.setIsUpdated(true);
            newItem.setIsDeleted(false);
            newItem.setId(existingItem.getId());
            return true;
        }
        return false;
    }

    @Override
    public EPushType getPushType() {
        return EPushType.MANUALLY_WLAN_ONLY;
    }

    abstract protected void syncData();

}
