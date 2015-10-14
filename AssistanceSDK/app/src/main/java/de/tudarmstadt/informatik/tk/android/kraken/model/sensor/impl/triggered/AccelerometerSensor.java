package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;

public class AccelerometerSensor extends AbstractTriggeredEvent implements SensorEventListener {

    private static final String TAG = AccelerometerSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private int SENSOR_DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int updateInterval = 10;    // in seconds
    // -----------------------------------------------------

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private DbProvider dbProvider;

    private long mLastEventDumpingTimestamp = 0;    // in nanoseconds

    private double x;
    private double y;
    private double z;
    private int accuracy = 0;

    public AccelerometerSensor(Context context) {
        super(context);

        if (dbProvider == null) {
            dbProvider = DbProvider.getInstance(context);
        }

        mSensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void dumpData() {

        DbAccelerometerSensor dbAccelerometerSensor = new DbAccelerometerSensor();

        dbAccelerometerSensor.setX(x);
        dbAccelerometerSensor.setY(y);
        dbAccelerometerSensor.setZ(z);
        dbAccelerometerSensor.setAccuracy(accuracy);
        dbAccelerometerSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        dbProvider.insertEventEntry(dbAccelerometerSensor, getType());
    }

    @Override
    public void startSensor() {
        reset();
        mSensorManager.registerListener(this, mAccelerometerSensor, SENSOR_DELAY_BETWEEN_TWO_EVENTS);
        isRunning = true;
    }

    @Override
    public void stopSensor() {
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
        isRunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        Log.d(TAG, "Accuracy changed. Old: " + this.accuracy + " new: " + accuracy);

        this.accuracy = accuracy;

        // checks for saving new data
        if (isTimeToSaveData(System.nanoTime())) {

            // accuracy has changed faster than accelerometer itself
            // ignore that accuracy
            if (x == 0 && y == 0 && z == 0) {
                return;
            }

            mLastEventDumpingTimestamp = System.nanoTime();

            dumpData();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // updating values
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        // checks for saving new data
        if (isTimeToSaveData(event.timestamp)) {

            mLastEventDumpingTimestamp = event.timestamp;

            // time to dump/save data into db
            dumpData();
        }
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
            if ((timestamp - mLastEventDumpingTimestamp) / 1000000000 > updateInterval) {
                return true;
            }
        }

        return false;
    }

//    private boolean addNewValueToAverage(SensorEvent event, boolean newSeries) {
//        if (newSeries) {
//            mCurrentEventTimestamp = event.timestamp;
//            m_floatSumAccelerationX = Math.abs(event.values[0]);
//            m_floatSumAccelerationY = Math.abs(event.values[1]);
//            m_floatSumAccelerationZ = Math.abs(event.values[2]);
//            m_intNumValues = 1;
//            return true;
//        } else if (event.timestamp < mCurrentEventTimestamp + updateInterval * 1000000000L) {
//            m_floatSumAccelerationX += Math.abs(event.values[0]);
//            m_floatSumAccelerationY += Math.abs(event.values[1]);
//            m_floatSumAccelerationZ += Math.abs(event.values[2]);
//            m_intNumValues++;
//            return true;
//        }
//        return false;
//    }

    @Override
    public void reset() {

        mLastEventDumpingTimestamp = 0;
        x = 0;
        y = 0;
        z = 0;
        accuracy = 0;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getUpdateInterval() {
        return this.updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    @Override
    public int getType() {
        return SensorType.ACCELEROMETER;
    }

}
