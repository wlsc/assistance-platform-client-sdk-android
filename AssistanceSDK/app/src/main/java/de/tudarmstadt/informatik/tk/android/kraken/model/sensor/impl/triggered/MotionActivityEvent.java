package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.service.ActivitySensorService;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;


public class MotionActivityEvent extends AbstractTriggeredEvent implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MotionActivityEvent.class.getSimpleName();

    // ------------------- Configuration -------------------
    private int DETECTION_INTERVAL_IN_SEC = 120;
    // -----------------------------------------------------

    private GoogleApiClient mGoogleApiClient = null;

    private PendingIntent m_activityRecognitionPendingIntent;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static MotionActivityEvent INSTANCE;

    private DbMotionActivityEventDao dbMotionActivityEventDao;

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

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (dbMotionActivityEventDao == null) {
            dbMotionActivityEventDao = DbProvider.getInstance(context).getDaoSession().getDbMotionActivityEventDao();
        }
    }

    @Override
    protected void dumpData() {

        Log.d(TAG, "Dumping data to db...");

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
        dbMotionActivityEventDao.insertOrReplace(motionActivityEvent);

        Log.d(TAG, "Finished dumping data.");
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

    public void startSensor() {

        // If a request is not already underway
        if (!isRunning) {
            // Indicate that a request is in progress
            isRunning = true;
            // Request a connection to Location Services
            mGoogleApiClient.connect();
            //
        }
    }

    public void stopSensor() {
        if (mGoogleApiClient != null) {
            try {
                if (mGoogleApiClient.isConnected()) {
                    ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, m_activityRecognitionPendingIntent);
                    mGoogleApiClient.disconnect();
                }
            } catch (IllegalStateException e) {

                Log.e(TAG, "Cannot disconnect from Google Api!", e);

                // stopping sensor
                isRunning = false;
            }
        }

        isRunning = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "Connection failed.");

        // Turn off the request flag
        isRunning = false;

        /*
         * If the error has a resolution, start a Google Play services activity
		 * to resolve it.
		 */
        if (connectionResult.hasResolution()) {

            try {
                connectionResult.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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

    }

    @Override
    public void onConnected(Bundle arg0) {

        Log.d(TAG, "Connection established.");

        Intent intent = new Intent(context, ActivitySensorService.class);
        m_activityRecognitionPendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, DETECTION_INTERVAL_IN_SEC * 1000, m_activityRecognitionPendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Turn off the request flag
        isRunning = false;
        // Delete the client
        mGoogleApiClient = null;
    }

    @Override
    public int getType() {
        return SensorType.MOTION_ACTIVITY;
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
