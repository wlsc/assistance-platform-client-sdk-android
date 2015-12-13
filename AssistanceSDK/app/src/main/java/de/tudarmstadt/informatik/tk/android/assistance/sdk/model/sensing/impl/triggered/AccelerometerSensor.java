package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class AccelerometerSensor extends
        AbstractTriggeredEvent implements
        SensorEventListener {

    private static final String TAG = AccelerometerSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int UPDATE_INTERVAL_IN_SEC = 5;
    // -----------------------------------------------------

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private long startTimestamp;
    private float sumAccelerationX;
    private float sumAccelerationY;
    private float sumAccelerationZ;
    private int accuracy;
    private int numValues;

    public AccelerometerSensor(Context context) {
        super(context);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void dumpData() {

        if (numValues > 0) {

            DbAccelerometerSensor dbAccelerometerSensor = new DbAccelerometerSensor();

            dbAccelerometerSensor.setX((double) (sumAccelerationX / numValues));
            dbAccelerometerSensor.setY((double) (sumAccelerationY / numValues));
            dbAccelerometerSensor.setZ((double) (sumAccelerationZ / numValues));
            dbAccelerometerSensor.setAccuracy(accuracy);
            dbAccelerometerSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

            Log.d(TAG, "Insert entry");

            daoProvider.getAccelerometerSensorDao().insert(dbAccelerometerSensor);

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
        return DtoType.ACCELEROMETER;
    }

}