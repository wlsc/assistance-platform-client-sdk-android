package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.EventUploadRequestDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.endpoint.EndpointGenerator;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.endpoint.EventUploadEndpoint;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.ConnectionUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class EventUploaderService extends GcmTaskService {

    private static final String TAG = EventUploaderService.class.getSimpleName();

    private static final int EVENTS_NUMBER_TO_SPLIT_AFTER = 500;
    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS = 80;

    // task identifier
    private static final long taskID = 999;
    // the task should be executed every N seconds
    private static final long periodSecs = 60l;
    // fallback for period in case of server is not available
    private static final long periodServerNotAvailableFallbackSecs = 300l;
    // the task can run as early as N seconds from the scheduled time
    private static final long flexSecs = 15l;
    // fallback for flexibility in case of server not available
    private static final long flexServerNotAvailableFallbackSecs = 100l;

    // an unique default task identifier
    private static String taskTagDefault = "periodic | " +
            taskID + ": " +
            periodSecs + "s, f:" +
            flexSecs;

    // an unique connection fallback task identifier
    private static String taskTagFallback = "periodic | " +
            taskID + ": " +
            periodServerNotAvailableFallbackSecs +
            "s, f:" +
            flexServerNotAvailableFallbackSecs;

    private static final String UPLOAD_ALL_FLAG_NAME = "UPLOAD_ALL";
    private static final int UPLOAD_ALL_FLAG = 1;

    private static PreferenceProvider mPreferenceProvider;

    private Map<Integer, List<? extends IDbSensor>> dbEvents = new HashMap<>();
    private Map<Integer, List<? extends SensorDto>> requestEvents = new HashMap<>();

    private static boolean isNeedInConnectionFallback;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Initializing...");

        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(getApplicationContext());
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken != null && !userToken.isEmpty()) {

            cancelAllTasks(getApplicationContext());

            if (isNeedInConnectionFallback) {
                schedulePeriodicTask(getApplicationContext(),
                        periodServerNotAvailableFallbackSecs,
                        flexServerNotAvailableFallbackSecs,
                        taskTagFallback);
            } else {
                schedulePeriodicTask(getApplicationContext(), periodSecs, flexSecs, taskTagDefault);
            }
        } else {
            Log.d(TAG, "User is not logged in. Scheduled task won't start");
        }

        Log.d(TAG, "Finished!");
    }

    @Override
    public int onRunTask(TaskParams taskParams) {

        Log.d(TAG, "Task uploader has started");

        // check Airplane Mode enabled
        if (ConnectionUtils.isAirplaneModeEnabled(getApplicationContext())) {
            Log.d(TAG, "Airplane Mode enabled. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        // device is not online
        if (!ConnectionUtils.isOnline(getApplicationContext())) {
            Log.d(TAG, "Device is not online. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        boolean isPeriodic = true;

        if (taskParams != null) {

            Bundle extras = taskParams.getExtras();

            if (extras != null) {

                int value = extras.getInt(UPLOAD_ALL_FLAG_NAME, 0);

                // upload all data at once
                if (value == UPLOAD_ALL_FLAG) {

                    isPeriodic = false;

                    Handler handler = new Handler(getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            final long serverDeviceId = mPreferenceProvider.getServerDeviceId();

                            Log.d(TAG, "Sync server device id: " + serverDeviceId);

                            // user logged out
                            if (serverDeviceId == -1) {
                                Log.d(TAG, "User logged out -- all tasks has been canceled!");
                                GcmNetworkManager.getInstance(getApplicationContext())
                                        .cancelAllTasks(EventUploaderService.class);
                                return;
                            }

                            getEntriesForUpload(0);

                            final List<SensorDto> eventsAsList = new ArrayList<>();

                            for (Map.Entry<Integer, List<? extends SensorDto>> entry : requestEvents.entrySet()) {
                                eventsAsList.addAll(entry.getValue());
                            }

                            // partial upload
                            int eventsSize = eventsAsList.size();

                            Log.d(TAG, "There are " + eventsSize + " events to upload");

                            if (eventsSize < EVENTS_NUMBER_TO_SPLIT_AFTER) {
                                // send as usual

                                Log.d(TAG, "Sending data in normal mode");

                                EventUploadRequestDto eventUploadRequest = new EventUploadRequestDto();
                                eventUploadRequest.setDataEvents(eventsAsList);
                                eventUploadRequest.setServerDeviceId(serverDeviceId);

                                doUploadEventData(eventUploadRequest);

                            } else {
                                // send partial with many requests
                                int howMuchToSend = eventsSize / EVENTS_NUMBER_TO_SPLIT_AFTER;

                                Log.d(TAG, "Sending partial data with " + (howMuchToSend + 1) + " requests");

                                for (int i = 0; i <= howMuchToSend; i++) {

                                    final int finalCounter = i;
                                    int lastElementIndex = (finalCounter + 1) * EVENTS_NUMBER_TO_SPLIT_AFTER;

                                    // assign last element index
                                    if (i == howMuchToSend) {
                                        lastElementIndex = eventsSize - 1;
                                    }

                                    final int finalLastElementIndex = lastElementIndex;

                                    AsyncTask.execute(new Runnable() {

                                        @Override
                                        public void run() {

                                            List<SensorDto> tmpSensors = eventsAsList
                                                    .subList(finalCounter * EVENTS_NUMBER_TO_SPLIT_AFTER,
                                                            finalLastElementIndex);

                                            EventUploadRequestDto eventUploadRequest = new EventUploadRequestDto();
                                            eventUploadRequest.setDataEvents(tmpSensors);
                                            eventUploadRequest.setServerDeviceId(serverDeviceId);

                                            doUploadEventData(eventUploadRequest);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        }

        // do periodic upload task
        if (isPeriodic) {

            Handler handler = new Handler(getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {

                    final long serverDeviceId = mPreferenceProvider.getServerDeviceId();

                    Log.d(TAG, "Sync server device id: " + serverDeviceId);

                    // user logged out
                    if (serverDeviceId == -1) {
                        Log.d(TAG, "User logged out -- all tasks has been canceled!");
                        GcmNetworkManager.getInstance(getApplicationContext())
                                .cancelAllTasks(EventUploaderService.class);
                        return;
                    }

                    getEntriesForUpload(PUSH_NUMBER_OF_EACH_ELEMENTS);

                    final List<SensorDto> eventsAsList = new ArrayList<>();

                    for (Map.Entry<Integer, List<? extends SensorDto>> entry : requestEvents.entrySet()) {
                        eventsAsList.addAll(entry.getValue());
                    }

                    // partial upload
                    int eventsSize = eventsAsList.size();

                    Log.d(TAG, "There are " + eventsSize + " events to upload");

                    if (eventsSize < EVENTS_NUMBER_TO_SPLIT_AFTER) {
                        // send as usual

                        Log.d(TAG, "Sending data in normal mode");

                        EventUploadRequestDto eventUploadRequest = new EventUploadRequestDto();
                        eventUploadRequest.setDataEvents(eventsAsList);
                        eventUploadRequest.setServerDeviceId(serverDeviceId);

                        doUploadEventData(eventUploadRequest);

                    } else {
                        // send partial with many requests
                        int howMuchToSend = eventsSize / EVENTS_NUMBER_TO_SPLIT_AFTER;

                        Log.d(TAG, "Sending partial data with " + (howMuchToSend + 1) + " requests");

                        for (int i = 0; i <= howMuchToSend; i++) {

                            final int finalCounter = i;
                            int lastElementIndex = (finalCounter + 1) * EVENTS_NUMBER_TO_SPLIT_AFTER;

                            // assign last element index
                            if (i == howMuchToSend) {
                                lastElementIndex = eventsSize - 1;
                            }

                            final int finalLastElementIndex = lastElementIndex;

                            AsyncTask.execute(new Runnable() {

                                @Override
                                public void run() {

                                    List<SensorDto> tmpSensors = eventsAsList
                                            .subList(finalCounter * EVENTS_NUMBER_TO_SPLIT_AFTER,
                                                    finalLastElementIndex);

                                    EventUploadRequestDto eventUploadRequest = new EventUploadRequestDto();
                                    eventUploadRequest.setDataEvents(tmpSensors);
                                    eventUploadRequest.setServerDeviceId(serverDeviceId);

                                    doUploadEventData(eventUploadRequest);
                                }
                            });
                        }
                    }
                }
            });
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    /**
     * Pushes events data to server
     *
     * @param eventUploadRequest
     */
    private void doUploadEventData(final EventUploadRequestDto eventUploadRequest) {

        Log.d(TAG, "Uploading data...");

        if (eventUploadRequest == null || eventUploadRequest.getDataEvents() == null) {
            Log.d(TAG, "eventUploadRequest is null or events list is empty!");
            return;
        }

        if (eventUploadRequest.getDataEvents().size() == 0) {
            Log.d(TAG, "No new data found");
            return;
        }

        // send to upload data service
        EventUploadEndpoint eventUploadEndpoint = EndpointGenerator
                .getInstance(getApplicationContext())
                .create(EventUploadEndpoint.class);

        String userToken = mPreferenceProvider.getUserToken();

        eventUploadEndpoint.uploadData(userToken, eventUploadRequest,
                new Callback<Void>() {

                    @Override
                    public void success(Void aVoid, Response response) {

                        if (response != null && (
                                response.getStatus() == 200 ||
                                        response.getStatus() == 204)) {

                            Log.d(TAG, "OK response from server received");

                            // successful transmission of event data -> remove that data from db
                            removeDbSentEvents();

                            // reschedule default periodic task
                            if (isNeedInConnectionFallback) {
                                isNeedInConnectionFallback = false;

                                rescheduleDefaultPeriodicTask();
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // TODO process error
                        Log.d(TAG, "Server returned error! Kind: " + error.getKind().name());

                        // fallbacking periodic request
                        if (!isNeedInConnectionFallback) {
                            isNeedInConnectionFallback = true;

                            rescheduleFallbackPeriodicTask();
                        }
                    }
                });
    }

    /**
     * Rescheduling default task by canceling fallback one
     */
    private void rescheduleDefaultPeriodicTask() {

        Log.d(TAG, "Rescheduling default periodic task...");

        // cancelling fallback periodic task
        cancelByTag(getApplicationContext(), taskTagFallback);

        // reschedule periodic task with default timings
        schedulePeriodicTask(getApplicationContext(),
                periodSecs,
                flexSecs,
                taskTagDefault);
    }

    /**
     * Rescheduling fallback task by canceling default one
     */
    private void rescheduleFallbackPeriodicTask() {

        Log.d(TAG, "Rescheduling fallback periodic task...");

        // cancelling default periodic task
        cancelByTag(getApplicationContext(), taskTagDefault);

        // reschedule periodic task with fallback timings
        schedulePeriodicTask(getApplicationContext(),
                periodServerNotAvailableFallbackSecs,
                flexServerNotAvailableFallbackSecs,
                taskTagFallback);
    }

    @Override
    public void onInitializeTasks() {

        Log.d(TAG, "Reinitialize periodic task...");

        // first cancel any running event uploader tasks
        cancelAllTasks(getApplicationContext());

        if (isNeedInConnectionFallback) {
            schedulePeriodicTask(getApplicationContext(),
                    periodServerNotAvailableFallbackSecs,
                    flexServerNotAvailableFallbackSecs,
                    taskTagFallback);
        } else {
            schedulePeriodicTask(getApplicationContext(), periodSecs, flexSecs, taskTagDefault);
        }

        super.onInitializeTasks();
    }

    /**
     * Returns events for upload to server
     *
     * @param numberOfElements
     * @return
     */
    public void getEntriesForUpload(int numberOfElements) {

        DaoProvider daoProvider = DaoProvider.getInstance(getApplicationContext());
        List<ISensor> sensors = SensorProvider.getInstance(getApplicationContext()).getEnabledSensors();

        for (ISensor sensor : sensors) {

            if (sensor == null) {
                continue;
            }

            int type = sensor.getType();

            switch (type) {
                case DtoType.ACCELEROMETER:

                    List<DbAccelerometerSensor> accList;

                    // give all
                    if (numberOfElements == 0) {
                        accList = daoProvider
                                .getAccelerometerSensorDao()
                                .getAll();
                    } else {
                        accList = daoProvider
                                .getAccelerometerSensorDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, accList);
                    requestEvents.put(type, daoProvider
                            .getAccelerometerSensorDao()
                            .convertObjects(accList));

                    break;

                case DtoType.LOCATION:

                    List<DbPositionSensor> posList;

                    // give all
                    if (numberOfElements == 0) {
                        posList = daoProvider
                                .getLocationSensorDao()
                                .getAll();
                    } else {
                        posList = daoProvider
                                .getLocationSensorDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, posList);
                    requestEvents.put(type, daoProvider
                            .getLocationSensorDao()
                            .convertObjects(posList));

                    break;

                case DtoType.MOTION_ACTIVITY:

                    List<DbMotionActivityEvent> maList;

                    // give all
                    if (numberOfElements == 0) {
                        maList = daoProvider
                                .getMotionActivityEventDao()
                                .getAll();
                    } else {
                        maList = daoProvider
                                .getMotionActivityEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, maList);
                    requestEvents.put(type, daoProvider
                            .getMotionActivityEventDao()
                            .convertObjects(maList));

                    break;

                case DtoType.FOREGROUND:

                    List<DbForegroundEvent> feList;

                    // give all
                    if (numberOfElements == 0) {
                        feList = daoProvider
                                .getForegroundEventDao()
                                .getAll();
                    } else {
                        feList = daoProvider
                                .getForegroundEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, feList);
                    requestEvents.put(type, daoProvider
                            .getForegroundEventDao()
                            .convertObjects(feList));

                    break;

                case DtoType.CONNECTION:

                    // connection
                    List<DbConnectionEvent> conList;

                    // give all
                    if (numberOfElements == 0) {
                        conList = daoProvider
                                .getConnectionEventDao()
                                .getAll();
                    } else {
                        conList = daoProvider
                                .getConnectionEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, conList);
                    requestEvents.put(type, daoProvider
                            .getConnectionEventDao()
                            .convertObjects(conList));

                    // mobile connection
                    List<DbMobileConnectionEvent> mobConList;

                    // give all
                    if (numberOfElements == 0) {
                        mobConList = daoProvider
                                .getMobileConnectionEventDao()
                                .getAll();
                    } else {
                        mobConList = daoProvider
                                .getMobileConnectionEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, mobConList);
                    requestEvents.put(type, daoProvider
                            .getMobileConnectionEventDao()
                            .convertObjects(mobConList));

                    // wifi connection
                    List<DbWifiConnectionEvent> wifiConList;

                    // give all
                    if (numberOfElements == 0) {
                        wifiConList = daoProvider
                                .getWifiConnectionEventDao()
                                .getAll();
                    } else {
                        wifiConList = daoProvider
                                .getWifiConnectionEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, wifiConList);
                    requestEvents.put(type, daoProvider
                            .getWifiConnectionEventDao()
                            .convertObjects(wifiConList));

                    break;

                case DtoType.FOREGROUND_TRAFFIC:

                    List<DbNetworkTrafficEvent> fteList;

                    // give all
                    if (numberOfElements == 0) {
                        fteList = daoProvider
                                .getNetworkTrafficEventDao()
                                .getAllForeground();
                    } else {
                        fteList = daoProvider
                                .getNetworkTrafficEventDao()
                                .getFirstNForeground(numberOfElements);
                    }

                    dbEvents.put(type, fteList);
                    requestEvents.put(type, daoProvider
                            .getNetworkTrafficEventDao()
                            .convertObjects(fteList));

                    break;

                case DtoType.BACKGROUND_TRAFFIC:

                    List<DbNetworkTrafficEvent> bteList;

                    // give all
                    if (numberOfElements == 0) {
                        bteList = daoProvider
                                .getNetworkTrafficEventDao()
                                .getAllBackground();
                    } else {
                        bteList = daoProvider
                                .getNetworkTrafficEventDao()
                                .getFirstNBackground(numberOfElements);
                    }

                    dbEvents.put(type, bteList);
                    requestEvents.put(type, daoProvider
                            .getNetworkTrafficEventDao()
                            .convertObjects(bteList));

                    break;

                case DtoType.LIGHT:

                    List<DbLightSensor> lightList;

                    // give all
                    if (numberOfElements == 0) {
                        lightList = daoProvider
                                .getLightSensorDao()
                                .getAll();
                    } else {
                        lightList = daoProvider
                                .getLightSensorDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, lightList);
                    requestEvents.put(type, daoProvider
                            .getLightSensorDao()
                            .convertObjects(lightList));

                    break;
            }
        }
    }

    /**
     * Removes successful transmitted entries from database
     */
    public void removeDbSentEvents() {

        Log.d(TAG, "Removing sent events from db...");

        DaoProvider daoProvider = DaoProvider.getInstance(getApplicationContext());

        for (Map.Entry<Integer, List<? extends IDbSensor>> entry : dbEvents.entrySet()) {

            if (entry == null) {
                continue;
            }

            int type = entry.getKey();
            List<? extends IDbSensor> values = entry.getValue();

            switch (type) {
                case DtoType.ACCELEROMETER:
                    daoProvider.getAccelerometerSensorDao().delete((List<DbAccelerometerSensor>) values);
                    break;

                case DtoType.LOCATION:
                    daoProvider.getLocationSensorDao().delete((List<DbPositionSensor>) values);
                    break;

                case DtoType.MOTION_ACTIVITY:
                    daoProvider.getMotionActivityEventDao().delete((List<DbMotionActivityEvent>) values);
                    break;

                case DtoType.FOREGROUND:
                    daoProvider.getForegroundEventDao().delete((List<DbForegroundEvent>) values);
                    break;

                case DtoType.FOREGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficEventDao().delete((List<DbNetworkTrafficEvent>) values);
                    break;

                case DtoType.BACKGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficEventDao().delete((List<DbNetworkTrafficEvent>) values);
                    break;

                case DtoType.CONNECTION:
                    daoProvider.getConnectionEventDao().delete((List<DbConnectionEvent>) values);
                    break;

                case DtoType.MOBILE_DATA_CONNECTION:
                    daoProvider.getMobileConnectionEventDao().delete((List<DbMobileConnectionEvent>) values);
                    break;

                case DtoType.WIFI_CONNECTION:
                    daoProvider.getWifiConnectionEventDao().delete((List<DbWifiConnectionEvent>) values);
                    break;

                case DtoType.LIGHT:
                    daoProvider.getLightSensorDao().delete((List<DbLightSensor>) values);
                    break;
            }
        }

        if (requestEvents != null) {
            requestEvents.clear();
        }

        Log.d(TAG, "Finished removing data from db");
    }

    /**
     * Cancels all GCM Network Manager tasks
     */
    public static void cancelAllTasks(Context context) {
        GcmNetworkManager.getInstance(context).cancelAllTasks(EventUploaderService.class);
    }

    /**
     * Cancels default periodic task
     */
    public static void cancel(Context context) {
        GcmNetworkManager.getInstance(context).cancelTask(taskTagDefault, EventUploaderService.class);
    }

    /**
     * Cancels GCM Network Manager periodic task
     */
    public static void cancelByTag(Context context, String tag) {
        GcmNetworkManager.getInstance(context).cancelTask(tag, EventUploaderService.class);
    }

    /**
     * Schedules an periodic upload task
     */
    public static void schedulePeriodicTask(Context context, long paramPeriodSecs, long paramFlexSecs, String tag) {

        Log.d(TAG, "Scheduling periodic task...");

        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(EventUploaderService.class)
                .setPeriod(paramPeriodSecs)
                .setFlex(paramFlexSecs)
                .setTag(tag)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();

        GcmNetworkManager.getInstance(context).schedule(periodicTask);

        Log.d(TAG, "Periodic task scheduled!");
    }

    /**
     * Schedules an periodic upload task
     */
    public static void scheduleOneTimeTask(Context context, long startSecs, long endSecs, String tag) {

        Log.d(TAG, "Scheduling one time task...");

        Bundle bundle = new Bundle();
        bundle.putInt(UPLOAD_ALL_FLAG_NAME, UPLOAD_ALL_FLAG);

        OneoffTask oneTimeTask = new OneoffTask.Builder()
                .setService(EventUploaderService.class)
                .setExecutionWindow(startSecs, endSecs)
                .setTag(tag)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .setExtras(bundle)
                .build();

        GcmNetworkManager.getInstance(context).schedule(oneTimeTask);

        Log.d(TAG, "One time task was scheduled!");
    }
}
