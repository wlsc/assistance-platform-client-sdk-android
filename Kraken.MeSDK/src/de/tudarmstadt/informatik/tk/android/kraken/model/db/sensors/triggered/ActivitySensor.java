package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered;

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

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.android.kraken.service.ActivitySensorService;


public class ActivitySensor extends AbstractTriggeredSensor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ActivitySensor.class.getSimpleName();

    // ------------------- Configuration -------------------
    private int DETECTION_INTERVAL_IN_SEC = 120;
    // -----------------------------------------------------

    private GoogleApiClient mGoogleApiClient = null;

    private PendingIntent m_activityRecognitionPendingIntent;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static ActivitySensor INSTANCE;

    public ActivitySensor(Context context) {
        super(context);
        INSTANCE = this;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void dumpData() {

    }

    public static ActivitySensor getInstance() {
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
        //mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Turn off the request flag
        isRunning = false;
        // Delete the client
        mGoogleApiClient = null;
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.MOTION_ACTIVITY_EVENT;
    }

    @Override
    public void reset() {

    }

    public void handleData(ActivityRecognitionResult userActivityResult) {

        List<DetectedActivity> probableActivities = userActivityResult.getProbableActivities();
        DetectedActivity mostProbableActivity = userActivityResult.getMostProbableActivity();

        Log.d(TAG, "Detected activity: " + mostProbableActivity.toString());

        for (DetectedActivity activity : probableActivities) {
            Log.d(TAG, "Probable: " + activity.toString());
        }


    }
}
