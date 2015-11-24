package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPeriodicEvent extends AbstractSensor {

    private static final String TAG = AbstractPeriodicEvent.class.getSimpleName();

    private ScheduledExecutorService mScheduledTaskExecutor;
    private Future<?> mFuture;
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

        if (mFuture == null) {
            mFuture = mScheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {

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
            if (mFuture != null && !mFuture.isCancelled()) {
                mFuture.cancel(true);
            }
        } finally {
            setRunning(false);
            mFuture = null;
        }
    }

    abstract protected void getData();

    protected int getDataIntervallInSec() {
        return DATA_INTERVAL_IN_SEC;
    }

    protected void setDataIntervallInSec(int sec) {
        DATA_INTERVAL_IN_SEC = sec;
        if (mFuture != null) {
            stopSensor();
            startSensor();
        }
    }
}
