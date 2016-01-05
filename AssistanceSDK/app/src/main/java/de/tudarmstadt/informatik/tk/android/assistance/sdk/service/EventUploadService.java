package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbBrowserHistorySensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.login.LoginApi;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.login.LoginResponseDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.login.UserDeviceDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorUploadApi;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorUploadDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.calendar.CalendarSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.calendar.CalendarReminderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.HardwareUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class EventUploadService extends GcmTaskService {

    private static final String TAG = EventUploadService.class.getSimpleName();

    private static final int EVENTS_NUMBER_TO_SPLIT_AFTER = 500;
    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS = 80;

    // task identifier
    private static final long taskID = 998;
    // the task should be executed every N seconds
    private static final long periodSecs = 60l;
    // fallback for period in case of server is not available
    private static final long periodServerNotAvailableFallbackSecs = 300l;
    // the task can run as early as N seconds from the scheduled time
    private static final long flexSecs = 15l;
    // fallback for flexibility in case of server not available
    private static final long flexServerNotAvailableFallbackSecs = 100l;

    // an unique default task identifier
    @NonNull
    private static String taskTagDefault = "periodic | " +
            taskID + ": " +
            periodSecs + "s, f:" +
            flexSecs;

    // an unique connection fallback task identifier
    @NonNull
    private static String taskTagFallback = "periodic | " +
            taskID + ": " +
            periodServerNotAvailableFallbackSecs +
            "s, f:" +
            flexServerNotAvailableFallbackSecs;

    private static final String UPLOAD_ALL_FLAG_NAME = "UPLOAD_ALL";
    private static final int UPLOAD_ALL_FLAG = 1;

    private static PreferenceProvider mPreferenceProvider;

    @NonNull
    private SparseArray<List<? extends IDbSensor>> dbEvents = new SparseArray<>();
    @NonNull
    private SparseArray<List<? extends SensorDto>> requestEvents = new SparseArray<>();

    private static boolean isNeedInConnectionFallback;

    private Subscription subUserLogin;

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
    public int onRunTask(@Nullable TaskParams taskParams) {

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

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken.isEmpty()) {
            Log.d(TAG, "User is not logged in. Upload request ignored");
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
                    handler.post(() -> {

                        final long serverDeviceId = mPreferenceProvider.getServerDeviceId();

                        Log.d(TAG, "Sync server device id: " + serverDeviceId);

                        // user logged out
                        if (serverDeviceId == -1) {
                            Log.d(TAG, "User logged out -- all tasks has been canceled!");
                            GcmNetworkManager.getInstance(getApplicationContext())
                                    .cancelAllTasks(EventUploadService.class);
                            return;
                        }

                        getEntriesForUpload(0);

                        final List<SensorDto> eventsAsList = new ArrayList<>();

                        for (int i = 0, eventsSize = requestEvents.size(); i < eventsSize; i++) {
                            eventsAsList.addAll(requestEvents.valueAt(i));
                        }

                        Log.d(TAG, "There are " + eventsAsList.size() + " events to upload");

                        // send partial data with many requests
                        List<List<SensorDto>> eventParts = Lists
                                .partition(eventsAsList, EVENTS_NUMBER_TO_SPLIT_AFTER);

                        for (final List<SensorDto> partEvent : eventParts) {

                            AsyncTask.execute(() -> {

                                SensorUploadDto eventUploadRequest = new SensorUploadDto(
                                        serverDeviceId,
                                        partEvent
                                );

                                doUploadEventData(eventUploadRequest);
                            });
                        }
                    });
                }
            }
        }

        // do periodic upload task
        if (isPeriodic) {

            Handler handler = new Handler(getMainLooper());
            handler.post(() -> {

                final long serverDeviceId = mPreferenceProvider.getServerDeviceId();

                Log.d(TAG, "Sync server device id: " + serverDeviceId);

                // user logged out
                if (serverDeviceId == -1) {
                    Log.d(TAG, "User logged out -- all tasks has been canceled!");
                    GcmNetworkManager.getInstance(getApplicationContext())
                            .cancelAllTasks(EventUploadService.class);
                    return;
                }

                getEntriesForUpload(PUSH_NUMBER_OF_EACH_ELEMENTS);

                final List<SensorDto> eventsAsList = new ArrayList<>();

                for (int i = 0, eventsSize = requestEvents.size(); i < eventsSize; i++) {
                    eventsAsList.addAll(requestEvents.valueAt(i));
                }

                Log.d(TAG, "There are " + eventsAsList.size() + " events to upload");

                // send partial with many requests
                List<List<SensorDto>> eventParts = Lists
                        .partition(eventsAsList, EVENTS_NUMBER_TO_SPLIT_AFTER);

                for (final List<SensorDto> eventPart : eventParts) {

                    AsyncTask.execute(() -> {

                        SensorUploadDto eventUploadRequest = new SensorUploadDto(
                                serverDeviceId,
                                eventPart
                        );

                        doUploadEventData(eventUploadRequest);
                    });
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
    private void doUploadEventData(@Nullable final SensorUploadDto eventUploadRequest) {

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
        SensorUploadApi sensorUploadApi = ApiGenerator
                .getInstance(getApplicationContext())
                .create(SensorUploadApi.class);

        String userToken = mPreferenceProvider.getUserToken();

        sensorUploadApi.uploadData(userToken, eventUploadRequest,
                new Callback<Void>() {

                    @Override
                    public void success(Void aVoid, @Nullable Response response) {

                        if (response != null && (
                                response.getStatus() == 200 ||
                                        response.getStatus() == 204)) {

                            Log.d(TAG, "OK response from server received");

                            // successful transmission of event data -> remove that data from db
                            handleSentEvents();

                            // reschedule default periodic task
                            if (isNeedInConnectionFallback) {
                                isNeedInConnectionFallback = false;

                                rescheduleDefaultPeriodicTask();
                            }
                        }
                    }

                    @Override
                    public void failure(@NonNull RetrofitError error) {

                        Log.d(TAG, "Server returned error! Kind: " + error.getKind().name());

                        Response response = error.getResponse();

                        if (response != null) {

                            // user need relogin
                            if (response.getStatus() == 401) {
                                relogin();
                            }
                        }

                        // fallbacking periodic request
                        if (!isNeedInConnectionFallback) {
                            isNeedInConnectionFallback = true;

                            rescheduleFallbackPeriodicTask();
                        }
                    }
                });
    }

    /**
     * Does user token refresh
     */
    private void relogin() {

        final PreferenceProvider preferenceProvider = PreferenceProvider.getInstance(getApplicationContext());

        String email = preferenceProvider.getUserEmail();
        String password = preferenceProvider.getUserPassword();
        long serverDeviceId = preferenceProvider.getServerDeviceId();

        UserDeviceDto userDevice = new UserDeviceDto();

        if (serverDeviceId != -1) {
            userDevice.setServerId(serverDeviceId);
        } else {
            userDevice.setOs(Config.PLATFORM_NAME);
            userDevice.setOsVersion(HardwareUtils.getAndroidVersion());
            userDevice.setBrand(HardwareUtils.getDeviceBrandName());
            userDevice.setModel(HardwareUtils.getDeviceModelName());
            userDevice.setDeviceId(HardwareUtils.getAndroidId(this));
        }

        LoginRequestDto loginRequest = new LoginRequestDto(email, password, userDevice);

        LoginApi userEndpoint = ApiGenerator.getInstance(getApplicationContext())
                .create(LoginApi.class);

        subUserLogin = userEndpoint.loginUser(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponseDto>() {

                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof RetrofitError) {
                            // ignore by now
                            Log.d(TAG, "login function failed. server status: " +
                                    ((RetrofitError) e).getResponse().getStatus());
                        }
                    }

                    @Override
                    public void onNext(LoginResponseDto response) {

                        if (response != null) {
                            Log.d(TAG, "User token received: " + response.getUserToken());

                            preferenceProvider.setUserToken(response.getUserToken());

                        } else {
                            Log.d(TAG, "apiResponse is NULL");
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
        Map<Integer, ISensor> sensors = SensorProvider.getInstance(getApplicationContext()).getRunningSensors();

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

                case SensorApiType.LOCATION:

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

                case SensorApiType.MOTION_ACTIVITY:

                    List<DbMotionActivitySensor> maList;

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

                case SensorApiType.FOREGROUND:

                    List<DbForegroundSensor> feList;

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

                case SensorApiType.CONNECTION:

                    // connection
                    List<DbConnectionSensor> conList;

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
                    List<DbMobileConnectionSensor> mobConList;

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

                    dbEvents.put(SensorApiType.MOBILE_DATA_CONNECTION, mobConList);
                    requestEvents.put(SensorApiType.MOBILE_DATA_CONNECTION, daoProvider
                            .getMobileConnectionEventDao()
                            .convertObjects(mobConList));

                    // wifi connection
                    List<DbWifiConnectionSensor> wifiConList;

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

                    dbEvents.put(SensorApiType.WIFI_CONNECTION, wifiConList);
                    requestEvents.put(SensorApiType.WIFI_CONNECTION, daoProvider
                            .getWifiConnectionEventDao()
                            .convertObjects(wifiConList));

                    break;

                case SensorApiType.FOREGROUND_TRAFFIC:

                    List<DbNetworkTrafficSensor> fteList;

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

                case SensorApiType.BACKGROUND_TRAFFIC:

                    List<DbNetworkTrafficSensor> bteList;

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

                case SensorApiType.LIGHT:

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

                case SensorApiType.LOUDNESS:

                    List<DbLoudnessSensor> loudnessList;

                    // give all
                    if (numberOfElements == 0) {
                        loudnessList = daoProvider
                                .getLoudnessEventDao()
                                .getAll();
                    } else {
                        loudnessList = daoProvider
                                .getLoudnessEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, loudnessList);
                    requestEvents.put(type, daoProvider
                            .getLoudnessEventDao()
                            .convertObjects(loudnessList));

                    break;

                case SensorApiType.RUNNING_PROCESSES:

                    List<DbRunningProcessesSensor> runProccessList;

                    // give all
                    if (numberOfElements == 0) {
                        runProccessList = daoProvider
                                .getRunningProcessesEventDao()
                                .getAll();
                    } else {
                        runProccessList = daoProvider
                                .getRunningProcessesEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, runProccessList);
                    requestEvents.put(type, daoProvider
                            .getRunningProcessesEventDao()
                            .convertObjects(runProccessList));

                    break;

                case SensorApiType.RUNNING_SERVICES:

                    List<DbRunningServicesSensor> runServiceList;

                    // give all
                    if (numberOfElements == 0) {
                        runServiceList = daoProvider
                                .getRunningServicesEventDao()
                                .getAll();
                    } else {
                        runServiceList = daoProvider
                                .getRunningServicesEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, runServiceList);
                    requestEvents.put(type, daoProvider
                            .getRunningServicesEventDao()
                            .convertObjects(runServiceList));

                    break;

                case SensorApiType.RUNNING_TASKS:

                    List<DbRunningTasksSensor> runTaskList;

                    // give all
                    if (numberOfElements == 0) {
                        runTaskList = daoProvider
                                .getRunningTasksEventDao()
                                .getAll();
                    } else {
                        runTaskList = daoProvider
                                .getRunningTasksEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, runTaskList);
                    requestEvents.put(type, daoProvider
                            .getRunningTasksEventDao()
                            .convertObjects(runTaskList));

                    break;

                case SensorApiType.RINGTONE:

                    List<DbRingtoneSensor> ringtoneList;

                    // give all
                    if (numberOfElements == 0) {
                        ringtoneList = daoProvider
                                .getRingtoneEventDao()
                                .getAll();
                    } else {
                        ringtoneList = daoProvider
                                .getRingtoneEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, ringtoneList);
                    requestEvents.put(type, daoProvider
                            .getRingtoneEventDao()
                            .convertObjects(ringtoneList));

                    break;

                case SensorApiType.GYROSCOPE:

                    List<DbGyroscopeSensor> gyroList;

                    // give all
                    if (numberOfElements == 0) {
                        gyroList = daoProvider
                                .getGyroscopeSensorDao()
                                .getAll();
                    } else {
                        gyroList = daoProvider
                                .getGyroscopeSensorDao()
                                .getFirstN(numberOfElements);
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
                                .getAll();
                    } else {
                        mfList = daoProvider
                                .getMagneticFieldSensorDao()
                                .getFirstN(numberOfElements);
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
                                .getBrowserHistoryEventDao()
                                .getAll();
                    } else {
                        bhList = daoProvider
                                .getBrowserHistoryEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, bhList);
                    requestEvents.put(type, daoProvider
                            .getBrowserHistoryEventDao()
                            .convertObjects(bhList));

                    break;

                case SensorApiType.CALENDAR:

                    // retrieve all events
                    List<DbCalendarSensor> calendarList = daoProvider
                            .getCalendarEventDao()
                            .getAllUpdated();

                    List<SensorDto> calendarListConverted = daoProvider
                            .getCalendarEventDao()
                            .convertObjects(calendarList);

                    List<CalendarSensorDto> calendarListConvertedNew = new ArrayList<>(
                            calendarListConverted.size());
                    Set<CalendarReminder> eventRemindersConvertedNew = new HashSet<>();

                    final CalendarReminderEventDao calendarReminderEventDao = daoProvider
                            .getCalendarReminderEventDao();

                    for (SensorDto sensorDto : calendarListConverted) {

                        if (sensorDto == null) {
                            continue;
                        }

                        CalendarSensorDto calendarSensorDto = (CalendarSensorDto) sensorDto;
                        List<DbCalendarReminderSensor> eventReminders = calendarReminderEventDao
                                .getAllByEventId(Long.valueOf(calendarSensorDto.getEventId()));

                        if (eventReminders == null || eventReminders.isEmpty()) {
                            continue;
                        }

                        List<SensorDto> eventRemindersConverted = calendarReminderEventDao
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
                                .getCallLogEventDao()
                                .getAll();
                    } else {
                        callLogList = daoProvider
                                .getCallLogEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, callLogList);
                    requestEvents.put(type, daoProvider
                            .getCallLogEventDao()
                            .convertObjects(callLogList));

                    break;

                case SensorApiType.CONTACT:

                    List<DbContactSensor> contactsList;

                    // give all
                    if (numberOfElements == 0) {
                        contactsList = daoProvider
                                .getContactEventDao()
                                .getAll();
                    } else {
                        contactsList = daoProvider
                                .getContactEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, contactsList);
                    requestEvents.put(type, daoProvider
                            .getContactEventDao()
                            .convertObjects(contactsList));

                    break;

                case SensorApiType.POWER_LEVEL:

                    List<DbPowerLevelSensor> powerLevelList;

                    // give all
                    if (numberOfElements == 0) {
                        powerLevelList = daoProvider
                                .getPowerLevelEventDao()
                                .getAll();
                    } else {
                        powerLevelList = daoProvider
                                .getPowerLevelEventDao()
                                .getFirstN(numberOfElements);
                    }

                    dbEvents.put(type, powerLevelList);
                    requestEvents.put(type, daoProvider
                            .getPowerLevelEventDao()
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
                    .getPowerStateEventDao()
                    .getAll();
        } else {
            powerStateList = daoProvider
                    .getPowerStateEventDao()
                    .getFirstN(numberOfElements);
        }

        dbEvents.put(SensorApiType.POWER_STATE, powerStateList);
        requestEvents.put(SensorApiType.POWER_STATE, daoProvider
                .getPowerStateEventDao()
                .convertObjects(powerStateList));
    }

    /**
     * Removes successful transmitted entries from database
     */
    public void handleSentEvents() {

        Log.d(TAG, "Handling sent events...");

        DaoProvider daoProvider = DaoProvider.getInstance(getApplicationContext());

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
                    daoProvider.getMotionActivityEventDao().delete((List<DbMotionActivitySensor>) values);
                    break;

                case SensorApiType.FOREGROUND:
                    daoProvider.getForegroundEventDao().delete((List<DbForegroundSensor>) values);
                    break;

                case SensorApiType.FOREGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficEventDao().delete((List<DbNetworkTrafficSensor>) values);
                    break;

                case SensorApiType.BACKGROUND_TRAFFIC:
                    daoProvider.getNetworkTrafficEventDao().delete((List<DbNetworkTrafficSensor>) values);
                    break;

                case SensorApiType.CONNECTION:
                    daoProvider.getConnectionEventDao().delete((List<DbConnectionSensor>) values);
                    break;

                case SensorApiType.MOBILE_DATA_CONNECTION:
                    daoProvider.getMobileConnectionEventDao().delete((List<DbMobileConnectionSensor>) values);
                    break;

                case SensorApiType.WIFI_CONNECTION:
                    daoProvider.getWifiConnectionEventDao().delete((List<DbWifiConnectionSensor>) values);
                    break;

                case SensorApiType.LIGHT:
                    daoProvider.getLightSensorDao().delete((List<DbLightSensor>) values);
                    break;

                case SensorApiType.LOUDNESS:
                    daoProvider.getLoudnessEventDao().delete((List<DbLoudnessSensor>) values);
                    break;

                case SensorApiType.RUNNING_PROCESSES:
                    daoProvider.getRunningProcessesEventDao().delete((List<DbRunningProcessesSensor>) values);
                    break;

                case SensorApiType.RUNNING_SERVICES:
                    daoProvider.getRunningServicesEventDao().delete((List<DbRunningServicesSensor>) values);
                    break;

                case SensorApiType.RUNNING_TASKS:
                    daoProvider.getRunningTasksEventDao().delete((List<DbRunningTasksSensor>) values);
                    break;

                case SensorApiType.RINGTONE:
                    daoProvider.getRingtoneEventDao().delete((List<DbRingtoneSensor>) values);
                    break;

                case SensorApiType.GYROSCOPE:
                    daoProvider.getGyroscopeSensorDao().delete((List<DbGyroscopeSensor>) values);
                    break;

                case SensorApiType.MAGNETIC_FIELD:
                    daoProvider.getMagneticFieldSensorDao().delete((List<DbMagneticFieldSensor>) values);
                    break;

                case SensorApiType.BROWSER_HISTORY:
                    daoProvider.getBrowserHistoryEventDao().delete((List<DbBrowserHistorySensor>) values);
                    break;

                case SensorApiType.CALL_LOG:
                    daoProvider.getCallLogEventDao().delete((List<DbCallLogSensor>) values);
                    break;

                case SensorApiType.POWER_STATE:
                    daoProvider.getPowerStateEventDao().delete((List<DbPowerStateSensor>) values);
                    break;

                case SensorApiType.POWER_LEVEL:
                    daoProvider.getPowerLevelEventDao().delete((List<DbPowerLevelSensor>) values);
                    break;

                case SensorApiType.CALENDAR:
                    List<DbCalendarSensor> calendarEvents = (List<DbCalendarSensor>) values;

                    for (DbCalendarSensor calEvent : calendarEvents) {
                        calEvent.setIsNew(false);
                    }

                    // update events state
                    daoProvider.getCalendarEventDao().update(calendarEvents);
                    break;
            }
        }

        requestEvents.clear();

        Log.d(TAG, "Finished removing data from db");
    }

    /**
     * Cancels all GCM Network Manager tasks
     */
    public static void cancelAllTasks(Context context) {
        GcmNetworkManager.getInstance(context).cancelAllTasks(EventUploadService.class);
    }

    /**
     * Cancels default periodic task
     */
    public static void cancel(Context context) {
        GcmNetworkManager.getInstance(context).cancelTask(taskTagDefault, EventUploadService.class);
    }

    /**
     * Cancels GCM Network Manager periodic task
     */
    public static void cancelByTag(Context context, @NonNull String tag) {
        GcmNetworkManager.getInstance(context).cancelTask(tag, EventUploadService.class);
    }

    /**
     * Schedules an periodic upload task
     */
    public static void schedulePeriodicTask(Context context, long paramPeriodSecs, long paramFlexSecs, String tag) {

        Log.d(TAG, "Scheduling periodic task...");

        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(EventUploadService.class)
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
                .setService(EventUploadService.class)
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

    @Override
    public void onDestroy() {

        if (subUserLogin != null) {
            subUserLogin.unsubscribe();
        }

        super.onDestroy();
    }
}
