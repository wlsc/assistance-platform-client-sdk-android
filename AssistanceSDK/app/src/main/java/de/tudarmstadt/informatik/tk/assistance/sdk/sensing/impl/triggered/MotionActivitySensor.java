package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.service.ActivitySensorService;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public final class MotionActivitySensor extends
        AbstractTriggeredSensor implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MotionActivitySensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private int UPDATE_INTERVAL_IN_SEC = 5;
    // -----------------------------------------------------

    private static GoogleApiClient mGoogleApiClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9_000;

    private static MotionActivitySensor INSTANCE;

    /**
     * High possibility motion action
     */
    private DetectedActivity mostProbableActivity;

    /**
     * All motion actions and their possibility
     */
    private List<DetectedActivity> probableActivities;

    private MotionActivitySensor(Context context) {
        super(context);

        mGoogleApiClient = getGoogleApiClient();
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static MotionActivitySensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new MotionActivitySensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        DbMotionActivitySensor motionActivityEvent = new DbMotionActivitySensor();

        for (DetectedActivity activity : probableActivities) {

            if (activity == null) {
                continue;
            }

            int confidence = activity.getConfidence();
            int type = activity.getType();

            switch (type) {

                case 0:
                    motionActivityEvent.setDriving(confidence);
                    break;
                case 1:
                    motionActivityEvent.setCycling(confidence);
                    break;
                case 2:
                    motionActivityEvent.setOnFoot(confidence);
                    break;
                case 3:
                    motionActivityEvent.setStationary(confidence);
                    break;
                case 4:
                    motionActivityEvent.setUnknown(confidence);
                    break;
                case 5:
                    motionActivityEvent.setTilting(confidence);
                    break;
                case 6:
                    // no such activity type in Google API
                    break;
                case 7:
                    motionActivityEvent.setWalking(confidence);
                    break;
                case 8:
                    motionActivityEvent.setRunning(confidence);
                    break;
            }
        }

        motionActivityEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
        motionActivityEvent.setDeviceId(deviceId);

        Log.d(TAG, "Insert entry");

        daoProvider.getMotionActivitySensorDao().insert(motionActivityEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public void startSensor() {

        // If a request is not already underway
        if (!isRunning()) {

            // Request a connection to motion activity API
            if (mGoogleApiClient != null &&
                    !mGoogleApiClient.isConnected() &&
                    !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void stopSensor() {

        if (mGoogleApiClient != null) {
            try {
                if (mGoogleApiClient.isConnected()) {

                    ActivityRecognition.ActivityRecognitionApi
                            .removeActivityUpdates(
                                    mGoogleApiClient,
                                    mActivityRecognitionPendingIntent);

                    mGoogleApiClient.disconnect();
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, "Cannot disconnect from Google Api!", e);
            } finally {
                setRunning(false);
                mGoogleApiClient = null;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG, "Connection failed.");

        try {

        /*
         * If the error has a resolution, start a Google Play services activity
		 * to resolve it.
		 */
            if (connectionResult.hasResolution()) {

                connectionResult.startResolutionForResult((Activity) context,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                // If no resolution is available, display an error dialog
            } else {
                Log.d(TAG, "Cannot find any resolution");
                // // Get the error code
                // int errorCode = connectionResult.getErrorCode();
                // // Get the error dialog from Google Play services
                // Dialog errorDialog =
                // GooglePlayServicesUtil.getErrorDialog(errorCode, (Activity)
                // context,
                // CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }

        } catch (SendIntentException e) {
            Log.e(TAG, "Cannot start resolution", e);
        } finally {
            setRunning(false);
        }
    }

    @Override
    public void onConnected(Bundle arg0) {

        Log.d(TAG, "Connection to Google API has been established.");

        Intent intent = new Intent(context, ActivitySensorService.class);
        mActivityRecognitionPendingIntent = PendingIntent.getService(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = getGoogleApiClient();
        }

        if (mGoogleApiClient.isConnected()) {

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                    mGoogleApiClient,
                    UPDATE_INTERVAL_IN_SEC * 1_000,
                    mActivityRecognitionPendingIntent);

            // Indicate that a request is in progress
            setRunning(true);
        }
    }

    @NonNull
    public GoogleApiClient getGoogleApiClient() {

        return new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public int getUpdateIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    public void setUpdateIntervalInSec(int updateIntervalInSec) {
        UPDATE_INTERVAL_IN_SEC = updateIntervalInSec;
    }

    /**
     * Update intervals
     */
    @Override
    public void updateSensorInterval(Double collectionInterval) {

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = collectionInterval.intValue();

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = getGoogleApiClient();
        }

        if (mGoogleApiClient.isConnected()) {

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                    mGoogleApiClient,
                    UPDATE_INTERVAL_IN_SEC * 1_000,
                    mActivityRecognitionPendingIntent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        // panic! stopping everything

        setRunning(false);
        // Delete the client
        mGoogleApiClient = null;
    }

    @Override
    public int getType() {
        return SensorApiType.MOTION_ACTIVITY;
    }

    @Override
    public void reset() {

    }

    /**
     * Handles new motion activity data
     *
     * @param userActivityResult
     */
    public void handleData(ActivityRecognitionResult userActivityResult) {

        probableActivities = userActivityResult.getProbableActivities();
        mostProbableActivity = userActivityResult.getMostProbableActivity();

        dumpData();
    }
}
