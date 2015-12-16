package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.BrowserHistoryEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.CalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.CallLogEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver.ContactsEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.BackgroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.LoudnessSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.PowerLevelEvent;
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
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * Main sensor provider
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class SensorProvider {

    private static final String TAG = SensorProvider.class.getSimpleName();

    // general availability of sensors
    private Map<Integer, ISensor> availableSensors = new HashMap<>();

    // running sensors
    private Map<Integer, ISensor> runningSensors = new HashMap<>();

    private static SensorProvider INSTANCE;

    private Context mContext;

    private DaoProvider daoProvider;

    /**
     * Constructs this class
     *
     * @param context
     */
    private SensorProvider(Context context) {
        this.mContext = context;

        daoProvider = DaoProvider.getInstance(context);

        initSensors();
    }

    private void initSensors() {
        initAvailableSensors();
        initEnabledSensors();
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

        Log.d(TAG, "Initializing available sensors...");

        /**
         * Triggered events / sensors
         */

        AccelerometerSensor accelerometerSensor = AccelerometerSensor.getInstance(mContext);
        availableSensors.put(accelerometerSensor.getType(), accelerometerSensor);

        ConnectionSensor connectionSensor = ConnectionSensor.getInstance(mContext);
        availableSensors.put(connectionSensor.getType(), connectionSensor);

        ForegroundEvent foregroundEvent = ForegroundEvent.getInstance(mContext);
        availableSensors.put(foregroundEvent.getType(), foregroundEvent);

        ForegroundTrafficEvent foregroundTrafficEvent = ForegroundTrafficEvent.getInstance(mContext);
        availableSensors.put(foregroundTrafficEvent.getType(), foregroundTrafficEvent);

        GyroscopeSensor gyroscopeSensor = GyroscopeSensor.getInstance(mContext);
        availableSensors.put(gyroscopeSensor.getType(), gyroscopeSensor);

        LightSensor lightSensor = LightSensor.getInstance(mContext);
        availableSensors.put(lightSensor.getType(), lightSensor);

        LocationSensor locationSensor = LocationSensor.getInstance(mContext);
        availableSensors.put(locationSensor.getType(), locationSensor);

        MagneticFieldSensor magneticFieldSensor = MagneticFieldSensor.getInstance(mContext);
        availableSensors.put(magneticFieldSensor.getType(), magneticFieldSensor);

        MotionActivityEvent motionActivityEvent = MotionActivityEvent.getInstance(mContext);
        availableSensors.put(motionActivityEvent.getType(), motionActivityEvent);

        /*
         * Periodic events / sensors
         */

        PowerLevelEvent powerLevelEvent = PowerLevelEvent.getInstance(mContext);
        availableSensors.put(powerLevelEvent.getType(), powerLevelEvent);

        BackgroundTrafficEvent backgroundTrafficEvent = BackgroundTrafficEvent.getInstance(mContext);
        availableSensors.put(backgroundTrafficEvent.getType(), backgroundTrafficEvent);

        RingtoneEvent ringtoneEvent = RingtoneEvent.getInstance(mContext);
        availableSensors.put(ringtoneEvent.getType(), ringtoneEvent);

        // loudness sensor is blocking microphone and consuming too much battery
        LoudnessSensor loudnessSensor = LoudnessSensor.getInstance(mContext);
        availableSensors.put(loudnessSensor.getType(), loudnessSensor);

        RunningProcessesReaderEvent runningProcessesReaderEvent = RunningProcessesReaderEvent.getInstance(mContext);
        availableSensors.put(runningProcessesReaderEvent.getType(), runningProcessesReaderEvent);

        RunningTasksReaderEvent runningTasksReaderEvent = RunningTasksReaderEvent.getInstance(mContext);
        availableSensors.put(runningTasksReaderEvent.getType(), runningTasksReaderEvent);

        RunningServicesReaderEvent runningServicesReaderEvent = RunningServicesReaderEvent.getInstance(mContext);
        availableSensors.put(runningServicesReaderEvent.getType(), runningServicesReaderEvent);

        /*
         *  Content observers
         */

        BrowserHistoryEvent browserHistoryEvent = BrowserHistoryEvent.getInstance(mContext);
        availableSensors.put(browserHistoryEvent.getType(), browserHistoryEvent);

        CalendarEvent calendarEvent = CalendarEvent.getInstance(mContext);
        availableSensors.put(calendarEvent.getType(), calendarEvent);

        ContactsEvent contactsEvent = ContactsEvent.getInstance(mContext);
        availableSensors.put(contactsEvent.getType(), contactsEvent);

        CallLogEvent callLogEvent = CallLogEvent.getInstance(mContext);
        availableSensors.put(callLogEvent.getType(), callLogEvent);

        Log.d(TAG, "Finished. Number of sensors: " + availableSensors.size());
    }

    /**
     * Initializes enabled events
     */
    private void initEnabledSensors() {

        Log.d(TAG, "Initializing ENABLED sensors...");

        String userToken = PreferenceProvider.getInstance(mContext).getUserToken();
        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (userToken.isEmpty() || user == null) {
            Log.d(TAG, "user token or user is NULL!");
            return;
        }

        List<DbModule> userActiveModules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (userActiveModules == null || userActiveModules.isEmpty()) {
            Log.d(TAG, "User has no active modules!");
            return;
        }

        List<DbModuleCapability> moduleCapabilities = new ArrayList<>();

        for (DbModule module : userActiveModules) {

            moduleCapabilities.addAll(daoProvider.getModuleCapabilityDao()
                    .getAllActive(module.getId()));
        }

        /*
         * Save them in map for further fast access
         */
        for (DbModuleCapability dbCap : moduleCapabilities) {

            int capType = DtoType.getDtoType(dbCap.getType());
            ISensor sensor = availableSensors.get(capType);

            runningSensors.put(sensor.getType(), sensor);

            if (!EventBus.getDefault().isRegistered(sensor)) {
                EventBus.getDefault().register(sensor);
            }
        }

        Log.d(TAG, "Finished. Number of sensors: " + runningSensors.size());
    }

    /**
     * Returns list of user's active sensors/events
     *
     * @param moduleCapabilities
     * @return
     */
    private List<ISensor> mapModuleCapabilitiesToSensors(List<DbModuleCapability> moduleCapabilities) {

        List<ISensor> result = new ArrayList<>();

        for (DbModuleCapability capability : moduleCapabilities) {
            result.add(getSensorByDtoType(capability.getType()));
        }

        return result;
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

        for (Map.Entry<Integer, ISensor> entry : availableSensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor.getPushType().equals(pushType)) {
                result.add(sensor);
            }
        }

        return result;
    }

    /**
     * Returns all available sensors in the system
     */
    public Map<Integer, ISensor> getAvailableSensors() {
        return availableSensors;
    }

    /**
     * Returns available a sensor by it given type
     *
     * @param type
     * @return
     */
    public ISensor getAvailableSensor(int type) {
        return availableSensors.get(type);
    }

    /**
     * Returns all enabled sensors / events
     *
     * @return
     */
    public Map<Integer, ISensor> getRunningSensors() {
        return runningSensors;
    }

    /**
     * Returns enabled a sensor by it given type
     *
     * @param type
     * @return
     */
    public ISensor getEnabledSensor(int type) {
        return runningSensors.get(type);
    }

    /**
     * Disables an active sensor
     *
     * @param type
     */
    public void disableSensor(int type) {

        ISensor sensor = runningSensors.get(type);

        if (sensor == null) {
            return;
        }

        sensor.stopSensor();
        runningSensors.remove(type);
    }

    /**
     * Assigns given context to available sensors
     *
     * @param context
     */
    public void setContextToSensors(Context context) {

        for (Map.Entry<Integer, ISensor> entry : availableSensors.entrySet()) {
            entry.getValue().setContext(context);
        }

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {
            entry.getValue().setContext(context);
        }

        mContext = context;
    }

    /**
     * Returns a sensor/event by its DTO type
     *
     * @param apiDtoType
     * @return
     */
    public ISensor getSensorByDtoType(String apiDtoType) {

        if (apiDtoType == null || apiDtoType.isEmpty()) {
            return null;
        }

        ISensor result = null;

        int dtoType = DtoType.getDtoType(apiDtoType);

        for (Map.Entry<Integer, ISensor> entry : availableSensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor.getType() == dtoType) {
                result = sensor;
                break;
            }
        }

        return result;
    }

    /**
     * Starts all sensors in running mappings
     */
    public void startAllStoppedSensors() {

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {
            entry.getValue().startSensor();
        }
    }

    /**
     * Stops all sensors from running
     */
    public void stopAllRunningSensors() {

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {
            entry.getValue().stopSensor();
        }
    }
}