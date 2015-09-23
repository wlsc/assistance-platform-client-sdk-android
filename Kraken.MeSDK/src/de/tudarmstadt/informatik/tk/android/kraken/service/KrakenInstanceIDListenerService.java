package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * The service is called by a server when access token to GCM was compromised
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 22.09.2015
 */
public class KrakenInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = KrakenInstanceIDListenerService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {

        Log.d(TAG, "onTokenRefresh was invoked! Refreshing token...");

        // TODO: implement refresh token
    }
}
