package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class LocationSensor extends
        AbstractTriggeredEvent implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = LocationSensor.class.getSimpleName();

    //------------------- Configuration -------------------
    // Accuracy
    private static final int ACCURACY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 40;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 20;
    //-----------------------------------------------------

    private long mLastEventDumpingTimestamp;    // in nanoseconds

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9_000;

    private Double latitude;
    private Double longitude;
    private Double accuracyHorizontal;
    private Double accuracyVertical;
    private Float speed;
    private Double altitude;

    public LocationSensor(Context context) {
        super(context);

        mGoogleApiClient = getGoogleApiClient();
    }

    @NonNull
    private GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void dumpData() {

        DbPositionSensor dbPositionSensor = new DbPositionSensor();

        dbPositionSensor.setLatitude(latitude);
        dbPositionSensor.setLongitude(longitude);
        dbPositionSensor.setAccuracyHorizontal(accuracyHorizontal);
        dbPositionSensor.setAccuracyVertical(accuracyVertical);
        dbPositionSensor.setAltitude(altitude);
        dbPositionSensor.setSpeed(speed);
        dbPositionSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getLocationSensorDao().insert(dbPositionSensor);

        Log.d(TAG, "Finished");
    }

    /**
     * Starts sensing
     */
    @Override
    public void startSensor() {

        if (!isRunning()) {

            try {

                if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }

                // Create the LocationRequest object
                mLocationRequest = LocationRequest.create();
                // Use high accuracy
                mLocationRequest.setPriority(ACCURACY);
                // Set the update interval to x seconds
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_SECONDS * 1_000);
                // Set the fastest update interval to x seconds
                mLocationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SECONDS * 1_000);

                setRunning(true);

            } catch (Exception e) {
                Log.e(TAG, "Some exception: ", e);
                setRunning(false);
            }
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (mGoogleApiClient != null) {
                if (mGoogleApiClient.isConnected()) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }
        } finally {
            setRunning(false);
        }
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
        try {

            if (mGoogleApiClient == null) {
                mGoogleApiClient = getGoogleApiClient();
            }

            Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (loc != null) {
                onLocationChanged(loc);
            }

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest,
                        this);
            }

        } catch (SecurityException ex) {
            Log.d(TAG, "SecurityException: user disabled location permission!");
            stopSensor();
        } finally {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Display the connection status
        Log.d(TAG, "Location connection is suspended.");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        // location changed -> update values
        Log.d(TAG, "Location has changed");

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracyHorizontal = (double) location.getAccuracy();
        accuracyVertical = (double) location.getAccuracy();
        speed = location.getSpeed();
        altitude = location.getAltitude();

        // saving them to SharedPreferences to further fast access
        PreferenceProvider.getInstance(context).setLastLatitude(latitude);
        PreferenceProvider.getInstance(context).setLastLongitude(longitude);

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
            if ((timestamp - mLastEventDumpingTimestamp) / 1_000_000_000 > UPDATE_INTERVAL_IN_SECONDS) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getType() {
        return DtoType.LOCATION;
    }

    @Override
    public void reset() {

    }
}