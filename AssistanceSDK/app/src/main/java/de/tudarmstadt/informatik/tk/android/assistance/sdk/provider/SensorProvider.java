package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.BrowserHistoryEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.CalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.CallLogEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.ContactsEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.BackgroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.LoudnessSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.RingtoneEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.RunningProcessesReaderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.RunningServicesReaderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.RunningTasksReaderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.MotionActivityEvent;

/**
 * Main sensor provider
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class SensorProvider {

    // general availability of sensors
    private List<ISensor> availableSensors = new ArrayList<>();
    private SparseArrayCompat<ISensor> availableSensorByType = new SparseArrayCompat<>();

    // running sensors
    private List<ISensor> enabledSensors = new ArrayList<>();
    private SparseArrayCompat<ISensor> enabledSensorByType = new SparseArrayCompat<>();

    private static SensorProvider INSTANCE;

    private Context mContext;

    /**
     * Constructs this class
     *
     * @param context
     */
    private SensorProvider(Context context) {

        this.mContext = context;

        initAvailableSensors();
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
    private void initAvailableSensors() {

        /**
         * Triggered events / sensors
         */

        AccelerometerSensor accelerometerSensor = new AccelerometerSensor(mContext);
        availableSensors.add(accelerometerSensor);

        ConnectionSensor connectionSensor = new ConnectionSensor(mContext);
        availableSensors.add(connectionSensor);

        ForegroundEvent foregroundEvent = new ForegroundEvent(mContext);
        availableSensors.add(foregroundEvent);

        ForegroundTrafficEvent foregroundTrafficEvent = new ForegroundTrafficEvent(mContext);
        availableSensors.add(foregroundTrafficEvent);

        GyroscopeSensor gyroscopeSensor = new GyroscopeSensor(mContext);
        availableSensors.add(gyroscopeSensor);

        LightSensor lightSensor = new LightSensor(mContext);
        availableSensors.add(lightSensor);

        LocationSensor locationSensor = new LocationSensor(mContext);
        availableSensors.add(locationSensor);

        MagneticFieldSensor magneticFieldSensor = new MagneticFieldSensor(mContext);
        availableSensors.add(magneticFieldSensor);

        MotionActivityEvent motionActivityEvent = MotionActivityEvent.getInstance(mContext);
        availableSensors.add(motionActivityEvent);

        /*
         * Periodic events / sensors
         */

        BackgroundTrafficEvent backgroundTrafficEvent = new BackgroundTrafficEvent(mContext);
        availableSensors.add(backgroundTrafficEvent);

        RingtoneEvent ringtoneEvent = new RingtoneEvent(mContext);
        availableSensors.add(ringtoneEvent);

        // loudness sensor is blocking microphone and consuming too much battery
        LoudnessSensor loudnessSensor = new LoudnessSensor(mContext);
        availableSensors.add(loudnessSensor);

        RunningProcessesReaderEvent runningProcessesReaderEvent = new RunningProcessesReaderEvent(mContext);
        availableSensors.add(runningProcessesReaderEvent);

        RunningTasksReaderEvent runningTasksReaderEvent = new RunningTasksReaderEvent(mContext);
        availableSensors.add(runningTasksReaderEvent);

        RunningServicesReaderEvent runningServicesReaderEvent = new RunningServicesReaderEvent(mContext);
        availableSensors.add(runningServicesReaderEvent);

        /*
         *  Content observer
         */

        BrowserHistoryEvent browserHistoryEvent = new BrowserHistoryEvent(mContext);
        availableSensors.add(browserHistoryEvent);

        CalendarEvent calendarEvent = new CalendarEvent(mContext);
        availableSensors.add(calendarEvent);

        ContactsEvent contactsEvent = new ContactsEvent(mContext);
        availableSensors.add(contactsEvent);

        CallLogEvent callLogEvent = new CallLogEvent(mContext);
        availableSensors.add(callLogEvent);


        /*
         * Save them in map for further fast access
         */
        for (ISensor sensor : availableSensors) {
            availableSensorByType.put(sensor.getType(), sensor);
        }
    }

    /**
     * Initializes enabled events
     */
    private void initEnabledSensors() {

        // TODO: query db for enabled events

        /*
         * Save them in map for further fast access
         */
        for (ISensor sensor : enabledSensors) {
            enabledSensorByType.put(sensor.getType(), sensor);
        }
    }

    /**
     * Returns available sensors by their PushType
     *
     * @param pushType
     * @return
     */
    public List<ISensor> getAvailableSensorsByPushType(EPushType pushType) {

        List<ISensor> result = new ArrayList<>();

        if (pushType == null) {
            return result;
        }

        for (ISensor sensor : availableSensors) {

            if (sensor.getPushType().equals(pushType)) {
                result.add(sensor);
            }
        }

        return result;
    }

    /**
     * Returns all available sensors in the system
     */
    public List<ISensor> getAvailableSensors() {
        return availableSensors;
    }

    /**
     * Returns available a sensor by it given type
     *
     * @param type
     * @return
     */
    public ISensor getAvailableSensor(int type) {
        return availableSensorByType.get(type);
    }

    /**
     * Returns all enabled sensors / events
     *
     * @return
     */
    public List<ISensor> getEnabledSensors() {

        List<ISensor> result = new ArrayList<>();

        for (ISensor sensor : enabledSensors) {

//            if (sensor.isDisabledBySystem() || sensor.isDisabledByUser()) {
//                continue;
//            }

            result.add(sensor);
        }

        return result;
    }

    /**
     * Returns enabled a sensor by it given type
     *
     * @param type
     * @return
     */
    public ISensor getEnabledSensor(int type) {
        return availableSensorByType.get(type);
    }

    /**
     * Assigns given context to available sensors
     *
     * @param context
     */
    public void setContextToSensors(Context context) {

        for (ISensor sensor : availableSensors) {
            sensor.setContext(context);
        }
    }
}
