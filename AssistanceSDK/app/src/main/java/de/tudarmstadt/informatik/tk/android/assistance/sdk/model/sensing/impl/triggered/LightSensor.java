package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.10.2015
 */
public class LightSensor
        extends AbstractTriggeredEvent
        implements SensorEventListener {

    private static final String TAG = LightSensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int SENSOR_DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private static final int SENSOR_MIN_DIFFERENCE = 5;
    // -----------------------------------------------------

    private DaoProvider daoProvider;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int accuracy;
    private float mLastValue;

    public LightSensor(Context context) {
        super(context);

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        mSensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public void dumpData() {

        DbLightSensor sensorLight = new DbLightSensor();

        sensorLight.setValue(mLastValue);
        sensorLight.setAccuracy(accuracy);
        sensorLight.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getLightSensorDao().insert(sensorLight);

        Log.d(TAG, "Finished");
    }

    @Override
    public void startSensor() {

        if (mSensorManager != null) {

            if (mSensor != null) {
                mSensorManager.registerListener(this,
                        mSensor,
                        SENSOR_DELAY_BETWEEN_TWO_EVENTS);

                setRunning(true);
            }
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (mSensorManager != null) {

                if (mSensor != null) {
                    mSensorManager.unregisterListener(this, mSensor);
                }
            }
        } finally {
            setRunning(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            this.accuracy = accuracy;
        }
    }

    @Override
    public int getType() {
        return Sensor.TYPE_LIGHT;
    }

    @Override
    public void reset() {
        mLastValue = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            float currentValue = event.values[0];

            if (currentValue < mLastValue - SENSOR_MIN_DIFFERENCE ||
                    (currentValue > (mLastValue + SENSOR_MIN_DIFFERENCE))) {

                mLastValue = currentValue;

                dumpData();
            }
        }
    }
}
