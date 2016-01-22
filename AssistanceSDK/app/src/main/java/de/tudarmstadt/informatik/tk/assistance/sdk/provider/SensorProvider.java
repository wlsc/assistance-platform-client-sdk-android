package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbBrowserHistorySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactEmailSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbFacebookSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLoudnessSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerLevelSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningTasksSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbWifiConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.event.ShowAccessibilityServiceTutorialEvent;
import de.tudarmstadt.informatik.tk.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.contact.ContactEmailNumber;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.contact.ContactSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.SensorUploadHolder;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.contentobserver.BrowserHistorySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.contentobserver.CalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.contentobserver.CallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.contentobserver.ContactsSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.general.FacebookSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.general.TucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.BackgroundTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.LoudnessSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.PowerLevelSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.RingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.RunningProcessesReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.RunningServicesReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.RunningTasksReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.ForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.ForegroundTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered.MotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social.FacebookSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social.TucanSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * Sensor and event provider/handler
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

    private static PreferenceProvider preferenceProvider;

    private static DaoProvider daoProvider;

    /**
     * Constructs this class
     *
     * @param context
     */
    private SensorProvider(Context context) {
        this.mContext = context;

        preferenceProvider = PreferenceProvider.getInstance(context);
        daoProvider = DaoProvider.getInstance(context);

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

    private void initSensors() {
        initAvailableSensors();
        initEnabledSensors();
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

        ForegroundSensor foregroundSensor = ForegroundSensor.getInstance(mContext);
        availableSensors.put(foregroundSensor.getType(), foregroundSensor);

        ForegroundTrafficSensor foregroundTrafficSensor = ForegroundTrafficSensor
                .getInstance(mContext, ForegroundTrafficSensor.Mode.PERIODIC);
        availableSensors.put(foregroundTrafficSensor.getType(), foregroundTrafficSensor);

        GyroscopeSensor gyroscopeSensor = GyroscopeSensor.getInstance(mContext);
        availableSensors.put(gyroscopeSensor.getType(), gyroscopeSensor);

        LightSensor lightSensor = LightSensor.getInstance(mContext);
        availableSensors.put(lightSensor.getType(), lightSensor);

        LocationSensor locationSensor = LocationSensor.getInstance(mContext);
        availableSensors.put(locationSensor.getType(), locationSensor);

        MagneticFieldSensor magneticFieldSensor = MagneticFieldSensor.getInstance(mContext);
        availableSensors.put(magneticFieldSensor.getType(), magneticFieldSensor);

        MotionActivitySensor motionActivitySensor = MotionActivitySensor.getInstance(mContext);
        availableSensors.put(motionActivitySensor.getType(), motionActivitySensor);

        /*
         * Periodic events / sensors
         */

        PowerLevelSensor powerLevelEvent = PowerLevelSensor.getInstance(mContext);
        availableSensors.put(powerLevelEvent.getType(), powerLevelEvent);

        BackgroundTrafficSensor backgroundTrafficEvent = BackgroundTrafficSensor.getInstance(mContext);
        availableSensors.put(backgroundTrafficEvent.getType(), backgroundTrafficEvent);

        RingtoneSensor ringtoneEvent = RingtoneSensor.getInstance(mContext);
        availableSensors.put(ringtoneEvent.getType(), ringtoneEvent);

        // loudness sensor is blocking microphone and consuming too much battery
        LoudnessSensor loudnessSensor = LoudnessSensor.getInstance(mContext);
        availableSensors.put(loudnessSensor.getType(), loudnessSensor);

        RunningProcessesReaderSensor runningProcessesReaderEvent = RunningProcessesReaderSensor.getInstance(mContext);
        availableSensors.put(runningProcessesReaderEvent.getType(), runningProcessesReaderEvent);

        RunningTasksReaderSensor runningTasksReaderEvent = RunningTasksReaderSensor.getInstance(mContext);
        availableSensors.put(runningTasksReaderEvent.getType(), runningTasksReaderEvent);

        RunningServicesReaderSensor runningServicesReaderEvent = RunningServicesReaderSensor.getInstance(mContext);
        availableSensors.put(runningServicesReaderEvent.getType(), runningServicesReaderEvent);

        /*
         *  Content observers
         */

        BrowserHistorySensor browserHistoryEvent = BrowserHistorySensor.getInstance(mContext);
        availableSensors.put(browserHistoryEvent.getType(), browserHistoryEvent);

        CalendarSensor calendarEvent = CalendarSensor.getInstance(mContext);
        availableSensors.put(calendarEvent.getType(), calendarEvent);

        ContactsSensor contactsEvent = ContactsSensor.getInstance(mContext);
        availableSensors.put(contactsEvent.getType(), contactsEvent);

        CallLogSensor callLogEvent = CallLogSensor.getInstance(mContext);
        availableSensors.put(callLogEvent.getType(), callLogEvent);

        /*
         *  GENERAL DUMMY SENSORS / EVENTS
         */
        TucanSensor tucanEvent = TucanSensor.getInstance(mContext);
        availableSensors.put(tucanEvent.getType(), tucanEvent);

        FacebookSensor facebookEvent = FacebookSensor.getInstance(mContext);
        availableSensors.put(facebookEvent.getType(), facebookEvent);

        Log.d(TAG, "Finished. Number of sensors: " + availableSensors.size());
    }

    /**
     * Initializes enabled events
     */
    private void initEnabledSensors() {

        Log.d(TAG, "Initializing ENABLED sensors...");

        String userToken = preferenceProvider.getUserToken();
        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "user token or user is NULL!");
            return;
        }

        List<DbModule> activeModules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (activeModules.isEmpty()) {
            Log.d(TAG, "User has no active modules!");
            return;
        }

        List<DbModuleCapability> activeModuleCapabilities = new ArrayList<>();

        for (DbModule module : activeModules) {

            List<DbModuleCapability> caps = module.getDbModuleCapabilityList();

            if (caps.isEmpty()) {
                continue;
            }

            for (DbModuleCapability cap : caps) {

                if (cap.getActive()) {
                    activeModuleCapabilities.add(cap);
                }
            }
        }

        // measure intervals by their DTO type
        SparseArray<Double> sensorIntervals = new SparseArray<>();

        EventBus eventBus = EventBus.getDefault();

        /*
         * Save them in map for further fast access
         */
        for (DbModuleCapability cap : activeModuleCapabilities) {

            int capType = SensorApiType.getDtoType(cap.getType());
            ISensor sensor = availableSensors.get(capType);

            // haven't seen that sensor yet
            if (runningSensors.get(capType) == null) {
                runningSensors.put(capType, sensor);
                sensorIntervals.put(capType, cap.getCollectionInterval());
            } else {
                Double oldCollectionFreq = sensorIntervals.get(capType);
                sensorIntervals.put(capType, Math.min(oldCollectionFreq, cap.getCollectionInterval()));
            }

            if (!eventBus.isRegistered(sensor)) {
                eventBus.register(sensor);
            }
        }

        // send only if we have subscribers
        if (eventBus.hasSubscriberForEvent(UpdateSensorIntervalEvent.class)) {

            // send collection interval updates to sensor/events
            for (int i = 0, sensorIntervalsSize = sensorIntervals.size(); i < sensorIntervalsSize; i++) {

                if (sensorIntervals.valueAt(i) == null) {
                    continue;
                }

                eventBus.post(new UpdateSensorIntervalEvent(
                        sensorIntervals.keyAt(i),
                        sensorIntervals.valueAt(i)
                ));
            }

            Log.d(TAG, "Sent event to: " + sensorIntervals.size());
        }

        // check if it was not already ignored by user once
        // if not, then show tutorial
        if (!PreferenceProvider.getInstance(mContext).getAccessibilityServiceIgnoredByUser()) {

            for (DbModuleCapability cap : activeModuleCapabilities) {

                if (cap == null || cap.getType() == null) {
                    continue;
                }

                String foregroundTypeName = SensorApiType.getApiName(SensorApiType.FOREGROUND);
                String foregroundTrafficTypeName = SensorApiType.getApiName(SensorApiType.FOREGROUND_TRAFFIC);

                if (cap.getType().equals(foregroundTypeName) || cap.getType().equals(foregroundTrafficTypeName)) {
                    if (eventBus.hasSubscriberForEvent(ShowAccessibilityServiceTutorialEvent.class)) {
                        eventBus.post(new ShowAccessibilityServiceTutorialEvent());
                    }
                }
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
        Log.d(TAG, "Running sensors: " + runningSensors.size());
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

        if (context == null) {
            return;
        }

        for (Map.Entry<Integer, ISensor> entry : availableSensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor == null) {
                continue;
            }

            sensor.setContext(context);
        }

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor == null) {
                continue;
            }

            sensor.setContext(context);
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

        if (apiDtoType == null || apiDtoType.isEmpty() || availableSensors == null) {
            return null;
        }

        ISensor result = null;

        int dtoType = SensorApiType.getDtoType(apiDtoType);

        for (Map.Entry<Integer, ISensor> entry : availableSensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor == null) {
                continue;
            }

            if (sensor.getType() == dtoType) {
                result = sensor;
                break;
            }
        }

        return result;
    }

    /**
     * Returns required collection frequency
     * for given sensor type
     *
     * @param sensorType
     * @return
     */
    public Double getCollectionInterval(String sensorType) {

        if (sensorType == null) {
            return null;
        }

        String userToken = preferenceProvider.getUserToken();

        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            return null;
        }

        List<DbModule> modules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (modules.isEmpty()) {
            return null;
        }

        Double result = Double.MAX_VALUE;

        for (DbModule module : modules) {

            List<DbModuleCapability> capabilities = module.getDbModuleCapabilityList();

            if (capabilities.isEmpty()) {
                continue;
            }

            for (DbModuleCapability cap : capabilities) {

                // we found our active capability type
                if (cap.getActive() && cap.getType().equals(sensorType)) {

                    // take the minimum
                    result = Math.min(cap.getCollectionInterval(), result);

                    // don't need to continue here
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns minimum of required update interval
     * for given sensor type
     *
     * @param sensorType
     * @return
     */
    public Double getUpdateInterval(String sensorType) {

        if (sensorType == null) {
            return null;
        }

        String userToken = preferenceProvider.getUserToken();

        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            return null;
        }

        List<DbModule> modules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (modules.isEmpty()) {
            return null;
        }

        Double result = Double.MAX_VALUE;

        for (DbModule module : modules) {

            List<DbModuleCapability> capabilities = module.getDbModuleCapabilityList();

            if (capabilities.isEmpty()) {
                continue;
            }

            for (DbModuleCapability cap : capabilities) {

                // we found our active capability type
                if (cap.getActive() && cap.getType().equals(sensorType) && cap.getUpdateInterval() != null) {

                    // take the minimum
                    result = Math.min(cap.getUpdateInterval(), result);

                    // don't need to continue here
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns minimum of required upload interval
     * for each sensor type
     *
     * @return
     */
    public Double getMinSensorUploadInterval() {

        String userToken = preferenceProvider.getUserToken();

        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            return null;
        }

        List<DbModule> modules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (modules.isEmpty()) {
            return null;
        }

        Double result = Double.MAX_VALUE;

        for (DbModule module : modules) {

            List<DbModuleCapability> capabilities = module.getDbModuleCapabilityList();

            if (capabilities.isEmpty()) {
                continue;
            }

            for (DbModuleCapability cap : capabilities) {

                // we found our active capability type
                if (cap.getActive() && cap.getUpdateInterval() != null) {

                    // take the minimum
                    result = Math.min(cap.getUpdateInterval(), result);
                }
            }
        }

        return result;
    }

    /**
     * Starts all sensors in running mappings
     */
    public void startAllStoppedSensors() {

        Log.d(TAG, "Starting all stopped sensors...");

        if (runningSensors == null) {
            return;
        }

        Log.d(TAG, "Running sensors size: " + runningSensors.size());

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {
            entry.getValue().startSensor();
        }
    }

    /**
     * Stops all sensors from running
     */
    public void stopAllRunningSensors() {

        Log.d(TAG, "Stopping all running sensors...");

        if (runningSensors == null) {
            Log.d(TAG, "Running sensors array is null");
            return;
        }

        for (Map.Entry<Integer, ISensor> entry : runningSensors.entrySet()) {
            entry.getValue().stopSensor();
        }
    }

    /**
     * Removes all enabled sensors
     */
    public void clearRunningSensors() {

        if (runningSensors != null) {

            Log.d(TAG, "Clearing running sensors...");
            stopAllRunningSensors();
            runningSensors.clear();
        }
    }

    /**
     * Returns DTO types of which sensors are available on the running Android systen
     *
     * @return
     */
    public Set<Integer> getUsableDeviceSensorDtos() {

        if (mContext == null) {
            return Collections.emptySet();
        }

        SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager == null) {
            return Collections.emptySet();
        }

        Set<Integer> dtos = new HashSet<>();

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            dtos.add(SensorApiType.getDtoType(Sensor.TYPE_ACCELEROMETER));
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            dtos.add(SensorApiType.getDtoType(Sensor.TYPE_GYROSCOPE));
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            dtos.add(SensorApiType.getDtoType(Sensor.TYPE_LIGHT));
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            dtos.add(SensorApiType.getDtoType(Sensor.TYPE_MAGNETIC_FIELD));
        }

        return dtos;
    }

    /**
     * Checks for ability of an user to run given module capability sensor/event type
     *
     * @param apiType
     * @return
     */
    public boolean hasUserAbilityToRunSensor(String apiType) {

        if (apiType == null) {
            return false;
        }

        Set<Integer> usableSensors = getUsableDeviceSensorDtos();
        int dtoType = SensorApiType.getDtoType(apiType);

        if (usableSensors.contains(dtoType)) {
            return true;
        }

        if (!SensorApiType.getApiName(dtoType).isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if given module capability active
     *
     * @param apiType
     * @return
     */
    public boolean isModuleCapabilityActive(String apiType) {

        String userToken = preferenceProvider.getUserToken();

        if (userToken.isEmpty()) {
            return false;
        }

        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            return false;
        }

        List<DbModule> activeModules = daoProvider.getModuleDao().getAllActive(user.getId());

        if (activeModules == null || activeModules.isEmpty()) {
            return false;
        }

        for (DbModule module : activeModules) {

            List<DbModuleCapability> caps = module.getDbModuleCapabilityList();

            if (caps == null) {
                continue;
            }

            for (DbModuleCapability cap : caps) {

                if (cap.getType().equals(apiType) && cap.getActive()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Activates given sensors/events by DTO type
     *
     * @param dtoTypes
     * @return
     */
    public boolean activateSensors(Set<Integer> dtoTypes) {

        if (dtoTypes == null) {
            Log.d(TAG, "DTO types array was null");
            return false;
        }

        Log.d(TAG, "Activating sensors...");

        for (Integer type : dtoTypes) {

            // that sensor is not running
            if (runningSensors.get(type) == null) {

                // get
                ISensor sensor = availableSensors.get(type);

                // run
                sensor.startSensor();

                // add to running sensors pool
                runningSensors.put(type, sensor);
            }
        }

        Log.d(TAG, "Finished activate sensors");

        return true;
    }

    /**
     * Deactivates given sensors/events by DTO type
     *
     * @param dtoTypes
     * @return
     */
    public boolean deactivateSensors(Set<Integer> dtoTypes) {

        if (dtoTypes == null) {
            Log.d(TAG, "DTO types array was null");
            return false;
        }

        Log.d(TAG, "Deactivating sensors...");

        for (Integer type : dtoTypes) {

            // sensor is running
            if (runningSensors.get(type) != null) {

                // get
                ISensor sensor = availableSensors.get(type);

                // stop
                sensor.stopSensor();

                // remove from running sensors pool
                runningSensors.remove(type);
            }
        }

        Log.d(TAG, "Finished deactivate sensors");

        return true;
    }

    /**
     * Synchronizes running sensors with DB entries
     */
    public void synchronizeRunningSensorsWithDb() {

        Log.d(TAG, "Synchronizing with db...");
        clearRunningSensors();
        initEnabledSensors();
    }

    /**
     * Returns events for upload to server
     *
     * @param numberOfElements
     * @return
     */
    public SensorUploadHolder getEntriesForUpload(int numberOfElements) {

        final SensorUploadHolder sensorUploadHolderHolder = new SensorUploadHolder(new SparseArray<>(), new SparseArray<>());

        String userToken = preferenceProvider.getUserToken();
        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "User is NULL. Aborting...");
            return sensorUploadHolderHolder;
        }

        long deviceId = preferenceProvider.getCurrentDeviceId();

        Map<Integer, ISensor> sensors = getRunningSensors();

        SparseArray<List<? extends IDbSensor>> dbEvents = new SparseArray<>();
        SparseArray<List<? extends SensorDto>> requestEvents = new SparseArray<>();

        for (Map.Entry<Integer, ISensor> entry : sensors.entrySet()) {

            ISensor sensor = entry.getValue();

            if (sensor == null) {
                continue;
            }

            int type = sensor.getType();

            switch (type) {
                case SensorApiType.ACCELEROMETER:

                    List<DbAccelerometerSensor> accList;

                    // give all
                    if (numberOfElements == 0) {
                        accList = daoProvider
                                .getAccelerometerSensorDao()
                                .getAll(deviceId);
                    } else {
                        accList = daoProvider
                                .getAccelerometerSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, accList);
                    requestEvents.put(type, daoProvider
                            .getAccelerometerSensorDao()
                            .convertObjects(accList));

                    break;

                case SensorApiType.LOCATION:

                    List<DbPositionSensor> posList;

                    // give all
                    if (numberOfElements == 0) {
                        posList = daoProvider
                                .getLocationSensorDao()
                                .getAll(deviceId);
                    } else {
                        posList = daoProvider
                                .getLocationSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, posList);
                    requestEvents.put(type, daoProvider
                            .getLocationSensorDao()
                            .convertObjects(posList));

                    break;

                case SensorApiType.MOTION_ACTIVITY:

                    List<DbMotionActivitySensor> maList;

                    // give all
                    if (numberOfElements == 0) {
                        maList = daoProvider
                                .getMotionActivitySensorDao()
                                .getAll(deviceId);
                    } else {
                        maList = daoProvider
                                .getMotionActivitySensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, maList);
                    requestEvents.put(type, daoProvider
                            .getMotionActivitySensorDao()
                            .convertObjects(maList));

                    break;

                case SensorApiType.FOREGROUND:

                    List<DbForegroundSensor> feList;

                    // give all
                    if (numberOfElements == 0) {
                        feList = daoProvider
                                .getForegroundSensorDao()
                                .getAll(deviceId);
                    } else {
                        feList = daoProvider
                                .getForegroundSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, feList);
                    requestEvents.put(type, daoProvider
                            .getForegroundSensorDao()
                            .convertObjects(feList));

                    break;

                case SensorApiType.CONNECTION:

                    // connection
                    List<DbConnectionSensor> conList;

                    // give all
                    if (numberOfElements == 0) {
                        conList = daoProvider
                                .getConnectionSensorDao()
                                .getAll(deviceId);
                    } else {
                        conList = daoProvider
                                .getConnectionSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, conList);
                    requestEvents.put(type, daoProvider
                            .getConnectionSensorDao()
                            .convertObjects(conList));

                    // mobile connection
                    List<DbMobileConnectionSensor> mobConList;

                    // give all
                    if (numberOfElements == 0) {
                        mobConList = daoProvider
                                .getMobileConnectionSensorDao()
                                .getAll(deviceId);
                    } else {
                        mobConList = daoProvider
                                .getMobileConnectionSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(SensorApiType.MOBILE_DATA_CONNECTION, mobConList);
                    requestEvents.put(SensorApiType.MOBILE_DATA_CONNECTION, daoProvider
                            .getMobileConnectionSensorDao()
                            .convertObjects(mobConList));

                    // wifi connection
                    List<DbWifiConnectionSensor> wifiConList;

                    // give all
                    if (numberOfElements == 0) {
                        wifiConList = daoProvider
                                .getWifiConnectionSensorDao()
                                .getAll(deviceId);
                    } else {
                        wifiConList = daoProvider
                                .getWifiConnectionSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(SensorApiType.WIFI_CONNECTION, wifiConList);
                    requestEvents.put(SensorApiType.WIFI_CONNECTION, daoProvider
                            .getWifiConnectionSensorDao()
                            .convertObjects(wifiConList));

                    break;

                case SensorApiType.FOREGROUND_TRAFFIC:

                    List<DbNetworkTrafficSensor> fteList;

                    // give all
                    if (numberOfElements == 0) {
                        fteList = daoProvider
                                .getNetworkTrafficSensorDao()
                                .getAllForeground(deviceId);
                    } else {
                        fteList = daoProvider
                                .getNetworkTrafficSensorDao()
                                .getFirstNForeground(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, fteList);
                    requestEvents.put(type, daoProvider
                            .getNetworkTrafficSensorDao()
                            .convertObjects(fteList));

                    break;

                case SensorApiType.BACKGROUND_TRAFFIC:

                    List<DbNetworkTrafficSensor> bteList;

                    // give all
                    if (numberOfElements == 0) {
                        bteList = daoProvider
                                .getNetworkTrafficSensorDao()
                                .getAllBackground(deviceId);
                    } else {
                        bteList = daoProvider
                                .getNetworkTrafficSensorDao()
                                .getFirstNBackground(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, bteList);
                    requestEvents.put(type, daoProvider
                            .getNetworkTrafficSensorDao()
                            .convertObjects(bteList));

                    break;

                case SensorApiType.LIGHT:

                    List<DbLightSensor> lightList;

                    // give all
                    if (numberOfElements == 0) {
                        lightList = daoProvider
                                .getLightSensorDao()
                                .getAll(deviceId);
                    } else {
                        lightList = daoProvider
                                .getLightSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, lightList);
                    requestEvents.put(type, daoProvider
                            .getLightSensorDao()
                            .convertObjects(lightList));

                    break;

                case SensorApiType.LOUDNESS:

                    List<DbLoudnessSensor> loudnessList;

                    // give all
                    if (numberOfElements == 0) {
                        loudnessList = daoProvider
                                .getLoudnessSensorDao()
                                .getAll(deviceId);
                    } else {
                        loudnessList = daoProvider
                                .getLoudnessSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, loudnessList);
                    requestEvents.put(type, daoProvider
                            .getLoudnessSensorDao()
                            .convertObjects(loudnessList));

                    break;

                case SensorApiType.RUNNING_PROCESSES:

                    List<DbRunningProcessesSensor> runProccessList;

                    // give all
                    if (numberOfElements == 0) {
                        runProccessList = daoProvider
                                .getRunningProcessesSensorDao()
                                .getAll(deviceId);
                    } else {
                        runProccessList = daoProvider
                                .getRunningProcessesSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, runProccessList);
                    requestEvents.put(type, daoProvider
                            .getRunningProcessesSensorDao()
                            .convertObjects(runProccessList));

                    break;

                case SensorApiType.RUNNING_SERVICES:

                    List<DbRunningServicesSensor> runServiceList;

                    // give all
                    if (numberOfElements == 0) {
                        runServiceList = daoProvider
                                .getRunningServicesSensorDao()
                                .getAll(deviceId);
                    } else {
                        runServiceList = daoProvider
                                .getRunningServicesSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, runServiceList);
                    requestEvents.put(type, daoProvider
                            .getRunningServicesSensorDao()
                            .convertObjects(runServiceList));

                    break;

                case SensorApiType.RUNNING_TASKS:

                    List<DbRunningTasksSensor> runTaskList;

                    // give all
                    if (numberOfElements == 0) {
                        runTaskList = daoProvider
                                .getRunningTasksSensorDao()
                                .getAll(deviceId);
                    } else {
                        runTaskList = daoProvider
                                .getRunningTasksSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, runTaskList);
                    requestEvents.put(type, daoProvider
                            .getRunningTasksSensorDao()
                            .convertObjects(runTaskList));

                    break;

                case SensorApiType.RINGTONE:

                    List<DbRingtoneSensor> ringtoneList;

                    // give all
                    if (numberOfElements == 0) {
                        ringtoneList = daoProvider
                                .getRingtoneSensorDao()
                                .getAll(deviceId);
                    } else {
                        ringtoneList = daoProvider
                                .getRingtoneSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, ringtoneList);
                    requestEvents.put(type, daoProvider
                            .getRingtoneSensorDao()
                            .convertObjects(ringtoneList));

                    break;

                case SensorApiType.GYROSCOPE:

                    List<DbGyroscopeSensor> gyroList;

                    // give all
                    if (numberOfElements == 0) {
                        gyroList = daoProvider
                                .getGyroscopeSensorDao()
                                .getAll(deviceId);
                    } else {
                        gyroList = daoProvider
                                .getGyroscopeSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, gyroList);
                    requestEvents.put(type, daoProvider
                            .getGyroscopeSensorDao()
                            .convertObjects(gyroList));

                    break;

                case SensorApiType.MAGNETIC_FIELD:

                    List<DbMagneticFieldSensor> mfList;

                    // give all
                    if (numberOfElements == 0) {
                        mfList = daoProvider
                                .getMagneticFieldSensorDao()
                                .getAll(deviceId);
                    } else {
                        mfList = daoProvider
                                .getMagneticFieldSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, mfList);
                    requestEvents.put(type, daoProvider
                            .getMagneticFieldSensorDao()
                            .convertObjects(mfList));

                    break;

                case SensorApiType.BROWSER_HISTORY:

                    List<DbBrowserHistorySensor> bhList;

                    // give all
                    if (numberOfElements == 0) {
                        bhList = daoProvider
                                .getBrowserHistorySensorDao()
                                .getAll(deviceId);
                    } else {
                        bhList = daoProvider
                                .getBrowserHistorySensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, bhList);
                    requestEvents.put(type, daoProvider
                            .getBrowserHistorySensorDao()
                            .convertObjects(bhList));

                    break;

                case SensorApiType.CALENDAR:

                    // retrieve all events
                    List<DbCalendarSensor> calendarList = daoProvider
                            .getCalendarSensorDao()
                            .getAllUpdated(deviceId);

                    List<SensorDto> calendarListConverted = daoProvider
                            .getCalendarSensorDao()
                            .convertObjects(calendarList);

                    List<CalendarSensorDto> calendarListConvertedNew = new ArrayList<>(
                            calendarListConverted.size());
                    Set<CalendarReminder> eventRemindersConvertedNew = new HashSet<>();

                    final CalendarReminderSensorDao calendarReminderSensorDao = daoProvider
                            .getCalendarReminderSensorDao();

                    for (SensorDto sensorDto : calendarListConverted) {

                        if (sensorDto == null) {
                            continue;
                        }

                        CalendarSensorDto calendarSensorDto = (CalendarSensorDto) sensorDto;
                        List<DbCalendarReminderSensor> eventReminders = calendarReminderSensorDao
                                .getAllByEventId(Long.valueOf(calendarSensorDto.getEventId()), deviceId);

                        if (eventReminders == null || eventReminders.isEmpty()) {
                            continue;
                        }

                        List<SensorDto> eventRemindersConverted = calendarReminderSensorDao
                                .convertObjects(eventReminders);

                        for (SensorDto sensorReminderDto : eventRemindersConverted) {
                            eventRemindersConvertedNew.add((CalendarReminder) sensorReminderDto);
                        }

                        calendarSensorDto.setAlarms(eventRemindersConvertedNew);
                        calendarListConvertedNew.add(calendarSensorDto);
                    }

                    dbEvents.put(type, calendarList);
                    requestEvents.put(type, calendarListConvertedNew);

                    break;

                case SensorApiType.CALL_LOG:

                    List<DbCallLogSensor> callLogList;

                    // give all
                    if (numberOfElements == 0) {
                        callLogList = daoProvider
                                .getCallLogSensorDao()
                                .getAll(deviceId);
                    } else {
                        callLogList = daoProvider
                                .getCallLogSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, callLogList);
                    requestEvents.put(type, daoProvider
                            .getCallLogSensorDao()
                            .convertObjects(callLogList));

                    break;

                case SensorApiType.CONTACT:

                    // retrieve all events
                    List<DbContactSensor> contactList = daoProvider
                            .getContactSensorDao()
                            .getAllUpdated(deviceId);

                    List<SensorDto> contactListConverted = daoProvider
                            .getContactSensorDao()
                            .convertObjects(contactList);

                    List<ContactSensorDto> contactListConvertedNew = new ArrayList<>(
                            contactListConverted.size());
                    Set<ContactEmailNumber> emailsConvertedNew = new HashSet<>();
                    Set<ContactEmailNumber> numbersConvertedNew = new HashSet<>();

                    final ContactEmailSensorDao contactEmailDao = daoProvider
                            .getContactEmailSensorDao();
                    final ContactNumberSensorDao contactNumberSensorDao = daoProvider
                            .getContactNumberSensorDao();

                    for (SensorDto sensorDto : contactListConverted) {

                        if (sensorDto == null) {
                            continue;
                        }

                        ContactSensorDto contactSensorDto = (ContactSensorDto) sensorDto;

                        // EMAILS
                        List<DbContactEmailSensor> eventEmails = contactEmailDao
                                .getAll(Long.valueOf(contactSensorDto.getGlobalContactId()), deviceId);

                        if (eventEmails == null || eventEmails.isEmpty()) {
                            continue;
                        }

                        for (DbContactEmailSensor contactEmail : eventEmails) {
                            emailsConvertedNew.add(
                                    new ContactEmailNumber(
                                            contactEmail.getType(),
                                            contactEmail.getAddress()));
                        }

                        // NUMBERS
                        List<DbContactNumberSensor> eventNumbers = contactNumberSensorDao
                                .getAll(Long.valueOf(contactSensorDto.getGlobalContactId()), deviceId);

                        if (eventNumbers == null || eventNumbers.isEmpty()) {
                            continue;
                        }

                        for (DbContactNumberSensor contactNumber : eventNumbers) {
                            numbersConvertedNew.add(
                                    new ContactEmailNumber(
                                            contactNumber.getType(),
                                            contactNumber.getNumber()));
                        }

                        contactSensorDto.setEmailAddresses(emailsConvertedNew);
                        contactSensorDto.setPhoneNumbers(numbersConvertedNew);

                        contactListConvertedNew.add(contactSensorDto);
                    }

                    dbEvents.put(type, contactList);
                    requestEvents.put(type, contactListConvertedNew);

                    break;

                case SensorApiType.POWER_LEVEL:

                    List<DbPowerLevelSensor> powerLevelList;

                    // give all
                    if (numberOfElements == 0) {
                        powerLevelList = daoProvider
                                .getPowerLevelSensorDao()
                                .getAll(deviceId);
                    } else {
                        powerLevelList = daoProvider
                                .getPowerLevelSensorDao()
                                .getFirstN(numberOfElements, deviceId);
                    }

                    dbEvents.put(type, powerLevelList);
                    requestEvents.put(type, daoProvider
                            .getPowerLevelSensorDao()
                            .convertObjects(powerLevelList));

                    break;
            }
        }

        /**
         * BATTERY STATUS
         */
        List<DbPowerStateSensor> powerStateList;

        // give all
        if (numberOfElements == 0) {
            powerStateList = daoProvider
                    .getPowerStateSensorDao()
                    .getAll(deviceId);
        } else {
            powerStateList = daoProvider
                    .getPowerStateSensorDao()
                    .getFirstN(numberOfElements, deviceId);
        }

        dbEvents.put(SensorApiType.POWER_STATE, powerStateList);
        requestEvents.put(SensorApiType.POWER_STATE, daoProvider
                .getPowerStateSensorDao()
                .convertObjects(powerStateList));

        /**
         * TUCAN sensor
         */
        TucanSensorDao tucanDao = daoProvider
                .getTucanSensorDao();

        DbTucanSensor tucanEntry = tucanDao.getIfChangedForUserId(user.getId());

        if (tucanEntry != null) {

            List<DbTucanSensor> tucanSensorList = new ArrayList<>(1);

            tucanSensorList.add(tucanEntry);

            dbEvents.put(SensorApiType.UNI_TUCAN, tucanSensorList);
            requestEvents.put(SensorApiType.UNI_TUCAN, daoProvider
                    .getTucanSensorDao()
                    .convertObjects(tucanSensorList));
        }

        /**
         * FACEBOOK sensor
         */
        FacebookSensorDao facebookDao = daoProvider
                .getFacebookSensorDao();

        DbFacebookSensor facebookEntry = facebookDao.getIfChangedForUserId(user.getId());

        if (facebookEntry != null) {

            List<DbFacebookSensor> facebookSensorList = new ArrayList<>(1);

            facebookSensorList.add(facebookEntry);

            dbEvents.put(SensorApiType.SOCIAL_FACEBOOK, facebookSensorList);
            requestEvents.put(SensorApiType.SOCIAL_FACEBOOK, daoProvider
                    .getFacebookSensorDao()
                    .convertObjects(facebookSensorList));
        }

        sensorUploadHolderHolder.setDbEvents(dbEvents);
        sensorUploadHolderHolder.setRequestEvents(requestEvents);

        // FINALLY return result
        return sensorUploadHolderHolder;
    }

    /**
     * Removes successful transmitted entries from database
     */
    public void handleSentEvents(SparseArray<List<? extends IDbSensor>> dbEvents) {

        Log.d(TAG, "Handling sent events...");

        for (int i = 0, dbEventsSize = dbEvents.size(); i < dbEventsSize; i++) {

            int type = dbEvents.keyAt(i);
            List<? extends IDbSensor> values = dbEvents.valueAt(i);

            if (values == null || values.isEmpty()) {
                continue;
            }

            switch (type) {
                case SensorApiType.ACCELEROMETER:
                    daoProvider.getAccelerometerSensorDao().delete((List<DbAccelerometerSensor>) values);
                    break;

                case SensorApiType.LOCATION:
                    daoProvider.getLocationSensorDao().delete((List<DbPositionSensor>) values);
                    break;

                case SensorApiType.MOTION_ACTIVITY:
                    daoProvider.getMotionActivitySensorDao().delete((List<DbMotionActivitySensor>) values);
                    break;

                case SensorApiType.FOREGROUND:
                    daoProvider.getForegroundSensorDao().delete((List<DbForegroundSensor>) values);
                    break;

                case SensorApiType.FOREGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficSensorDao().delete((List<DbNetworkTrafficSensor>) values);
                    break;

                case SensorApiType.BACKGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficSensorDao().delete((List<DbNetworkTrafficSensor>) values);
                    break;

                case SensorApiType.CONNECTION:
                    daoProvider.getConnectionSensorDao().delete((List<DbConnectionSensor>) values);
                    break;

                case SensorApiType.MOBILE_DATA_CONNECTION:
                    daoProvider.getMobileConnectionSensorDao().delete((List<DbMobileConnectionSensor>) values);
                    break;

                case SensorApiType.WIFI_CONNECTION:
                    daoProvider.getWifiConnectionSensorDao().delete((List<DbWifiConnectionSensor>) values);
                    break;

                case SensorApiType.LIGHT:
                    daoProvider.getLightSensorDao().delete((List<DbLightSensor>) values);
                    break;

                case SensorApiType.LOUDNESS:
                    daoProvider.getLoudnessSensorDao().delete((List<DbLoudnessSensor>) values);
                    break;

                case SensorApiType.RUNNING_PROCESSES:
                    daoProvider.getRunningProcessesSensorDao().delete((List<DbRunningProcessesSensor>) values);
                    break;

                case SensorApiType.RUNNING_SERVICES:
                    daoProvider.getRunningServicesSensorDao().delete((List<DbRunningServicesSensor>) values);
                    break;

                case SensorApiType.RUNNING_TASKS:
                    daoProvider.getRunningTasksSensorDao().delete((List<DbRunningTasksSensor>) values);
                    break;

                case SensorApiType.RINGTONE:
                    daoProvider.getRingtoneSensorDao().delete((List<DbRingtoneSensor>) values);
                    break;

                case SensorApiType.GYROSCOPE:
                    daoProvider.getGyroscopeSensorDao().delete((List<DbGyroscopeSensor>) values);
                    break;

                case SensorApiType.MAGNETIC_FIELD:
                    daoProvider.getMagneticFieldSensorDao().delete((List<DbMagneticFieldSensor>) values);
                    break;

                case SensorApiType.BROWSER_HISTORY:
                    daoProvider.getBrowserHistorySensorDao().delete((List<DbBrowserHistorySensor>) values);
                    break;

                case SensorApiType.CALL_LOG:
                    daoProvider.getCallLogSensorDao().delete((List<DbCallLogSensor>) values);
                    break;

                case SensorApiType.POWER_STATE:
                    daoProvider.getPowerStateSensorDao().delete((List<DbPowerStateSensor>) values);
                    break;

                case SensorApiType.POWER_LEVEL:
                    daoProvider.getPowerLevelSensorDao().delete((List<DbPowerLevelSensor>) values);
                    break;

                case SensorApiType.CALENDAR:
                    List<DbCalendarSensor> calendarEvents = (List<DbCalendarSensor>) values;

                    for (DbCalendarSensor calEvent : calendarEvents) {
                        calEvent.setIsNew(Boolean.FALSE);
                    }

                    // update events state
                    daoProvider.getCalendarSensorDao().update(calendarEvents);
                    break;

                case SensorApiType.CONTACT:
                    List<DbContactSensor> calendarSensors = (List<DbContactSensor>) values;

                    for (DbContactSensor contactEvent : calendarSensors) {
                        contactEvent.setIsNew(Boolean.FALSE);
                    }

                    // update events state
                    daoProvider.getContactSensorDao().update(calendarSensors);
                    break;

                case SensorApiType.UNI_TUCAN:

                    // marking as not changed
                    List<DbTucanSensor> tucanSensors = (List<DbTucanSensor>) values;

                    for (DbTucanSensor sensor : tucanSensors) {
                        sensor.setWasChanged(Boolean.FALSE);
                    }

                    // update events state
                    daoProvider.getTucanSensorDao().update(tucanSensors);

                    break;

                case SensorApiType.SOCIAL_FACEBOOK:

                    // marking as not changed
                    List<DbFacebookSensor> facebookSensors = (List<DbFacebookSensor>) values;

                    for (DbFacebookSensor sensor : facebookSensors) {
                        sensor.setWasChanged(Boolean.FALSE);
                    }

                    // update events state
                    daoProvider.getFacebookSensorDao().update(facebookSensors);

                    break;
            }
        }

        Log.d(TAG, "Finished removing data from db");
    }
}