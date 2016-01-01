package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
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
    private static final int DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
    private static int UPDATE_INTERVAL_IN_SEC = 5;
    // -----------------------------------------------------

    private static LightSensor INSTANCE;

    private DaoProvider daoProvider;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private long startTimestamp;
    private float mLastValue;
    private int accuracy;
    private int numValues;

    private LightSensor(Context context) {
        super(context);

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        mSensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static LightSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new LightSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        if (numValues > 0) {

            DbLightSensor sensorLight = new DbLightSensor();

            sensorLight.setValue(mLastValue / numValues);
            sensorLight.setAccuracy(accuracy);
            sensorLight.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

            Log.d(TAG, "Insert entry");

            daoProvider.getLightSensorDao().insert(sensorLight);

            Log.d(TAG, "Finished");
        }
    }

    @Override
    public void startSensor() {

        if (mSensorManager != null) {

            if (mSensor != null) {
                mSensorManager.registerListener(this,
                        mSensor,
                        DELAY_BETWEEN_TWO_EVENTS);

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
        return DtoType.LIGHT;
    }

    @Override
    public void reset() {

        mLastValue = 0;
        accuracy = 0;
        numValues = 0;
        startTimestamp = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            boolean isValueAdded = addNewValueToAverage(event, false);

            if (!isValueAdded) {

                dumpData();
                addNewValueToAverage(event, true);
            }
        }
    }

    /**
     * Update intervals
     *
     * @param event
     */
    @Override
    public void onEvent(UpdateSensorIntervalEvent event) {

        // only accept this sensor topic type
        if (event.getDtoType() != getType()) {
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
     * Adds new value after summation old ones
     *
     * @param event
     * @param newSeries
     * @return
     */
    private boolean addNewValueToAverage(SensorEvent event, boolean newSeries) {

        if (newSeries) {

            startTimestamp = event.timestamp;
            mLastValue = Math.abs(event.values[0]);
            numValues = 1;

            return true;
        } else {
            if (event.timestamp < (startTimestamp + UPDATE_INTERVAL_IN_SEC * 1_000_000_000l)) {

                mLastValue += Math.abs(event.values[0]);
                numValues++;

                return true;
            }
        }

        return false;
    }
}
