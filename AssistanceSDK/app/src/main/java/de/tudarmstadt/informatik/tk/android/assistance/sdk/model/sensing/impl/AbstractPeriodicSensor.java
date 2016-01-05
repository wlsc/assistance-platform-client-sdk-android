package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public abstract class AbstractPeriodicSensor extends AbstractSensor {

    private static final String TAG = AbstractPeriodicSensor.class.getSimpleName();

    private ScheduledExecutorService mScheduledTaskExecutor;
    private Future<?> mFuture;
    protected boolean mLooperPrepared;

    // ------------------- Configuration -------------------
    private int UPDATE_INTERVAL_IN_SEC = 600;
    // -----------------------------------------------------

    public AbstractPeriodicSensor(Context context) {
        super(context);
        mScheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void startSensor() {

        if (!isRunning()) {

            if (mFuture == null) {
                mFuture = mScheduledTaskExecutor.scheduleAtFixedRate(() -> {
                    try {
                        getData();
                    } catch (Exception e) {
                        Log.e(TAG, "Cannot get data for sensor! Error: ", e);
                    }
                }, 0, getDataIntervalInSec(), TimeUnit.SECONDS);
            }

            setRunning(true);
        }
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

    protected int getDataIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    protected void setDataIntervalInSec(int sec) {

        UPDATE_INTERVAL_IN_SEC = sec;
        
        if (mFuture != null) {
            stopSensor();
            startSensor();
        }
    }
}
