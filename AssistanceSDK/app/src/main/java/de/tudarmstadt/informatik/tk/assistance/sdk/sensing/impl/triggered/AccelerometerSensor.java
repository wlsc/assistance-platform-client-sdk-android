package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public final class AccelerometerSensor extends
        AbstractTriggeredSensor implements
        SensorEventListener {

    private static final String TAG = AccelerometerSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int UPDATE_INTERVAL_IN_SEC = 5;
    // -----------------------------------------------------

    private static AccelerometerSensor INSTANCE;

    private final SensorManager mSensorManager;
    private final Sensor mSensor;

    private long startTimestamp;
    private float sumAccelerationX;
    private float sumAccelerationY;
    private float sumAccelerationZ;
    private int accuracy;
    private int numValues;

    private AccelerometerSensor(Context context) {
        super(context);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static AccelerometerSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new AccelerometerSensor(context);
        }

        return INSTANCE;
    }

    @Override

    public void dumpData() {

        if (numValues > 0) {

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbAccelerometerSensor sensor = new DbAccelerometerSensor();

            sensor.setX((double) (sumAccelerationX / numValues));
            sensor.setY((double) (sumAccelerationY / numValues));
            sensor.setZ((double) (sumAccelerationZ / numValues));
            sensor.setAccuracy(accuracy);
            sensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
            sensor.setDeviceId(deviceId);

            Log.d(TAG, "Insert entry");

            daoProvider.getAccelerometerSensorDao().insert(sensor);

            Log.d(TAG, "Finished");
        }
    }

    @Override
    public void startSensor() {

        try {
            if (mSensorManager != null) {
                if (mSensor != null) {

                    reset();

                    mSensorManager.registerListener(this, mSensor, DELAY_BETWEEN_TWO_EVENTS);
                    setRunning(true);

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Some error: ", e);
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (mSensorManager != null && mSensor != null) {
                mSensorManager.unregisterListener(this, mSensor);
            }
        } finally {
            setRunning(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        // serve only this type
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            Log.d(TAG, "Accuracy has changed. Old: " + this.accuracy + " new: " + accuracy);

            dumpData();
            reset();
            this.accuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // serve only this type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            boolean isValueAdded = addNewValueToAverage(event, false);

            if (!isValueAdded) {

                dumpData();
                addNewValueToAverage(event, true);
            }
        }
    }

    /**
     * Adds new value after summation old ones
     *
     * @param event
     * @param newSeries
     * @return
     */
    private boolean addNewValueToAverage(SensorEvent event, boolean newSeries) {

        if (newSeries) {

            startTimestamp = event.timestamp;
            sumAccelerationX = Math.abs(event.values[0]);
            sumAccelerationY = Math.abs(event.values[1]);
            sumAccelerationZ = Math.abs(event.values[2]);
            numValues = 1;

            return true;
        } else {
            if (event.timestamp < (startTimestamp + UPDATE_INTERVAL_IN_SEC * 1_000_000_000l)) {

                sumAccelerationX += Math.abs(event.values[0]);
                sumAccelerationY += Math.abs(event.values[1]);
                sumAccelerationZ += Math.abs(event.values[2]);
                numValues++;

                return true;
            }
        }

        return false;
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

        UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
    }

    public int getUpdateIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    public void setUpdateIntervalInSec(int updateIntervalInSec) {
        UPDATE_INTERVAL_IN_SEC = updateIntervalInSec;
    }

    @Override
    public void reset() {

        this.startTimestamp = 0;
        this.sumAccelerationX = 0.0f;
        this.sumAccelerationY = 0.0f;
        this.sumAccelerationZ = 0.0f;
        this.accuracy = 0;
        this.numValues = 0;
    }

    @Override
    public int getType() {
        return SensorApiType.ACCELEROMETER;
    }

}