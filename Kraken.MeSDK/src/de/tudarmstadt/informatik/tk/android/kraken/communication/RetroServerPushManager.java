package de.tudarmstadt.informatik.tk.android.kraken.communication;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.dao.AbstractDao;
import de.tudarmstadt.informatik.tk.android.kraken.KrakenServiceManager;
import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.communication.endpoint.EventUploadEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.db.DatabaseManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbUpdatableSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.AccelerometerSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.PositionSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.android.kraken.service.KrakenService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Service to push data saved on the device to a server
 */
public class RetroServerPushManager {

    private static final String TAG = RetroServerPushManager.class.getSimpleName();

    private static final int PERIODIC_PUSH_DELAY_IN_MIN = 0;
    private static final int PERIODIC_PUSH_PERIOD_IN_MIN = 1;
    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS = 50;

    private static RetroServerPushManager INSTANCE;

    private static KrakenServiceManager krakenServiceManager;

    private HashSet<ISensor> mSensorsImmediate = new HashSet<ISensor>();
    private HashMap<ISensor, Long> mSensorsPeriodic = new HashMap<ISensor, Long>();
    private HashSet<ISensor> mSensorsWlan = new HashSet<ISensor>();
    private boolean mIsWlanConnected = false;

    private static Future<?> mFuture;
    protected ScheduledExecutorService mScheduledTaskExecutor;
    private static Context mContext;

    private static PreferenceManager mPreferenceManager;

    private static DbAccelerometerSensorDao dbAccelerometerSensorDao;
    private static DbPositionSensorDao dbPositionSensorDao;

    private List<DbAccelerometerSensor> dbAccelerometerSensors;
    private List<DbPositionSensor> dbPositionSensors;

    private RetroServerPushManager(Context ctx) {

        mContext = ctx;
        mScheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public static RetroServerPushManager getInstance(Context ctx) {

        if (INSTANCE == null) {
            INSTANCE = new RetroServerPushManager(ctx);
        }

        if (mFuture == null) {
            INSTANCE.startPeriodicPush();
        }

        if (mPreferenceManager == null) {
            mPreferenceManager = PreferenceManager.getInstance(ctx);
        }

        return INSTANCE;
    }

    public void setWLANConnected(boolean bConnected) {

        if (bConnected) {
            mIsWlanConnected = true;
            stopPeriodicPush();
            flushAll();
        } else {
            mIsWlanConnected = false;
            startPeriodicPush();
        }
    }

    private void startPeriodicPush() {

        Log.d(TAG, "Starting periodic push of data...");

        if (krakenServiceManager == null) {
            krakenServiceManager = KrakenServiceManager.getInstance(mContext);
        }

        krakenServiceManager.showIcon(true);

        if (mFuture == null) {

            mFuture = mScheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {

                    Log.d(TAG, "Invoking periodic push data to server...");

                    flushData(EPushType.PERIODIC);
                }
            }, PERIODIC_PUSH_DELAY_IN_MIN, PERIODIC_PUSH_PERIOD_IN_MIN, TimeUnit.MINUTES);
        }

        if (dbAccelerometerSensorDao == null) {
            dbAccelerometerSensorDao = DatabaseManager.getInstance(mContext).getDaoSession().getDbAccelerometerSensorDao();
        }

