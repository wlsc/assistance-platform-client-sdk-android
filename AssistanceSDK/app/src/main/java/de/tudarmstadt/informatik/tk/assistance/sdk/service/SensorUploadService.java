package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.UserDeviceDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.SensorUploadHolder;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.ApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.LoginApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.SensorUploadApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.GcmUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.HardwareUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.StringUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class SensorUploadService extends GcmTaskService {

    private static final String TAG = SensorUploadService.class.getSimpleName();

    private static final int EVENTS_NUMBER_TO_SPLIT_AFTER_DEFAULT = 500;
    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT = 80;
    private static int EVENTS_NUMBER_TO_SPLIT_AFTER = 500;
    private static int PUSH_NUMBER_OF_EACH_ELEMENTS = 80;

    // task identifier
    private static final long taskID = 998;
    // the task should be executed every N seconds
    private static final long periodSecsDefault = 60l;
    private static long periodSecs = 60l;
    // the task can run as early as N seconds from the scheduled time
    private static final long flexSecsDefault = 15l;
    private static long flexSecs = 15l;

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


    private static final String UPLOAD_ALL_FLAG_NAME = "UPLOAD_ALL";
    private static final int UPLOAD_ALL_FLAG_VALUE = 1;

    private static SensorProvider sensorProvider;
    private static PreferenceProvider mPreferenceProvider;

    private Subscription sensorUploadSubscription;
    private Subscription userLoginSubscription;

    @NonNull
    private SensorUploadHolder sensorData;

    private static boolean shouldUseConnectionFallback;

    private static long startRequestTime;
    private static long elapsedRequestTime;

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
                periodSecs = minUpload;
                flexSecs = minUpload + trueFallbackTolerance;
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

        for (int i = 0, eventsSize = sensorData.getRequestEvents().size(); i < eventsSize; i++) {
            eventsAsList.addAll(sensorData.getRequestEvents().valueAt(i));
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

        sensorUploadSubscription = sensorApi.uploadData(userToken, eventUploadRequest)
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
    private class SensorUploadSubscriber extends Subscriber<Void> {

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

                if (PUSH_NUMBER_OF_EACH_ELEMENTS > PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT) {
                    PUSH_NUMBER_OF_EACH_ELEMENTS = PUSH_NUMBER_OF_EACH_ELEMENTS_DEFAULT;
                }
            }

            startRequestTime = 0;
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
                        // dont need to continue here
                        return;
                    }
                }
            }

            // fallbacking periodic request
            if (!shouldUseConnectionFallback) {
                shouldUseConnectionFallback = true;

                rescheduleFallbackPeriodicTask();
            }
        }

        @Override
        public void onNext(Void aVoid) {

            Log.d(TAG, "OK response from server received");

            // successful transmission of event data -> remove that data from db
            sensorProvider.handleSentEvents(sensorData.getDbEvents());

            // reschedule default periodic task
            if (shouldUseConnectionFallback) {
                shouldUseConnectionFallback = false;

                rescheduleNormalPeriodicTask();
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
     * Rescheduling normal periodic task by canceling fallback task
     */
    private void rescheduleNormalPeriodicTask() {

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
    private void rescheduleFallbackPeriodicTask() {

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
        Log.d(TAG, "onDestroy: Destroying upload task");
        // XXX: dont unsubscribe in onDestroy. its because onDestroy is faster than sending data
//        RxUtils.unsubscribe(userLoginSubscription);
//        RxUtils.unsubscribe(sensorUploadSubscription);
        super.onDestroy();
    }
}