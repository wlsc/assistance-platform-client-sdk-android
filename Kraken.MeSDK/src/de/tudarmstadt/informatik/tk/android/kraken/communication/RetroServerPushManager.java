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
import de.tudarmstadt.informatik.tk.android.kraken.communication.services.EventUploadService;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbUpdatableSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.android.kraken.services.KrakenService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RetroServerPushManager {

    private static final String TAG = RetroServerPushManager.class.getSimpleName();

    private static final int PERIODIC_PUSH_DELAY_IN_MIN = 0;
    private static final int PERIODIC_PUSH_PERIOD_IN_MIN = 1;

    private static RetroServerPushManager instance;

    private HashSet<ISensor> mSensorsImmediate = new HashSet<ISensor>();
    private HashMap<ISensor, Long> mSensorsPeriodic = new HashMap<ISensor, Long>();
    private HashSet<ISensor> mSensorsWlan = new HashSet<ISensor>();
    private boolean mIsWlanConnected = false;

    private static Future<?> mFuture;
    protected ScheduledExecutorService mScheduledTaskExecutor;
    private static Context mContext;

    public static RetroServerPushManager getInstance(Context ctx) {

        if (instance == null) {
            instance = new RetroServerPushManager();
        }

        if (mFuture == null) {
            // TODO: enabled it to push data periodically
//            instance.startPeriodicPush();
        }

        instance.mContext = ctx.getApplicationContext();

        return instance;
    }

    private RetroServerPushManager() {
        mScheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setWlanConnected(boolean bConnected) {
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

        if (mFuture == null) {
            mFuture = mScheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "periodic flush");
                    flushData(EPushType.PERIODIC);
                }
            }, PERIODIC_PUSH_DELAY_IN_MIN, PERIODIC_PUSH_PERIOD_IN_MIN, TimeUnit.MINUTES);
        }
    }

    public static void stopPeriodicPush() {

        Log.d(TAG, "Stopped periodic push of data!");

        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    // TODO Wäre ein if (m_setImmediate.contains(sensor) || m_bIsWlanConnected) hier sinnvoller?
    // Es müsste dann nicht bei jedem inform jeder Sensor abgefragt werden. (Einige Queries werden gespart)

    // TODO: Was passiert, wenn ein MANUAL PushSensor hier informt?
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

    private void flushData(ISensor sensor) {

        Log.d(TAG, "flushData: " + sensor.getSensorType().getSensorName());

//        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
//        if (kroken == null)
//            return;

        EventUploadRequest eventUploadRequest = new EventUploadRequest();
        List<Sensor> sensorDataEvents = sensor.flushDataRetro();

        if (sensorDataEvents != null) {

            eventUploadRequest.setDataEvents(sensorDataEvents);

            sendSensorData(eventUploadRequest);
        }
    }

    public void flushAll() {
        flushData(EPushType.ALL);
    }

    private void flushData(EPushType type) {

        Log.d(TAG, "flushData: " + type.name());

//        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
//        if (kroken == null)
//            return;

        EventUploadRequest eventUploadRequest = new EventUploadRequest();
        buildSensorsDataArray(type, eventUploadRequest);
        sendSensorData(eventUploadRequest);
    }

    private void sendSensorData(final EventUploadRequest eventUploadRequest) {

        if (eventUploadRequest == null) {
            return;
        }

        // add cached data!
//        addCachedDataToArray(dataWrappers);

        if (eventUploadRequest.getDataEvents().size() == 0) {
            return;
        }

        // send to upload data service
        EventUploadService eventUploadService = ServiceGenerator.createService(EventUploadService.class);

        // TODO: get user token
        String userToken = "";

        eventUploadService.uploadData(userToken, eventUploadRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    // successful transmission of event data -> remove that data from db
                    // TODO: remove event data from db after successful transmission

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
