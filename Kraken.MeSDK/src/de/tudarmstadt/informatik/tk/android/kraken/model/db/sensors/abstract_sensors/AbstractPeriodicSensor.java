package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPeriodicSensor extends AbstractSensor {

    private ScheduledExecutorService m_scheduledTaskExecutor;
    private Future<?> m_future;
    protected boolean m_bLooperPrepared = false;


    public enum Configuration {
        DATA_INTERVAL_IN_SEC;
    }

    // ------------------- Configuration -------------------
    private int DATA_INTERVAL_IN_SEC = 600;
    // -----------------------------------------------------

    public AbstractPeriodicSensor(Context context) {
        super(context);
        m_scheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void startSensor() {
        if (m_future == null) {
            m_future = m_scheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    try {
                        getData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, getDataIntervallInSec(), TimeUnit.SECONDS);
        }
        isRunning = true;
    }

    @Override
    public void stopSensor() {
        if (m_future != null) {
            m_future.cancel(true);
            m_future = null;
        }
        isRunning = false;
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
