package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * Provides event for calibrated and uncalibrated gyroscope sensor
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class GyroscopeSensor extends
        AbstractTriggeredSensor implements
        SensorEventListener {

    private static final String TAG = GyroscopeSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int UPDATE_INTERVAL_IN_SEC = 10;
    // -----------------------------------------------------

    private static GyroscopeSensor INSTANCE;

    private long mLastEventDumpingTimestamp;    // in nanoseconds

    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;
    private Sensor mGyroscopeUncalibratedSensor;

    private double x;
    private double y;
    private double z;
    private int accuracy;

    private float xUncalibratedNoDrift;
    private float yUncalibratedNoDrift;
    private float zUncalibratedNoDrift;
    private float xUncalibratedEstimatedDrift;
    private float yUncalibratedEstimatedDrift;
    private float zUncalibratedEstimatedDrift;

    private GyroscopeSensor(Context context) {
        super(context);

        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        mGyroscopeSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mGyroscopeUncalibratedSensor = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        }
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static GyroscopeSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new GyroscopeSensor(context);
        }

        return INSTANCE;
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // serve only this type
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // updating values
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }

        // serve only this type
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {

            // updating values
            xUncalibratedNoDrift = event.values[0];
            yUncalibratedNoDrift = event.values[1];
            zUncalibratedNoDrift = event.values[2];
            xUncalibratedEstimatedDrift = event.values[3];
            yUncalibratedEstimatedDrift = event.values[4];
            zUncalibratedEstimatedDrift = event.values[5];
        }

        // checks for saving new data
        if (isTimeToSaveData(event.timestamp)) {

            mLastEventDumpingTimestamp = event.timestamp;

            // time to dump/save data into db
            dumpData();
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE ||
                sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {

            Log.d(TAG, "Accuracy has changed. Old: " + this.accuracy + " new: " + accuracy);

            this.accuracy = accuracy;

            // checks for saving new data
            if (isTimeToSaveData(System.nanoTime())) {

                mLastEventDumpingTimestamp = System.nanoTime();

                dumpData();
            }
        }
    }

    @Override
    public void startSensor() {

        if (mSensorManager != null) {

            if (mGyroscopeSensor != null) {
                mSensorManager.registerListener(this,
                        mGyroscopeSensor,
                        DELAY_BETWEEN_TWO_EVENTS);
            }

            if (mGyroscopeUncalibratedSensor != null) {
                mSensorManager.registerListener(this,
                        mGyroscopeUncalibratedSensor,
                        DELAY_BETWEEN_TWO_EVENTS);
            }

            if (mGyroscopeSensor != null || mGyroscopeUncalibratedSensor != null) {
                setRunning(true);
            }
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (mSensorManager != null) {
                if (mGyroscopeSensor != null) {
                    mSensorManager.unregisterListener(this, mGyroscopeSensor);
                }
                if (mGyroscopeUncalibratedSensor != null) {
                    mSensorManager.unregisterListener(this, mGyroscopeUncalibratedSensor);
                }
            }
        } finally {
            setRunning(false);
        }
    }

    @Override
    public int getType() {
        return SensorApiType.GYROSCOPE;
    }

    @Override
    public void reset() {

        mLastEventDumpingTimestamp = 0;
        x = 0;
        y = 0;
        z = 0;
        accuracy = 0;

        xUncalibratedNoDrift = 0.0f;
        yUncalibratedNoDrift = 0.0f;
        zUncalibratedNoDrift = 0.0f;
        xUncalibratedEstimatedDrift = 0.0f;
        yUncalibratedEstimatedDrift = 0.0f;
        zUncalibratedEstimatedDrift = 0.0f;
    }

    @Override
    public void dumpData() {

        DbGyroscopeSensor gyroscopeSensor = new DbGyroscopeSensor();

        /**
         * Calibrated data
         */

        if (x != 0) {
            gyroscopeSensor.setX(x);
        }

        if (y != 0) {
            gyroscopeSensor.setY(y);
        }

        if (z != 0) {
            gyroscopeSensor.setZ(z);
        }

        /**
         * Uncalibrated data
         */

        if (xUncalibratedNoDrift != 0.0f) {
            gyroscopeSensor.setXUncalibratedNoDrift(xUncalibratedNoDrift);
        }

        if (yUncalibratedNoDrift != 0.0f) {
            gyroscopeSensor.setYUncalibratedNoDrift(yUncalibratedNoDrift);
        }

        if (zUncalibratedNoDrift != 0.0f) {
            gyroscopeSensor.setZUncalibratedNoDrift(zUncalibratedNoDrift);
        }

        if (xUncalibratedEstimatedDrift != 0.0f) {
            gyroscopeSensor.setXUncalibratedEstimatedDrift(xUncalibratedEstimatedDrift);
        }

        if (yUncalibratedEstimatedDrift != 0.0f) {
            gyroscopeSensor.setYUncalibratedEstimatedDrift(yUncalibratedEstimatedDrift);
        }

        if (zUncalibratedEstimatedDrift != 0.0f) {
            gyroscopeSensor.setZUncalibratedEstimatedDrift(zUncalibratedEstimatedDrift);
        }

        gyroscopeSensor.setAccuracy(accuracy);
        gyroscopeSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getGyroscopeSensorDao().insert(gyroscopeSensor);

        Log.d(TAG, "Finished");
    }

    /**
     * Update intervals
     */
    @Override
    public void updateSensorInterval(Double collectionInterval) {

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = collectionInterval.intValue();

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        this.UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
    }

    public int getUpdateIntervalInSec() {
        return this.UPDATE_INTERVAL_IN_SEC;
    }

    public void setUpdateIntervalInSec(int updateIntervalInSec) {
        this.UPDATE_INTERVAL_IN_SEC = updateIntervalInSec;
    }

    /**
     * Checks for the time to save new sensor reading into db
     *
     * @param timestamp
     * @return
     */
    private boolean isTimeToSaveData(long timestamp) {

        // save the first sensor data
        if (mLastEventDumpingTimestamp == 0) {
            return true;
        } else {

            // the time has come -> save data into db
            if ((timestamp - mLastEventDumpingTimestamp) / 1_000_000_000 > UPDATE_INTERVAL_IN_SEC) {
                return true;
            }
        }

        return false;
    }
}
