package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.triggered;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorLocation;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.TrafficLocation;

public class LocationSensor extends AbstractTriggeredSensor implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    public static enum Configuration {
        ACCURACY, UPDATE_INTERVAL_IN_SECONDS, FASTEST_INTERVAL_IN_SECONDS
    };

    //------------------- Configuration -------------------
    // Accuracy
    private int ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    // Update frequency in seconds
    private int UPDATE_INTERVAL_IN_SECONDS = 60;
    // The fastest update frequency, in seconds
    private int FASTEST_INTERVAL_IN_SECONDS = 30;
    //-----------------------------------------------------


    private LocationClient m_locationClient = null;
    private LocationRequest m_locationRequest = null;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public LocationSensor(Context context) {
        super(context);
    }

    public void startSensor() {
        m_locationClient = new LocationClient(m_context, this, this);
        // Create the LocationRequest object
        m_locationRequest = LocationRequest.create();
        // Use high accuracy
        m_locationRequest.setPriority(ACCURACY);
        // Set the update interval to x seconds
        m_locationRequest.setInterval(UPDATE_INTERVAL_IN_SECONDS * 1000);
        // Set the fastest update interval to x seconds
        m_locationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SECONDS * 1000);
        m_locationClient.connect();
        m_bIsRunning = true;
   }

    public void stopSensor() {
        if (m_locationClient != null) {
            if (m_locationClient.isConnected()) {
                m_locationClient.removeLocationUpdates(this);
                m_locationClient.disconnect();
            }
        }
        m_bIsRunning = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult((Activity) m_context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
            // showErrorDialog(connectionResult.getErrorCode());
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // maybe this is not a goot choice, because maybe the last location is
        // out-dated?
        // But let's give it a try...
        Location loc = m_locationClient.getLastLocation();
        if (loc != null)
            onLocationChanged(loc);

        m_locationClient.requestLocationUpdates(m_locationRequest, this);
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
//		Toast.makeText(m_context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        SensorLocation sensorLocation = new SensorLocation();
        sensorLocation.setAccuracy(location.getAccuracy());
        sensorLocation.setLongitude(location.getLongitude());
        sensorLocation.setLatitude(location.getLatitude());
        sensorLocation.setSpeed(location.getSpeed());
        sensorLocation.setProvider(location.getProvider());
        handleDatabaseObject(sensorLocation);
        TrafficLocation.getInstance().setLatitude(location.getLatitude());
        TrafficLocation.getInstance().setLongitude(location.getLongitude());
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_LOCATION;
    }
}