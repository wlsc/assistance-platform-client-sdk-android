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

    private static SensorProvider sensorProvider;
    private static PreferenceProvider mPreferenceProvider;

    private Subscription sensorUploadSubscription;
    private Subscription userLoginSubscription;

    @NonNull
    private SensorUploadHolder sensorData;

    private static boolean isNeedInConnectionFallback;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Initializing...");

        if (sensorProvider == null) {
            sensorProvider = SensorProvider.getInstance(getApplicationContext());
        }

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

        if (sensorProvider == null) {
            sensorProvider = SensorProvider.getInstance(getApplicationContext());
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
            GcmUtils.cancelAllTasks(getApplicationContext(), SensorUploadService.class);
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
            sensorProvider.handleSentEvents(sensorData.getDbEvents());

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
        Log.d(TAG, "onDestroy: Destroying upload task");
        // XXX: dont unsubscribe in onDestroy. its because onDestroy is faster than sending data
//        RxUtils.unsubscribe(userLoginSubscription);
//        RxUtils.unsubscribe(sensorUploadSubscription);
        super.onDestroy();
    }
}