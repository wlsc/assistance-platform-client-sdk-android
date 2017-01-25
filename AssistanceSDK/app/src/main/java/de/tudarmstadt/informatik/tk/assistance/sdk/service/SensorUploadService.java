package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.SparseArray;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.OneoffTask.Builder;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUpload;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.UserDeviceDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.ApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.LoginApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.SensorUploadApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor.SensorUploadLogsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.SensorUploadHolder;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.GcmUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.HardwareUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.JsonUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.StringUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class SensorUploadService extends GcmTaskService {

    static final String TAG = SensorUploadService.class.getSimpleName();

    private static final int EVENTS_NUMBER_TO_SPLIT_AFTER_DEFAULT = 500;
    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT = 80;
    static int EVENTS_NUMBER_TO_SPLIT_AFTER = 500;
    static int PUSH_NUMBER_OF_EACH_ELEMENTS = 80;

    // task identifier
    private static final long taskID = 998;
    // the task should be executed every N seconds
    private static final long periodSecsDefault = 30L;
    static long periodSecs = 30L;
    // the task can run as early as N seconds from the scheduled time
    private static final long flexSecsDefault = 15L;
    private static long flexSecs = 15L;

    /**
     * In case of errors -> fallback strategy
     */
    // it's +-10% of tolerance
    private static final int FALLBACK_TOLERANCE = 10;
    // this value is multiplied with fallback's periodSecs and flexPeriod values
    private static final int fallbackMultiplier = 2;
    // fallback for period in case of server is not available
    private static long periodFallbackSecs = periodSecs;
    // fallback for flexibility in case of server not available
    private static long flexFallbackSecs = flexSecs;
    //--------------------------------------------------------

    private static final String UPLOAD_ALL_FLAG_NAME = "UPLOAD_ALL";
    private static final int UPLOAD_ALL_FLAG_VALUE = 1;

    static SensorProvider sensorProvider;
    static PreferenceProvider mPreferenceProvider;

    private Subscription sensorUploadSubscription;
    private Subscription userLoginSubscription;

    @NonNull
    SensorUploadHolder sensorData;

    static boolean shouldUseConnectionFallback;

    static long startRequestTime;
    static long elapsedRequestTime;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Initializing...");

        if (sensorProvider == null) {
            sensorProvider = SensorProvider.getInstance(this);
        }

        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(this);
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (StringUtils.isNotNullAndEmpty(userToken)) {

            Double minUploadInterval = sensorProvider.getMinSensorUploadInterval();

            if (minUploadInterval != null) {

                long minUpload = minUploadInterval.longValue();

                Log.d(TAG, "Setting up new upload interval: " + minUpload);

                long trueFallbackTolerance = (long) Math.ceil(
                        FALLBACK_TOLERANCE * minUpload / 100);

                Log.d(TAG, "Calculated fallback tolerance: " + trueFallbackTolerance);

                // set normal values
                periodSecs = minUpload + trueFallbackTolerance;
                flexSecs = minUpload;
                // set fallback values
                periodFallbackSecs = periodSecs;
                flexFallbackSecs = flexSecs;

                Log.d(TAG, "New upload rates: period is " + periodSecs + " and flex is " + flexSecs);
            }

            GcmUtils.cancelAllTasks(this, SensorUploadService.class);

            if (shouldUseConnectionFallback) {
                schedulePeriodicTask(this,
                        taskID,
                        periodFallbackSecs,
                        flexFallbackSecs);
            } else {
                schedulePeriodicTask(this, taskID, periodSecs, flexSecs);
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
        if (ConnectionUtils.isAirplaneModeEnabled(this)) {
            Log.d(TAG, "Airplane Mode enabled. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        // device is not online
        if (!ConnectionUtils.isOnline(this)) {
            Log.d(TAG, "Device is not online. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        if (sensorProvider == null) {
            sensorProvider = SensorProvider.getInstance(this);
        }

        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(this);
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken.isEmpty()) {
            Log.d(TAG, "User is not logged in. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        startRequestTime = System.nanoTime();

        boolean isPeriodic = true;

        if (taskParams != null) {

            Bundle extras = taskParams.getExtras();

            if (extras != null) {

                int value = extras.getInt(UPLOAD_ALL_FLAG_NAME, 0);

                // upload all data at once
                if (value == UPLOAD_ALL_FLAG_VALUE) {

                    isPeriodic = false;

                    Handler handler = new Handler(getMainLooper());
                    handler.post(() -> prepareSensorData(0));
                }
            }
        }

        // do periodic upload task
        if (isPeriodic) {

            Handler handler = new Handler(getMainLooper());
            handler.post(() -> prepareSensorData(PUSH_NUMBER_OF_EACH_ELEMENTS));
        }

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    /**
     * Prepares the sensor data to be uploaded and calls the upload
     *
     * @param amountOfData
     */
    private void prepareSensorData(int amountOfData) {

        final long serverDeviceId = mPreferenceProvider.getServerDeviceId();

        Log.d(TAG, "Sync server device id: " + serverDeviceId);

        // user logged out
        if (serverDeviceId == -1) {
            Log.d(TAG, "User logged out -- all tasks has been canceled!");
            GcmUtils.cancelAllTasks(this, SensorUploadService.class);
            return;
        }

        sensorData = sensorProvider.getEntriesForUpload(amountOfData);

        final List<SensorDto> eventsAsList = new ArrayList<>();
        final SparseArray<List<? extends SensorDto>> requestEvents = sensorData.getRequestEvents();

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

                SensorUploadRequestDto eventUploadRequest = new SensorUploadRequestDto(
                        serverDeviceId,
                        eventPart
                );

                doUploadEventData(eventUploadRequest);
            });
        }
    }

    /**
     * Pushes events data to server
     *
     * @param eventUploadRequest
     */
    private void doUploadEventData(@Nullable final SensorUploadRequestDto eventUploadRequest) {

        Log.d(TAG, "Uploading data...");

        if (eventUploadRequest == null || eventUploadRequest.getDataEvents() == null) {
            Log.d(TAG, "eventUploadRequest is null or events list is empty!");
            return;
        }

        if (eventUploadRequest.getDataEvents().isEmpty()) {
            Log.d(TAG, "No new data found");
            return;
        }

        SensorUploadApiProvider sensorApi = ApiProvider.getInstance(this).getSensorUploadApiProvider();

        /**
         * Send to upload data service
         */
        String userToken = mPreferenceProvider.getUserToken();

        sensorUploadSubscription = sensorApi.uploadData(userToken, eventUploadRequest)
                .subscribe(new SensorUploadSubscriber(eventUploadRequest));
    }

    /**
     * Does user token refresh
     */
    void relogin() {

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

        ApiProvider apiProvider = ApiProvider.getInstance(this);
        LoginApiProvider userApiProvider = apiProvider.getLoginApiProvider();

        LoginRequestDto loginRequest = new LoginRequestDto(email, password, userDevice);

        userLoginSubscription = userApiProvider
                .loginUser(loginRequest)
                .subscribe(new UserLoginSubscriber());
    }

    /**
     * Class to upload sensor data with RxJava
     */
    private class SensorUploadSubscriber extends Subscriber<SensorUploadResponseDto> {

        private final DbUser user;
        private final SensorUploadRequestDto eventUploadRequest;
        private final SensorUploadLogsDao sensorUploadLogsDao;
        private long requestStartTimeMillis;
        private LogsSensorUpload logsSensorUpload;

        public SensorUploadSubscriber(SensorUploadRequestDto eventUploadRequest) {

            this.eventUploadRequest = eventUploadRequest;
            this.logsSensorUpload = new LogsSensorUpload();
            String userToken = mPreferenceProvider.getUserToken();
            this.user = DaoProvider.getInstance(getApplicationContext())
                    .getUserDao()
                    .getByToken(userToken);
            this.sensorUploadLogsDao = DaoProvider.getInstance(getApplicationContext()).getSensorUploadLogsDao();
            this.requestStartTimeMillis = System.currentTimeMillis();
        }

        @Override
        public void onStart() {
            super.onStart();

            logsSensorUpload.setStartTime(requestStartTimeMillis);
            logsSensorUpload.setBodySize(Long.valueOf(JsonUtils.getGson().toJson(eventUploadRequest).length()));

            boolean isWifi = ConnectionUtils.isConnectedWifi(getApplicationContext());
            boolean isMobile = ConnectionUtils.isConnectedMobile(getApplicationContext());

            if (isMobile) {

                int type = ConnectionUtils.getConnectionType(getApplicationContext());
                String mobileType = "";

                switch (type) {
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        mobileType = "4g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        mobileType = "4g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        mobileType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        mobileType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        mobileType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        mobileType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        mobileType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        mobileType = "3g";
                        break;
                }

                logsSensorUpload.setNetworkType(mobileType);
            }

            if (isWifi) {
                logsSensorUpload.setNetworkType("wlan");
            }

            logsSensorUpload.setDbUser(user);
            logsSensorUpload.setEventsNumber(eventUploadRequest.getDataEvents().size());
        }

        @Override
        public void onCompleted() {

            // check if time is not exeeding periodSecs (else -> we cannot promise data in time)
            elapsedRequestTime = System.nanoTime() - startRequestTime;
            long elapsedRequestTimeInSec = TimeUnit.SECONDS.convert(
                    elapsedRequestTime, TimeUnit.NANOSECONDS);

            Log.d(TAG, "Calculated elapsed time in seconds: " + elapsedRequestTimeInSec);

            long trueFallbackTolerance = (long) Math.ceil(
                    FALLBACK_TOLERANCE * EVENTS_NUMBER_TO_SPLIT_AFTER / 100);

            Log.d(TAG, "Calculated fallback tolerance: " + trueFallbackTolerance);

            if (periodSecs < elapsedRequestTimeInSec) {

                Log.d(TAG, "Roundtrip is greater than upload period");

                // change packet size -> apply fallback tolerance percentage
                EVENTS_NUMBER_TO_SPLIT_AFTER -= trueFallbackTolerance;
                PUSH_NUMBER_OF_EACH_ELEMENTS -= trueFallbackTolerance;

                Log.d(TAG, "New upload period: events " +
                        EVENTS_NUMBER_TO_SPLIT_AFTER + " and push " +
                        PUSH_NUMBER_OF_EACH_ELEMENTS);

            } else {

                Log.d(TAG, "Roundtrip time is NORMAL");

                // gracefully return to prev values
                EVENTS_NUMBER_TO_SPLIT_AFTER += trueFallbackTolerance;
                PUSH_NUMBER_OF_EACH_ELEMENTS += trueFallbackTolerance;

                // in case it will be greater than standard default values
                if (EVENTS_NUMBER_TO_SPLIT_AFTER > EVENTS_NUMBER_TO_SPLIT_AFTER_DEFAULT) {
                    EVENTS_NUMBER_TO_SPLIT_AFTER = EVENTS_NUMBER_TO_SPLIT_AFTER_DEFAULT;
                }

                // in case it will be greater than standard default values
                if (PUSH_NUMBER_OF_EACH_ELEMENTS > PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT) {
                    PUSH_NUMBER_OF_EACH_ELEMENTS = PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT;
                }
            }

            startRequestTime = 0;
        }

        @Override
        public void onError(Throwable e) {

            if (e instanceof HttpException) {

                HttpException error = (HttpException) e;

                Log.d(TAG, "Server returned error! Code: " + error.code());

                // user need relogin
                if (error.code() == 401) {
                    relogin();
                    // dont need to continue here
                    return;
                }
            }

            rescheduleFallbackPeriodicTask();
        }

        @Override
        public void onNext(SensorUploadResponseDto responseDto) {

            Log.d(TAG, "OK response from server received");

            // successful transmission of event data -> remove that data from db
            sensorProvider.handleSentEvents(sensorData.getDbEvents());

            // reschedule default periodic task
            if (shouldUseConnectionFallback) {
                shouldUseConnectionFallback = false;

                rescheduleNormalPeriodicTask();
            }

            /**
             *  insert logs data into db
             */
            if (responseDto != null) {
                logsSensorUpload.setProcessingTime(responseDto.getProcessingTime());
            }

            logsSensorUpload.setResponseTime(System.currentTimeMillis() - requestStartTimeMillis);

            Log.d(TAG, "Sensor upload logs inserting...");
            sensorUploadLogsDao.insert(logsSensorUpload);
            Log.d(TAG, "Done");
        }
    }

    /**
     * Subscriber for user to auto login with RxJava
     */
    private class UserLoginSubscriber extends Subscriber<LoginResponseDto> {

        UserLoginSubscriber() {
        }

        @Override
        public void onCompleted() {
            // do nothing
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof HttpException) {
                // ignore by now
                Log.d(TAG, "login function failed. Code: " +
                        ((HttpException) e).code());
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
     * Rescheduling normal periodic task by canceling fallback task
     */
    void rescheduleNormalPeriodicTask() {

        Log.d(TAG, "Rescheduling normal periodic task...");

        // cancelling all tasks
        GcmUtils.cancelAllTasks(this, SensorUploadService.class);

        // return to default values
        periodFallbackSecs = periodSecs;
        flexFallbackSecs = periodFallbackSecs;

        // reschedule normal periodic task
        schedulePeriodicTask(this,
                taskID,
                periodSecs,
                flexSecs);
    }

    /**
     * Rescheduling fallback task by canceling normal task
     */
    void rescheduleFallbackPeriodicTask() {

        Log.d(TAG, "Rescheduling fallback periodic task...");

        // cancelling all tasks
        GcmUtils.cancelAllTasks(this, SensorUploadService.class);

        // increase fallback with +-10%
        long trueFallbackTolerance = (long) Math.ceil(FALLBACK_TOLERANCE * periodFallbackSecs / 100);
        periodFallbackSecs = (periodFallbackSecs * fallbackMultiplier) + trueFallbackTolerance;
        flexFallbackSecs = flexFallbackSecs * fallbackMultiplier;

        // reschedule periodic task with fallback timings
        schedulePeriodicTask(this,
                taskID,
                periodFallbackSecs,
                flexFallbackSecs);
    }

    @Override
    public void onInitializeTasks() {

        Log.d(TAG, "Reinitialize periodic task...");

        // first cancel any running tasks
        GcmUtils.cancelAllTasks(this, SensorUploadService.class);

        if (shouldUseConnectionFallback) {
            schedulePeriodicTask(
                    this,
                    taskID,
                    periodFallbackSecs,
                    flexFallbackSecs);
        } else {
            schedulePeriodicTask(
                    this,
                    taskID,
                    periodSecs,
                    flexSecs);
        }

        super.onInitializeTasks();
    }

    /**
     * Schedules an periodic upload task
     */
    public static void schedulePeriodicTask(Context context, long taskId, long paramPeriodSecs, long paramFlexSecs) {
        GcmUtils.startPeriodicTask(context, SensorUploadService.class, taskId, paramPeriodSecs, paramFlexSecs);
    }

    /**
     * Schedules an periodic upload task
     */
    public static void scheduleOneTimeTask(Context context, long startSecs, long endSecs, String tag) {

        Log.d(TAG, "Scheduling one time task...");

        Bundle bundle = new Bundle();
        bundle.putInt(UPLOAD_ALL_FLAG_NAME, UPLOAD_ALL_FLAG_VALUE);

        OneoffTask oneTimeTask = new Builder()
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
        Log.d(TAG, "onDestroy: Destroying upload task");
        // XXX: dont unsubscribe in onDestroy. its because onDestroy is faster than sending data
//        RxUtils.unsubscribe(userLoginSubscription);
//        RxUtils.unsubscribe(sensorUploadSubscription);
        super.onDestroy();
    }
}