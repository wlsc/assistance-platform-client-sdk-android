package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.ISensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MotionActivityEvent;

/**
 * Main sensor provider
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class SensorProvider {

    private SparseArrayCompat<ISensor> sensorByType = new SparseArrayCompat<>();

    private List<ISensor> mAvailableSensors = new ArrayList<>();

    private static SensorProvider INSTANCE;

    private Context mContext;

    /**
     * Constructs this class
     *
     * @param context
     */
    private SensorProvider(Context context) {

        this.mContext = context;

        initSensors();
    }

    public static SensorProvider getInstance(Context ctx) {

        if (INSTANCE == null) {
            INSTANCE = new SensorProvider(ctx);
        } else {
            INSTANCE.setContextToSensors(ctx);
        }

        return INSTANCE;
    }

    /**
     * Initializes available sensors
     */
    private void initSensors() {

        /**
         * Triggered events / sensors
         */

        // works
        AccelerometerSensor accelerometerSensor = new AccelerometerSensor(mContext);
        mAvailableSensors.add(accelerometerSensor);

        MotionActivityEvent motionActivityEvent = MotionActivityEvent.getInstance(mContext);
        mAvailableSensors.add(motionActivityEvent);

        // works
//        LightSensor lightSensor = new LightSensor(mContext);
//        mAvailableSensors.add(lightSensor);

        // works
        LocationSensor locationSensor = new LocationSensor(mContext);
        mAvailableSensors.add(locationSensor);

//        MeasurementEvent measurementEvent = new MeasurementEvent(mContext);
//        mAvailableSensors.add(measurementEvent);

        // works
//        ConnectionSensor connectionSensor = new ConnectionSensor(mContext);
//        mAvailableSensors.add(connectionSensor);

        //new foreground traffic
//        ForegroundTrafficEvent foregroundTrafficEvent = new ForegroundTrafficEvent(mContext);
//        mAvailableSensors.add(foregroundTrafficEvent);

        //new periodic background traffic
//        BackgroundTrafficEvent backgroundTrafficEvent = new BackgroundTrafficEvent(mContext);
//        mAvailableSensors.add(backgroundTrafficEvent);

        // loudness sensor is blocking microphone and consuming too much battery
        // LoudnessSensor loudnessSensor = new LoudnessSensor(mContext);
        // mAvailableSensors.add(loudnessSensor);

        /**
         * Periodic events / sensors
         */

//        RingtoneEvent ringtoneEvent = new RingtoneEvent(mContext);
//        mAvailableSensors.add(ringtoneEvent);

//        CalendarEvent calendarEvent = new CalendarEvent(mContext);
//        mAvailableSensors.add(calendarEvent);

//        ContactsEvent contactsEvent = new ContactsEvent(mContext);
//        mAvailableSensors.add(contactsEvent);

//        CallLogEvent callLogEvent = new CallLogEvent(mContext);
//        mAvailableSensors.add(callLogEvent);

//        BrowserHistoryEvent browserHistoryEvent = new BrowserHistoryEvent(mContext);
//        mAvailableSensors.add(browserHistoryEvent);

        ForegroundEvent foregroundEvent = new ForegroundEvent(mContext);
        mAvailableSensors.add(foregroundEvent);

        /**
         * Save them in map for further fast access
         */
        for (ISensor sensor : mAvailableSensors) {
            sensorByType.put(sensor.getType(), sensor);
        }
    }

    /**
     * Returns available sensors by their PushType
     *
     * @param pushType
     * @return
     */
    public List<ISensor> getSensorsByPushType(EPushType pushType) {

        List<ISensor> result = new ArrayList<>();

        if (pushType == null) {
            return result;
        }

        for (ISensor sensor : mAvailableSensors) {
            if (sensor.getPushType().equals(pushType)) {
                result.add(sensor);
            }
        }

        return result;
    }

    public List<ISensor> getAvailableSensors() {
        return mAvailableSensors;
    }

    /**
     * Returns a sensor by it given type
     *
     * @param type
     * @return
     */
    public ISensor getSensor(int type) {
        return sensorByType.get(type);
    }

    /**
     * Returns all enabled sensors / events
     *
     * @return
     */
    public List<ISensor> getEnabledSensors() {

        List<ISensor> result = new ArrayList<>();

        for (ISensor sensor : mAvailableSensors) {

            if (sensor.isDisabledBySystem() || sensor.isDisabledByUser()) {
                continue;
            }

            result.add(sensor);
        }

        return result;
    }

    /**
     * Assigns given context to available sensors
     *
     * @param ctx
     */
    public void setContextToSensors(Context ctx) {
        for (ISensor sensor : mAvailableSensors) {
            sensor.setContext(ctx);
        }
    }
}
