package de.tudarmstadt.informatik.tk.android.kraken.model.sensor;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPeriodicEvent extends AbstractSensor {

    private static final String TAG = AbstractPeriodicEvent.class.getSimpleName();

    private ScheduledExecutorService mScheduledTaskExecutor;
    private Future<?> m_future;
    protected boolean m_bLooperPrepared;

    // ------------------- Configuration -------------------
    private int DATA_INTERVAL_IN_SEC = 600;
    // -----------------------------------------------------

    public AbstractPeriodicEvent(Context context) {
        super(context);
        mScheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void startSensor() {

        if (m_future == null) {
            m_future = mScheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    try {
                        getData();
                    } catch (Exception e) {
                        Log.e(TAG, "Cannot get data for sensor! Error: ", e);
                    }
                }
            }, 0, getDataIntervallInSec(), TimeUnit.SECONDS);
        }

        setRunning(true);
    }

    @Override
    public void stopSensor() {

        try {
            if (m_future != null) {
                m_future.cancel(true);
                m_future = null;
            }
        } finally {
            setRunning(false);
        }
    }

    abstract protected void getData();

    protected int getDataIntervallInSec() {
        return DATA_INTERVAL_IN_SEC;
    }

    protected void setDataIntervallInSec(int sec) {
        DATA_INTERVAL_IN_SEC = sec;
        if (m_future != null) {
            stopSensor();
            startSensor();
        }
    }
}
