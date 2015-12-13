package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class MagneticFieldSensor extends AbstractTriggeredEvent implements SensorEventListener {

    private static final String TAG = MagneticFieldSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int UPDATE_INTERVAL_IN_SEC = 10;
    // -----------------------------------------------------

    private static MagneticFieldSensor INSTANCE;

    private long mLastEventDumpingTimestamp;    // in nanoseconds

    private SensorManager mSensorManager;
    private Sensor mMagneticFieldSensor;
    private Sensor mMagneticFieldUncalibratedSensor;

    private double x;
    private double y;
    private double z;
    private int accuracy;

    private float xUncalibratedNoHardIron;
    private float yUncalibratedNoHardIron;
    private float zUncalibratedNoHardIron;
    private float xUncalibratedEstimatedIronBias;
    private float yUncalibratedEstimatedIronBias;
    private float zUncalibratedEstimatedIronBias;

    private MagneticFieldSensor(Context context) {
        super(context);

        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        mMagneticFieldSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mMagneticFieldUncalibratedSensor = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        }
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static MagneticFieldSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new MagneticFieldSensor(context);
        }

        return INSTANCE;
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
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

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            // updating values
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {

            // updating values
            xUncalibratedNoHardIron = event.values[0];
            yUncalibratedNoHardIron = event.values[1];
            zUncalibratedNoHardIron = event.values[2];
            xUncalibratedEstimatedIronBias = event.values[3];
            yUncalibratedEstimatedIronBias = event.values[4];
            zUncalibratedEstimatedIronBias = event.values[5];
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
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ||
                sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {

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

            if (mMagneticFieldSensor != null) {
                mSensorManager.registerListener(this,
                        mMagneticFieldSensor,
                        DELAY_BETWEEN_TWO_EVENTS);
            }

            if (mMagneticFieldUncalibratedSensor != null) {
                mSensorManager.registerListener(this,
                        mMagneticFieldUncalibratedSensor,
                        DELAY_BETWEEN_TWO_EVENTS);
            }

            if (mMagneticFieldSensor != null || mMagneticFieldUncalibratedSensor != null) {
                setRunning(true);
            }
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (mSensorManager != null) {

                if (mMagneticFieldSensor != null) {
                    mSensorManager.unregisterListener(this, mMagneticFieldSensor);
                }

                if (mMagneticFieldUncalibratedSensor != null) {
                    mSensorManager.unregisterListener(this, mMagneticFieldUncalibratedSensor);
                }
            }
        } finally {
            setRunning(false);
        }
    }

    @Override
    public int getType() {
        return DtoType.MAGNETIC_FIELD;
    }

    @Override
    public void reset() {

        mLastEventDumpingTimestamp = 0;
        x = 0.0;
        y = 0.0;
        z = 0.0;
        accuracy = 0;

        xUncalibratedNoHardIron = 0.0f;
        yUncalibratedNoHardIron = 0.0f;
        zUncalibratedNoHardIron = 0.0f;
        xUncalibratedEstimatedIronBias = 0.0f;
        yUncalibratedEstimatedIronBias = 0.0f;
        zUncalibratedEstimatedIronBias = 0.0f;
    }

    @Override
    public void dumpData() {

        DbMagneticFieldSensor magneticFieldSensor = new DbMagneticFieldSensor();

        /**
         * Calibrated data
         */

        if (x != 0.0) {
            magneticFieldSensor.setX(x);
        }

        if (y != 0.0) {
            magneticFieldSensor.setY(y);
        }

        if (z != 0.0) {
            magneticFieldSensor.setZ(z);
        }

        /**
         * Uncalibrated data
         */
        if (xUncalibratedNoHardIron != 0.0f) {
            magneticFieldSensor.setXUncalibratedNoHardIron(xUncalibratedNoHardIron);
        }

        if (yUncalibratedNoHardIron != 0.0f) {
            magneticFieldSensor.setYUncalibratedNoHardIron(yUncalibratedNoHardIron);
        }

        if (zUncalibratedNoHardIron != 0.0f) {
            magneticFieldSensor.setZUncalibratedNoHardIron(zUncalibratedNoHardIron);
        }

        if (xUncalibratedEstimatedIronBias != 0.0f) {
            magneticFieldSensor.setXUncalibratedEstimatedIronBias(xUncalibratedEstimatedIronBias);
        }

        if (yUncalibratedEstimatedIronBias != 0.0f) {
            magneticFieldSensor.setYUncalibratedEstimatedIronBias(yUncalibratedEstimatedIronBias);
        }

        if (zUncalibratedEstimatedIronBias != 0.0f) {
            magneticFieldSensor.setZUncalibratedEstimatedIronBias(zUncalibratedEstimatedIronBias);
        }

        magneticFieldSensor.setAccuracy(accuracy);
        magneticFieldSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getMagneticFieldSensorDao().insert(magneticFieldSensor);

        Log.d(TAG, "Finished");
    }

    /**
     * Update intervals
     *
     * @param event
     */
    @Override
    public void onEvent(UpdateSensorIntervalEvent event) {

        // only accept this sensor topic type
        if (event.getTopic() != getType()) {
            return;
        }

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = (int) Math.round(1.0 / event.getCollectionFrequency());

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        setUpdateIntervalInSec(newUpdateIntervalInSec);
    }

    public int getUpdateIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    public void setUpdateIntervalInSec(int updateIntervalInSec) {
        UPDATE_INTERVAL_IN_SEC = updateIntervalInSec;
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
