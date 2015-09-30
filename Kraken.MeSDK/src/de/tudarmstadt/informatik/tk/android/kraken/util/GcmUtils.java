package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenConfig;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.09.2015
 */
public class GcmUtils {

    private static final String TAG = GcmUtils.class.getSimpleName();

    private GcmUtils() {
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean isPlayServicesInstalled(Activity activity) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (apiAvailability.isUserResolvableError(resultCode)) {

                apiAvailability
                        .getErrorDialog(activity, resultCode, KrakenConfig.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported!");
            }

            return false;
        }

        return true;
    }
}
