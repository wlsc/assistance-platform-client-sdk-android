package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensor.impl.triggered;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.service.ActivitySensorService;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class MotionActivityEvent extends
        AbstractTriggeredEvent implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MotionActivityEvent.class.getSimpleName();

    // ------------------- Configuration -------------------
    private static final int DETECTION_INTERVAL_IN_SEC = 120;
    // -----------------------------------------------------

    private GoogleApiClient mGoogleApiClient;

    private PendingIntent mActivityRecognitionPendingIntent;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static MotionActivityEvent INSTANCE;

    /**
     * High possibility motion action
     */
    private DetectedActivity mostProbableActivity;

    /**
     * All motion actions and their possibility
     */
    private List<DetectedActivity> probableActivities;

    private MotionActivityEvent(Context context) {
        super(context);

        mGoogleApiClient = getGoogleApiClient();
    }

    @Override
    public void dumpData() {

        DbMotionActivityEvent motionActivityEvent = new DbMotionActivityEvent();

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

        // insert db entry
        daoProvider.getMotionActivityEventDao().insert(motionActivityEvent);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static MotionActivityEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new MotionActivityEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public void startSensor() {

        // If a request is not already underway
        if (!isRunning()) {

            // Request a connection to Location Services
            if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "Connection failed.");

        try {

        /*
         * If the error has a resolution, start a Google Play services activity
		 * to resolve it.
		 */
            if (connectionResult.hasResolution()) {

                try {
                    connectionResult.startResolutionForResult((Activity) context,
                            CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (SendIntentException e) {
                    Log.e(TAG, "Cannot start resolution", e);
                }
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
                    DETECTION_INTERVAL_IN_SEC * 1000,
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

    @Override
    public void onConnectionSuspended(int i) {

        // panic! stopping everything

        setRunning(false);
        // Delete the client
        mGoogleApiClient = null;
    }

    @Override
    public int getType() {
        return DtoType.MOTION_ACTIVITY;
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
