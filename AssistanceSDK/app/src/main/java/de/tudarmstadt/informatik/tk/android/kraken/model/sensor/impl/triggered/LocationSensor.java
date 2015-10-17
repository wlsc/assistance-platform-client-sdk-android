package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;


public class LocationSensor extends AbstractTriggeredEvent implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = LocationSensor.class.getSimpleName();

    //------------------- Configuration -------------------
    // Accuracy
    private int ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    // Update frequency in seconds
    private int updateInterval = 15;
    // The fastest update frequency, in seconds
    private int FASTEST_INTERVAL_IN_SECONDS = 30;
    //-----------------------------------------------------

    private long mLastEventDumpingTimestamp = 0;    // in nanoseconds

    private GoogleApiClient mGoogleApiClient = null;
    private LocationRequest m_locationRequest = null;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private Double latitude;
    private Double longitude;
    private Double accuracyHorizontal;
    private Double accuracyVertical;
    private Float speed;
    private Double altitude;

    private DbProvider dbProvider;

    public LocationSensor(Context context) {
        super(context);

        if (dbProvider == null) {
            dbProvider = DbProvider.getInstance(context);
        }
    }

    @Override
    protected void dumpData() {

        DbPositionSensor dbPositionSensor = new DbPositionSensor();

        dbPositionSensor.setLatitude(latitude);
        dbPositionSensor.setLongitude(longitude);
        dbPositionSensor.setAccuracyHorizontal(accuracyHorizontal);
        dbPositionSensor.setAccuracyVertical(accuracyVertical);
        dbPositionSensor.setAltitude(altitude);
        dbPositionSensor.setSpeed(speed);
        dbPositionSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        dbProvider.insertEventEntry(dbPositionSensor, getType());
    }

    /**
     * Starts sensing
     */
    @Override
    public void startSensor() {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        // Create the LocationRequest object
        m_locationRequest = LocationRequest.create();
        // Use high accuracy
        m_locationRequest.setPriority(ACCURACY);
        // Set the update interval to x seconds
        m_locationRequest.setInterval(updateInterval * 1000);
        // Set the fastest update interval to x seconds
        m_locationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SECONDS * 1000);
        isRunning = true;
    }

    @Override
    public void stopSensor() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
        isRunning = false;
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
                connectionResult.startResolutionForResult((Activity) context, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e(TAG, "Cannot start resolution for location connection. Error: ", e);
                mGoogleApiClient.connect();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
            // showErrorDialog(connectionResult.getErrorCode());
            Log.e(TAG, "Cannot find any resolution for location connection error");
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        // maybe this is not a good choice, because maybe the last location is
        // out-dated?
        // But let's give it a try...
        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (loc != null) {
            onLocationChanged(loc);
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, m_locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Display the connection status
//		Toast.makeText(context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Location connection is suspended.");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        // location changed -> update values
        Log.d(TAG, "Location has changed.");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracyHorizontal = Double.valueOf(location.getAccuracy());
        accuracyVertical = Double.valueOf(location.getAccuracy());
        speed = location.getSpeed();
        altitude = location.getAltitude();

        // checks for saving new data
        if (isTimeToSaveData(System.nanoTime())) {

            mLastEventDumpingTimestamp = System.nanoTime();

            dumpData();
        }
    }

    /**
     * Checks for the time to save new sensor reading into db
     *
     * @param timestamp
     * @return
     */
    private boolean isTimeToSaveData(long timestamp) {

        // save the first sensor data
        if (mLastEventDumpingTimestamp == 0) {
            return true;
        } else {
            // the time has come -> save data into db
            if ((timestamp - mLastEventDumpingTimestamp) / 1000000000 > updateInterval) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getType() {
        return SensorType.LOCATION;
    }

    @Override
    public void reset() {

    }
}