        if (dbPositionSensorDao == null) {
            dbPositionSensorDao = DatabaseManager.getInstance(mContext).getDaoSession().getDbPositionSensorDao();
        }
    }

    public static void stopPeriodicPush() {

        Log.d(TAG, "Stopped periodic push of data!");

        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }

        if (krakenServiceManager == null) {
            krakenServiceManager = KrakenServiceManager.getInstance(mContext);
        }

        krakenServiceManager.showIcon(false);

        dbAccelerometerSensorDao = null;
        dbPositionSensorDao = null;
    }

    public void inform(ISensor sensor) {

        Log.d(TAG, "inform: " + sensor.getSensorType().getSensorName());

        if (mSensorsImmediate.contains(sensor)) {
            flushData(sensor);
        } else if (mIsWlanConnected) {
            flushAll();
        }
    }

    public void setPushType(ISensor sensor, EPushType type) {
        switch (type) {
            case IMMEDIATE:
                mSensorsPeriodic.remove(sensor);
                mSensorsWlan.remove(sensor);
                mSensorsImmediate.add(sensor);
                break;
            case PERIODIC:
                mSensorsImmediate.remove(sensor);
                mSensorsWlan.remove(sensor);
                mSensorsPeriodic.put(sensor, null);
                break;
            case WLAN_ONLY:
                mSensorsImmediate.remove(sensor);
                mSensorsPeriodic.remove(sensor);
                mSensorsWlan.add(sensor);
                break;
        }
    }

    /**
     * Flushing data of sensors
     *
     * @param sensor
     */
    private void flushData(ISensor sensor) {

        Log.d(TAG, "flushData: " + sensor.getSensorType().getSensorName());

//        EventUploadRequest eventUploadRequest = new EventUploadRequest();
//        List<Sensor> sensorDataEvents = sensor.flushDataRetro();
//
//        if (sensorDataEvents != null) {
//
//            eventUploadRequest.setDataEvents(sensorDataEvents);
//
//            sendSensorData(eventUploadRequest);
//        }
    }

    public void flushAll() {
        flushData(EPushType.ALL);
    }

    /**
     * Flush data and send it to server
     *
     * @param type
     */
    private void flushData(EPushType type) {

        Log.d(TAG, "flushData: " + type.name());

        EventUploadRequest eventUploadRequest = new EventUploadRequest();

        long deviceServerId = mPreferenceManager.getCurrentDeviceServerId();

        Log.d(TAG, "Current server device id: " + deviceServerId);

        List<Sensor> events = new LinkedList<>();

        events.addAll(getAccelerometerEntries());
        events.addAll(getPositionEntries());

        eventUploadRequest.setDataEvents(events);
        eventUploadRequest.setServerDeviceId(deviceServerId);

//        buildSensorsDataArray(type, eventUploadRequest);
        sendSensorData(eventUploadRequest);
    }

    /**
     * Harvest particular number of accelerometer sensor entries from database
     *
     * @return
     */
    private List<Sensor> getAccelerometerEntries() {

        List<Sensor> result = new LinkedList<>();

        dbAccelerometerSensors = dbAccelerometerSensorDao
                .queryBuilder()
                .limit(PUSH_NUMBER_OF_EACH_ELEMENTS)
                .build()
                .list();

        if (dbAccelerometerSensors != null) {

            for (int i = 0; i < dbAccelerometerSensors.size(); i++) {

                DbAccelerometerSensor sensor = dbAccelerometerSensors.get(i);

                AccelerometerSensorRequest accelerometerSensorRequest = new AccelerometerSensorRequest();

                accelerometerSensorRequest.setType(SensorType.ACCELEROMETER);
                accelerometerSensorRequest.setTypeStr(SensorType.getApiName(SensorType.ACCELEROMETER));
                accelerometerSensorRequest.setX(sensor.getX());
                accelerometerSensorRequest.setY(sensor.getY());
                accelerometerSensorRequest.setZ(sensor.getZ());
                accelerometerSensorRequest.setAccuracy(sensor.getAccuracy());
                accelerometerSensorRequest.setCreated(sensor.getCreated());

                result.add(accelerometerSensorRequest);
            }
        }

        return result;
    }

    /**
     * Returns number of first entries of position events from database
     *
     * @return
     */
    private List<Sensor> getPositionEntries() {

        List<Sensor> result = new LinkedList<>();

        dbPositionSensors = dbPositionSensorDao
                .queryBuilder()
                .limit(PUSH_NUMBER_OF_EACH_ELEMENTS)
                .build()
                .list();

        if (dbPositionSensors != null) {

            for (int i = 0; i < dbPositionSensors.size(); i++) {

                DbPositionSensor sensor = dbPositionSensors.get(i);

                PositionSensorRequest positionSensorRequest = new PositionSensorRequest();

                positionSensorRequest.setType(SensorType.POSITION);
                positionSensorRequest.setTypeStr(SensorType.getApiName(SensorType.POSITION));
                positionSensorRequest.setLatitude(sensor.getLatitude());
                positionSensorRequest.setLongitude(sensor.getLongitude());
                positionSensorRequest.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
                positionSensorRequest.setAccuracyVertical(sensor.getAccuracyVertical());
                positionSensorRequest.setAltitude(sensor.getAltitude());
                positionSensorRequest.setSpeed(sensor.getSpeed());
                positionSensorRequest.setCreated(sensor.getCreated());

                result.add(positionSensorRequest);
            }
        }

        return result;
    }

    /**
     * Pushes events data to server
     *
     * @param eventUploadRequest
     */
    private void sendSensorData(final EventUploadRequest eventUploadRequest) {

        if (eventUploadRequest == null || eventUploadRequest.getDataEvents() == null) {
            return;
        }

        // add cached data!
//        addCachedDataToArray(dataWrappers);

        if (eventUploadRequest.getDataEvents().size() == 0) {
            return;
        }

        // send to upload data service
        EventUploadEndpoint eventUploadEndpoint = ServiceGenerator.createService(EventUploadEndpoint.class);

        String userToken = mPreferenceManager.getUserToken();

        eventUploadEndpoint.uploadData(userToken, eventUploadRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    // successful transmission of event data -> remove that data from db
                    removeDbEventsSent();
                } else {
                    // TODO: show error
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO process error
            }
        });
    }

    /**
     * Removes successful transmitted entries from database
     */
    private void removeDbEventsSent() {

        Log.d(TAG, "Removing sent event data from db...");

        if (dbAccelerometerSensors != null) {

            if (dbAccelerometerSensorDao == null) {
                dbAccelerometerSensorDao = DatabaseManager.getInstance(mContext).getDaoSession().getDbAccelerometerSensorDao();
            }

            dbAccelerometerSensorDao.deleteInTx(dbAccelerometerSensors);
        }

        if (dbPositionSensors != null) {

            if (dbPositionSensorDao == null) {
                dbPositionSensorDao = DatabaseManager.getInstance(mContext).getDaoSession().getDbPositionSensorDao();
            }

            dbPositionSensorDao.deleteInTx(dbPositionSensors);
        }

        Log.d(TAG, "Finished removing data from db");
    }

    private void buildSensorsDataArray(EPushType type, EventUploadRequest eventUploadRequest) {

        switch (type) {
            case IMMEDIATE:
//                addSensorsToArray(mSensorsImmediate, dataWrappers);
                break;
            case PERIODIC:
                addPeriodicPushingSensorsToArray(mSensorsPeriodic, eventUploadRequest);
                break;
            case WLAN_ONLY:
//                addSensorsToArray(mSensorsWlan, dataWrappers);
                break;
            case ALL: {
//                addSensorsToArray(mSensorsImmediate, dataWrappers);
//                addSensorsToArray(mSensorsPeriodic.keySet(), dataWrappers);
//                addSensorsToArray(mSensorsWlan, dataWrappers);
                break;
            }
        }
    }

