package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.android.kraken.utils.DateUtils;

public class AccelerometerSensor extends AbstractTriggeredSensor implements SensorEventListener {

    public enum Configuration {
        SENSOR_DELAY_BETWEEN_TWO_EVENTS, AGGREGATION_TIME_IN_SEC
    }

    // ------------------- Configuration -------------------
    private int SENSOR_DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private int AGGREGATION_TIME_IN_SEC = 60;
    // -----------------------------------------------------

    private SensorManager m_sensorManager;
    private Sensor m_accelerometerSensor;

    private long m_longStartTimestamp = 0;
    private float m_floatSumAccelerationX = 0;
    private float m_floatSumAccelerationY = 0;
    private float m_floatSumAccelerationZ = 0;

    private double x;
    private double y;
    private double z;

    private int accuracy = 0;
    private int m_intNumValues = 0;

    private static DbAccelerometerSensorDao accelerometerSensorDao;

    public AccelerometerSensor(Context context) {
        super(context);

        m_sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        m_accelerometerSensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometerSensorDao == null) {
            accelerometerSensorDao = mDaoSession.getDbAccelerometerSensorDao();
        }
    }

    @Override
    protected void dumpData() {

        DbAccelerometerSensor dbAccelerometerSensor = new DbAccelerometerSensor();

        dbAccelerometerSensor.setX(x);
        dbAccelerometerSensor.setY(y);
        dbAccelerometerSensor.setZ(z);
        dbAccelerometerSensor.setAccuracy(accuracy);
        dbAccelerometerSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        accelerometerSensorDao.insert(dbAccelerometerSensor);
    }

    @Override
    public void startSensor() {
        reset();
        m_sensorManager.registerListener(this, m_accelerometerSensor, SENSOR_DELAY_BETWEEN_TWO_EVENTS);
        isRunning = true;
    }

    @Override
    public void stopSensor() {
        m_sensorManager.unregisterListener(this, m_accelerometerSensor);
        isRunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sendCurrentSeries();
        reset();
        this.accuracy = accuracy;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean bValueAdded = addNewValueToAverage(event, false);

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if (!bValueAdded) {
            sendCurrentSeries();
            addNewValueToAverage(event, true);
        }
    }

    private boolean addNewValueToAverage(SensorEvent event, boolean newSeries) {
        if (newSeries) {
            m_longStartTimestamp = event.timestamp;
            m_floatSumAccelerationX = Math.abs(event.values[0]);
            m_floatSumAccelerationY = Math.abs(event.values[1]);
            m_floatSumAccelerationZ = Math.abs(event.values[2]);
            m_intNumValues = 1;
            return true;
        } else if (event.timestamp < m_longStartTimestamp + AGGREGATION_TIME_IN_SEC * 1000000000L) {
            m_floatSumAccelerationX += Math.abs(event.values[0]);
            m_floatSumAccelerationY += Math.abs(event.values[1]);
            m_floatSumAccelerationZ += Math.abs(event.values[2]);
            m_intNumValues++;
            return true;
        }
        return false;
    }

    @Override
    public void reset() {

        m_longStartTimestamp = 0;
        m_floatSumAccelerationX = 0;
        m_floatSumAccelerationY = 0;
        m_floatSumAccelerationZ = 0;
        accuracy = 0;
        m_intNumValues = 0;
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

    private void sendCurrentSeries() {
        if (m_intNumValues > 0) {
//			SensorAccelerometer sensorAcc = new SensorAccelerometer();
//			sensorAcc.setAccuracy(accuracy);
//			sensorAcc.setAccelerationX(m_floatSumAccelerationX / m_intNumValues);
//			sensorAcc.setAccelerationY(m_floatSumAccelerationY / m_intNumValues);
//			sensorAcc.setAccelerationZ(m_floatSumAccelerationZ / m_intNumValues);
//			handleDBEntry(sensorAcc);
        }

    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.ACCELEROMETER_SENSOR;
    }

}
