package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.endpoint.EndpointGenerator;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.endpoint.EventUploadEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.provider.PreferenceProvider;
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
    // the task should be executed every 30 seconds
    private static final long periodSecs = 60L;
    // the task can run as early as -15 seconds from the scheduled time
    private static final long flexSecs = 15L;

    // an unique task identifier
    private static final String taskTag = "periodic | " + taskID + ": " + periodSecs + "s, f:" + flexSecs;

    private static final String UPLOAD_ALL_FLAG_NAME = "UPLOAD_ALL";
    private static final int UPLOAD_ALL_FLAG = 1;

    private static PreferenceProvider mPreferenceProvider;

    private SparseArrayCompat<List<Sensor>> events;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Initializing...");

        if (mPreferenceProvider == null) {
            mPreferenceProvider = PreferenceProvider.getInstance(getApplicationContext());
        }

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken != null && !userToken.isEmpty()) {
            schedulePeriodicTask(getApplicationContext(), periodSecs, flexSecs, taskTag);
        } else {
            Log.d(TAG, "User is not logged in. Scheduled task won't start");
        }

        Log.d(TAG, "Finished!");
    }

    @Override
    public int onRunTask(TaskParams taskParams) {

        Log.d(TAG, "Upload task has started");

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
                                GcmNetworkManager.getInstance(getApplicationContext()).cancelAllTasks(EventUploaderService.class);
                                return;
                            }

                            events = new SparseArrayCompat<>();
                            events = DbProvider.getInstance(getApplicationContext()).getEntriesForUpload(0);

                            final List<Sensor> eventsAsList = new LinkedList<>();

                            for (int i = 0; i < events.size(); i++) {
                                int key = events.keyAt(i);
                                eventsAsList.addAll(events.get(key));
                            }

                            // partial upload
                            int eventsSize = eventsAsList.size();

                            Log.d(TAG, "There are " + eventsSize + " events to upload");

                            if (eventsSize < EVENTS_NUMBER_TO_SPLIT_AFTER) {
                                // send as usual

                                Log.d(TAG, "Sending data in normal mode");

                                EventUploadRequest eventUploadRequest = new EventUploadRequest();
                                eventUploadRequest.setDataEvents(eventsAsList);
                                eventUploadRequest.setServerDeviceId(serverDeviceId);

                                doUploadEventData(eventUploadRequest);

                            } else {
                                // send partial with many requests
                                int howMuchToSend = eventsSize / EVENTS_NUMBER_TO_SPLIT_AFTER;

                                Log.d(TAG, "Sending partial data with " + howMuchToSend + " requests");

                                for (int i = 0; i <= howMuchToSend; i++) {

                                    final int finalCounter = i;

                                    AsyncTask.execute(new Runnable() {

                                        @Override
                                        public void run() {

                                            List<Sensor> tmpSensors = eventsAsList
                                                    .subList(finalCounter * EVENTS_NUMBER_TO_SPLIT_AFTER,
                                                            (finalCounter + 1) * EVENTS_NUMBER_TO_SPLIT_AFTER);

                                            EventUploadRequest eventUploadRequest = new EventUploadRequest();
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

                    long serverDeviceId = mPreferenceProvider.getServerDeviceId();

                    Log.d(TAG, "Sync server device id: " + serverDeviceId);

                    // user logged out
                    if (serverDeviceId == -1) {
                        Log.d(TAG, "User logged out -- all tasks has been canceled!");
                        GcmNetworkManager.getInstance(getApplicationContext()).cancelAllTasks(EventUploaderService.class);
                        return;
                    }

                    events = new SparseArrayCompat<>();
                    events = DbProvider.getInstance(getApplicationContext()).getEntriesForUpload(PUSH_NUMBER_OF_EACH_ELEMENTS);

                    List<Sensor> eventsAsList = new LinkedList<>();

                    for (int i = 0; i < events.size(); i++) {
                        int key = events.keyAt(i);
                        eventsAsList.addAll(events.get(key));
                    }

                    // partial upload
                    int eventsSize = eventsAsList.size();

                    Log.d(TAG, "There are " + eventsSize + " events to upload");

                    if (eventsSize < EVENTS_NUMBER_TO_SPLIT_AFTER) {
                        // send as usual

                        Log.d(TAG, "Sending data in normal mode");

                        EventUploadRequest eventUploadRequest = new EventUploadRequest();
                        eventUploadRequest.setDataEvents(eventsAsList);
                        eventUploadRequest.setServerDeviceId(serverDeviceId);

                        doUploadEventData(eventUploadRequest);

                    } else {
                        // send partial with many requests
                        int howMuchToSend = eventsSize / EVENTS_NUMBER_TO_SPLIT_AFTER;

                        Log.d(TAG, "Sending partial data with " + howMuchToSend + " requests");

                        for (int i = 0; i <= howMuchToSend; i++) {

                            List<Sensor> tmpSensors = eventsAsList
                                    .subList(i * EVENTS_NUMBER_TO_SPLIT_AFTER,
                                            (i + 1) * EVENTS_NUMBER_TO_SPLIT_AFTER);

                            EventUploadRequest eventUploadRequest = new EventUploadRequest();
                            eventUploadRequest.setDataEvents(tmpSensors);
                            eventUploadRequest.setServerDeviceId(serverDeviceId);

                            doUploadEventData(eventUploadRequest);
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
    private void doUploadEventData(final EventUploadRequest eventUploadRequest) {

        Log.d(TAG, "Uploading data...");

        if (eventUploadRequest == null || eventUploadRequest.getDataEvents() == null) {
            return;
        }

        if (eventUploadRequest.getDataEvents().size() == 0) {
            Log.d(TAG, "No new data found");
            return;
        }

        // send to upload data service
        EventUploadEndpoint eventUploadEndpoint = EndpointGenerator.create(EventUploadEndpoint.class);

        String userToken = mPreferenceProvider.getUserToken();

        eventUploadEndpoint.uploadData(userToken, eventUploadRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    Log.d(TAG, "OK response from server received");

                    // successful transmission of event data -> remove that data from db
                    DbProvider.getInstance(getApplicationContext()).removeDbSentEvents(events);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO process error
                Log.d(TAG, "Server returned error!");
            }
        });
    }

    @Override
    public void onInitializeTasks() {

        Log.d(TAG, "Reinitialize periodic task...");

        // first cancel any running event uploader tasks
        cancelAllTasks(getApplicationContext());
        schedulePeriodicTask(getApplicationContext(), periodSecs, flexSecs, taskTag);

        super.onInitializeTasks();
    }

    /**
     * Cancels all GCM Network Manager tasks
     */
    public static void cancelAllTasks(Context context) {
        GcmNetworkManager.getInstance(context).cancelAllTasks(EventUploaderService.class);
    }

    /**
     * Cancels this GCM Network Manager periodic task
     */
    public static void cancel(Context context) {
        GcmNetworkManager.getInstance(context).cancelTask(taskTag, EventUploaderService.class);
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
    public static void schedulePeriodicTask(Context context, long periodSecs, long flexSecs, String tag) {

        Log.d(TAG, "Scheduling periodic task...");

        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(EventUploaderService.class)
                .setPeriod(periodSecs)
                .setFlex(flexSecs)
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