//    private void addSensorsToArray(Set<ISensor> set, EventUploadRequest eventUploadRequest) {
//
//        for (ISensor sensor : set) {
//            ApiMessage.DataWrapper data = sensor.flushDataRetro();
//            if (data != null) {
//                eventUploadRequest.setDataEvents(data);
//            }
//        }
//    }

    private void addPeriodicPushingSensorsToArray(HashMap<ISensor, Long> map, EventUploadRequest eventUploadRequest) {

        long longCurrentTimestamp = Calendar.getInstance().getTimeInMillis();

        for (Map.Entry<ISensor, Long> entry : map.entrySet()) {
            ISensor sensor = entry.getKey();
            Long lastSensorPush = entry.getValue();
            if (lastSensorPush == null) {
                entry.setValue(longCurrentTimestamp);
                continue;
            }

            int intPushIntervall = sensor.getPushIntervalInMin();
            long longPushIntervall = intPushIntervall * 60 * 1000;

            if (lastSensorPush + longPushIntervall < longCurrentTimestamp) {
                List<Sensor> data = sensor.flushDataRetro();
                if (data != null) {
                    eventUploadRequest.setDataEvents(data);
                }
                entry.setValue(longCurrentTimestamp);
            }
        }
        Log.d(TAG, "addPeriodicPushingSensorsToArray: " + eventUploadRequest.getDataEvents().size());
    }

    private void addCachedDataToArray(EventUploadRequest eventUploadRequest) {

        Log.d(TAG, "addCachedDataToArray before: " + eventUploadRequest.getDataEvents().size());
//        for (ApiMessage.DataWrapper data : mCache) {
//            dataWrappers.add(data);
//        }
//        mCache.clear();
//
//        if (mIsWlanConnected) {
//            for (ApiMessage.DataWrapper data : mCacheWlan) {
//                dataWrappers.add(data);
//            }
//            mCacheWlan.clear();
//        }
//        Log.d(TAG, "addCachedDataToArray after:  " + dataWrappers.size());
    }

    /**
     * This method is invoked by Sensors which handle the sending of items by their own.
     * These are sensors with more than one database table. (e.g. calendar or contacts sensor)
     */
    public void flushManually(EPushType pushType, List<Sensor> sensors) {

        Log.d(TAG, "flushManually: " + sensors.size());

        EventUploadRequest eventUploadRequest = new EventUploadRequest();

        if (sensors.size() == 0)
            return;

        for (Sensor sensor : sensors) {

            if (sensor == null) {
                continue;
            }

            if (!mIsWlanConnected && (EPushType.MANUALLY_WLAN_ONLY.equals(pushType) || EPushType.WLAN_ONLY.equals(pushType))) {
//                addToCache(pushType, sensors);
                return;
            }

            eventUploadRequest.setDataEvents(sensors);
        }

        sendSensorData(eventUploadRequest);
    }

