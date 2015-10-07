package de.tudarmstadt.informatik.tk.android.kraken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import de.tudarmstadt.informatik.tk.android.kraken.api.ServerCommunication;

public class GcmManager {

    private static final String TAG = GcmManager.class.getSimpleName();

    private static final String PROPERTY_REG_ID = "GcmRegId";
    private static final String APP_VERSION = "AppVersion";

    private static GcmManager instance;

    private static Context mContext;
    private GoogleCloudMessaging mGCM;
    private String registrationToken;

    private GcmManager(Context context) {
        this.mContext = context;
    }

    public static GcmManager getInstance(Context context) {

        if (instance == null) {
            instance = new GcmManager(context);
        }

        return instance;
    }

    public static GcmManager getInstance() {
        return instance;
    }

    public void registerAtCloud() {

        mGCM = GoogleCloudMessaging.getInstance(mContext);
        registrationToken = getRegistrationId(mContext);

        if (registrationToken.isEmpty()) {
            registerInBackground();
        }

    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        return mContext.getSharedPreferences(Settings.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {

        new AsyncTask<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (mGCM == null) {
                        mGCM = GoogleCloudMessaging.getInstance(mContext);
                    }
                    registrationToken = mGCM.register(Config.GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + registrationToken;

                    // You should send the registration ID to your server over
                    // HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your
                    // app.
                    // The request to your server should be authenticated if
                    // your app
                    // is using accounts.

                    sendRegistrationIdToBackend(registrationToken);


                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

        }.execute();

    }

    public void sendRegistrationIdToBackend(String registrationToken) {

        Log.d(TAG, "Sending GCM registration token to backend...");

        ServerCommunication.getInstance(mContext).sendGcmRegistrationToken(registrationToken);
    }
}
