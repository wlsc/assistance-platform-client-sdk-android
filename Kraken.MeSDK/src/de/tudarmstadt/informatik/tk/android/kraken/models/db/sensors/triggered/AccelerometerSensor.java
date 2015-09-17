package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractTriggeredSensor;

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

    private int m_intAccuracy = 0;
    private int m_intNumValues = 0;

    private static DbAccelerometerSensorDao accelerometerSensorDao;

    public AccelerometerSensor(Context context) {
        super(context);

        m_sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        m_accelerometerSensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometerSensorDao == null) {
            accelerometerSensorDao = m_daoSession.getDbAccelerometerSensorDao();
        }
    }

    @Override
    protected void dumpData() {


//        accelerometerSensorDao.inse
    }

    @Override
    public void startSensor() {
        reset();
        m_sensorManager.registerListener(this, m_accelerometerSensor, SENSOR_DELAY_BETWEEN_TWO_EVENTS);
        m_bIsRunning = true;
    }

    @Override
    public void stopSensor() {
        m_sensorManager.unregisterListener(this, m_accelerometerSensor);
        m_bIsRunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sendCurrentSeries();
        reset();
        m_intAccuracy = accuracy;
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
        m_intAccuracy = 0;
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

    public int getM_intAccuracy() {
        return this.m_intAccuracy;
    }

    public void setM_intAccuracy(int m_intAccuracy) {
        this.m_intAccuracy = m_intAccuracy;
    }

    private void sendCurrentSeries() {
        if (m_intNumValues > 0) {
//			SensorAccelerometer sensorAcc = new SensorAccelerometer();
//			sensorAcc.setAccuracy(m_intAccuracy);
//			sensorAcc.setAccelerationX(m_floatSumAccelerationX / m_intNumValues);
//			sensorAcc.setAccelerationY(m_floatSumAccelerationY / m_intNumValues);
//			sensorAcc.setAccelerationZ(m_floatSumAccelerationZ / m_intNumValues);
//			handleDBEntry(sensorAcc);
        }

    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_ACCELEROMETER;
    }

}