//    public void addToCache(EPushType pushType, ApiMessage.DataWrapper... dataWrappers) {
//
//        Log.d(TAG, "addToCache: " + dataWrappers.length);
//        for (ApiMessage.DataWrapper data : dataWrappers) {
//            if (pushType.equals(EPushType.MANUALLY_IMMEDIATE) || pushType.equals(EPushType.IMMEDIATE) || pushType.equals(EPushType.PERIODIC)) {
//                mCache.add(data);
//            } else if (pushType.equals(EPushType.MANUALLY_WLAN_ONLY) || pushType.equals(EPushType.WLAN_ONLY)) {
//                mCacheWlan.add(data);
//            }
//        }
//    }

    public void removeDataFromDb(List<? extends IDbSensor> liSensorData, String strFullqualifiedSensorClassName) {

        Log.d(TAG, "removeDataFromDb: " + liSensorData.size() + ", " + strFullqualifiedSensorClassName);

        try {

            Class<? extends IDbSensor> dbClass = (Class<? extends
                    IDbSensor>) Class.forName(strFullqualifiedSensorClassName);
            AbstractDao<? extends IDbSensor, Long> dao = (AbstractDao<?
                    extends IDbSensor, Long>) KrakenService.getInstance()
                    .getDaoSession().getDao(dbClass);

            // We assume that every entry in this list is of the same type!
            if (liSensorData != null && liSensorData.size() > 0) {
                // UpdatableSensors won't be simply deleted from database. May
                // be we have to update the flags!
                if (liSensorData.get(0) instanceof IDbUpdatableSensor) {
                    List<IDbUpdatableSensor> liDelete = new LinkedList<>();
                    List<IDbUpdatableSensor> liUpdate = new LinkedList<>();
                    for (IDbSensor data : liSensorData) {
                        IDbUpdatableSensor event = (IDbUpdatableSensor) data;
                        if (event.getIsDeleted())
                            liDelete.add(event);
                        else {
                            event.setIsNew(false);
                            event.setIsUpdated(false);
                            liUpdate.add(event);
                        }
                    }
                    dao.updateInTx((Iterable) liUpdate);
                    dao.deleteInTx((Iterable) liDelete);
                } else {
                    dao.deleteInTx((Iterable) liSensorData);
                }
            }

        } catch (ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
