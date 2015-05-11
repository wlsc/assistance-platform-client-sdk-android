package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.triggered;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorActivity;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractTriggeredSensor;

public class ActivitySensor extends AbstractTriggeredSensor implements ConnectionCallbacks, OnConnectionFailedListener {

	public enum Configuration
	{
		DETECTION_INTERVAL_IN_SEC;
	}
	
	// ------------------- Configuration -------------------
    private int DETECTION_INTERVAL_IN_SEC = 120;
	// -----------------------------------------------------

	private ActivityRecognitionClient m_activityRecognitionClient = null;

	private PendingIntent m_activityRecognitionPendingIntent;

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private static ActivitySensor m_instance;

	public ActivitySensor(Context context) {
		super(context);

		m_activityRecognitionClient = new ActivityRecognitionClient(m_context, this, this);
		m_instance = this;
	}

	public static ActivitySensor getInstance() {
		return m_instance;
	}

	public void startSensor() {

		// If a request is not already underway
		if (!m_bIsRunning) {
			// Indicate that a request is in progress
			m_bIsRunning = true;
			// Request a connection to Location Services
			m_activityRecognitionClient.connect();
			//
		}
	}

	public void stopSensor() {
		if (m_activityRecognitionClient != null) {
            try {
                m_activityRecognitionClient.removeActivityUpdates(m_activityRecognitionPendingIntent);
                m_activityRecognitionClient.disconnect();
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
		}
		m_bIsRunning = false;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Turn off the request flag
		m_bIsRunning = false;
		/*
		 * If the error has a resolution, start a Google Play services activity
		 * to resolve it.
		 */
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult((Activity) m_context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
			// If no resolution is available, display an error dialog
		} else {
			// // Get the error code
			// int errorCode = connectionResult.getErrorCode();
			// // Get the error dialog from Google Play services
			// Dialog errorDialog =
			// GooglePlayServicesUtil.getErrorDialog(errorCode, (Activity)
			// m_context,
			// CONNECTION_FAILURE_RESOLUTION_REQUEST);
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		Intent intent = new Intent(m_context, ActivitySensorService.class);
		m_activityRecognitionPendingIntent = PendingIntent.getService(m_context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		m_activityRecognitionClient.requestActivityUpdates(DETECTION_INTERVAL_IN_SEC * 1000, m_activityRecognitionPendingIntent);
        //m_activityRecognitionClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// Turn off the request flag
		m_bIsRunning = false;
		// Delete the client
		m_activityRecognitionClient = null;
	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.SENSOR_ACTIVITY;
	}

	public void sendData(List<DetectedActivity> liActivities) {
		for (int i = 0; i < liActivities.size(); i++) {
			DetectedActivity act = liActivities.get(i);

			SensorActivity sensorAct = new SensorActivity();
			sensorAct.setType(act.getType());
			sensorAct.setConfidence(act.getConfidence());
			sensorAct.setRanking(i);
			handleDatabaseObject(sensorAct);
		}
	}
	
	public void sendData(DetectedActivity act) {
			SensorActivity sensorAct = new SensorActivity();
			sensorAct.setType(act.getType());
			sensorAct.setConfidence(act.getConfidence());
			handleDatabaseObject(sensorAct);
	}
}
