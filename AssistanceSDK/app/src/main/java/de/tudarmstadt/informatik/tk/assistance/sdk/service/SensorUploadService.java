package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
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
import de.tudarmstadt.informatik.tk.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.UserDeviceDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.contact.ContactEmailNumber;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.contact.ContactSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.ApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.SensorUploadApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social.FacebookSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social.TucanSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.GcmUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.HardwareUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.RxUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;
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
public class SensorUploadService extends GcmTaskService {

    private static final String TAG = SensorUploadService.class.getSimpleName();

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

    private Subscription sensorUploadSubscription;
    private Subscription userLoginSubscription;

    @NonNull
    private SparseArray<List<? extends IDbSensor>> dbEvents = new SparseArray<>();
    @NonNull
    private SparseArray<List<? extends SensorDto>> requestEvents = new SparseArray<>();

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

            GcmUtils.cancelAllTasks(getApplicationContext(), SensorUploadService.class);

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

        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(getApplicationContext());
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
                            GcmUtils.cancelAllTasks(getApplicationContext(), SensorUploadService.class);
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

                        Handler uploadHandler = new Handler(getMainLooper());

                        for (final List<SensorDto> partEvent : eventParts) {

                            uploadHandler.post(() -> {

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
                    GcmUtils.cancelAllTasks(getApplicationContext(), SensorUploadService.class);
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

                Handler uploadHandler = new Handler(getMainLooper());

                for (final List<SensorDto> eventPart : eventParts) {

                    uploadHandler.post(() -> {

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

        SensorUploadApiProvider sensorApi = ApiProvider.getInstance(this).getSensorUploadApiProvider();

        /**
         * Send to upload data service
         */
        String userToken = mPreferenceProvider.getUserToken();

        sensorUploadSubscription = sensorApi
                .uploadData(userToken, eventUploadRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SensorUploadSubscriber());
    }

    /**
     * Does user token refresh
     */
    private void relogin() {

        String email = mPreferenceProvider.getUserEmail();
        String password = mPreferenceProvider.getUserPassword();
        long serverDeviceId = mPreferenceProvider.getServerDeviceId();

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

        userLoginSubscription = userEndpoint.loginUser(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserLoginSubscriber());
    }

    /**
     * Class to upload sensor data with RxJava
     */
    private class SensorUploadSubscriber extends Subscriber<Void> {

        @Override
        public void onCompleted() {
            // empty
        }

        @Override
        public void onError(Throwable e) {

            if (e instanceof RetrofitError) {

                RetrofitError error = (RetrofitError) e;

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
        }

        @Override
        public void onNext(Void aVoid) {

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

    /**
     * Subscriber for user to auto login with RxJava
     */
    private class UserLoginSubscriber extends Subscriber<LoginResponseDto> {

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

                mPreferenceProvider.setUserToken(response.getUserToken());

            } else {
                Log.d(TAG, "apiResponse is NULL");
            }
        }
    }

    /**
     * Rescheduling default task by canceling fallback one
     */
    private void rescheduleDefaultPeriodicTask() {

        Log.d(TAG, "Rescheduling default periodic task...");

        // cancelling fallback periodic task
        GcmUtils.cancelByTag(getApplicationContext(), taskTagFallback, SensorUploadService.class);

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
        GcmUtils.cancelByTag(getApplicationContext(), taskTagDefault, SensorUploadService.class);

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
        GcmUtils.cancelAllTasks(getApplicationContext(), SensorUploadService.class);

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
        String userToken = PreferenceProvider.getInstance(getApplicationContext()).getUserToken();
        DbUser user = daoProvider.getUserDao().getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "User is NULL. Aborting...");
            return;
        }

        long deviceId = PreferenceProvider.getInstance(getApplicationContext()).getCurrentDeviceId();

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

        requestEvents.clear();

        Log.d(TAG, "Finished removing data from db");
    }

    /**
     * Schedules an periodic upload task
     */
    public static void schedulePeriodicTask(Context context, long paramPeriodSecs, long paramFlexSecs, String tag) {
        GcmUtils.startPeriodicTask(context, SensorUploadService.class, paramPeriodSecs, paramFlexSecs, tag);
    }

    /**
     * Schedules an periodic upload task
     */
    public static void scheduleOneTimeTask(Context context, long startSecs, long endSecs, String tag) {

        Log.d(TAG, "Scheduling one time task...");

        Bundle bundle = new Bundle();
        bundle.putInt(UPLOAD_ALL_FLAG_NAME, UPLOAD_ALL_FLAG);

        OneoffTask oneTimeTask = new OneoffTask.Builder()
                .setService(SensorUploadService.class)
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
        RxUtils.unsubscribe(userLoginSubscription);
        RxUtils.unsubscribe(sensorUploadSubscription);
        super.onDestroy();
    }
}