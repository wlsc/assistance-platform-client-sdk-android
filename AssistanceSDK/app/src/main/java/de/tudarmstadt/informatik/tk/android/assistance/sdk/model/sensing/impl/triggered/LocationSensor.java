package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

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
    private int UPDATE_INTERVAL_IN_SEC = 20;
    // The fastest update frequency, in seconds
    private int FASTEST_INTERVAL_IN_SEC = 15;
    //-----------------------------------------------------

    private static LocationSensor INSTANCE;

    private GoogleApiClient mGoogleApiClient;

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9_000;

    private Double latitude;
    private Double longitude;
    private Double accuracyHorizontal;
    private Double accuracyVertical;
    private Float speed;
    private Double altitude;

    private LocationSensor(Context context) {
        super(context);

        mGoogleApiClient = getGoogleApiClient();
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static LocationSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new LocationSensor(context);
        }

        return INSTANCE;
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
     * Update intervals
     */
    @Override
    public void updateSensorInterval(Double collectionInterval) {

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old fastest interval: " + FASTEST_INTERVAL_IN_SEC + " sec");

        int newFastestUpdateIntervalInSec = collectionInterval.intValue();

        Log.d(TAG, "New fastest interval: " + newFastestUpdateIntervalInSec + " sec");

        FASTEST_INTERVAL_IN_SEC = newFastestUpdateIntervalInSec;

        // calc +10% to new normal update interval
        int newNormalUpdateIntervalInSec = newFastestUpdateIntervalInSec +
                ((int) Math.round(newFastestUpdateIntervalInSec * 0.1));

        Log.d(TAG, "Old normal interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        UPDATE_INTERVAL_IN_SEC = newNormalUpdateIntervalInSec;

        Log.d(TAG, "New normal interval: " + newNormalUpdateIntervalInSec + " sec");

        if (mGoogleApiClient.isConnected()) {

            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        getNewLocationRequest(),
                        this);
            } catch (SecurityException ex) {
                Log.d(TAG, "SecurityException: user disabled location permission!");
                stopSensor();
            }
        }
    }

    public int getUpdateIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    public void setUpdateIntervalInSec(int updateIntervalInSec) {
        UPDATE_INTERVAL_IN_SEC = updateIntervalInSec;
    }

    public int getFastestIntervalInSec() {
        return FASTEST_INTERVAL_IN_SEC;
    }

    public void setFastestIntervalInSec(int fastestIntervalInSec) {
        FASTEST_INTERVAL_IN_SEC = fastestIntervalInSec;
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

                setRunning(true);

            } catch (Exception e) {
                Log.e(TAG, "Some exception: ", e);
                setRunning(false);
            }
        }
    }

    /**
     * Returns new location request for Google API client
     *
     * @return
     */
    private LocationRequest getNewLocationRequest() {

        // Create the LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();
        // Use high accuracy
        locationRequest.setPriority(ACCURACY);
        // Set the update interval to x seconds
        locationRequest.setInterval(UPDATE_INTERVAL_IN_SEC * 1_000);
        // Set the fastest update interval to x seconds
        locationRequest.setFastestInterval(FASTEST_INTERVAL_IN_SEC * 1_000);

        return locationRequest;
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
                        getNewLocationRequest(),
                        this);
            }

        } catch (SecurityException ex) {
            Log.d(TAG, "SecurityException: user disabled location permission!");
            stopSensor();
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

        dumpData();
    }

    @Override
    public int getType() {
        return DtoType.LOCATION;
    }

    @Override
    public void reset() {

    }
}