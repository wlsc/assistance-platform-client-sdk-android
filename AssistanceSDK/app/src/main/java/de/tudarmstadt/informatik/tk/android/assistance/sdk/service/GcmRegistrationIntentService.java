package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.ServerCommunicationProvider;

/**
 * Does GCM mobile client registration
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.09.2015
 */
public class GcmRegistrationIntentService extends IntentService {

    private static final String TAG = GcmRegistrationIntentService.class.getSimpleName();

    private static final String TOPICS_SERVICE = "/topics/";
    private static final String[] TOPICS = {"global"};

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GcmRegistrationIntentService() {
        super(TAG);
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            Log.d(TAG, "Trying to get GCM registration token...");

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(Config.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.d(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            PreferenceProvider.getInstance(getApplicationContext()).setSentTokenToServer(true);

        } catch (Exception e) {

            Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            PreferenceProvider.getInstance(getApplicationContext()).setSentTokenToServer(false);
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(PreferenceProvider.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to server
     *
     * @param token The new token
     */
    private void sendRegistrationToServer(String token) {

        Log.d(TAG, "Sending GCM registration token to backend...");

        final ServerCommunicationProvider communicationProvider = ServerCommunicationProvider.getInstance(getApplicationContext());
        communicationProvider.sendGcmRegistrationToken(token);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {

        GcmPubSub pubSub = GcmPubSub.getInstance(this);

        for (String topic : TOPICS) {
            pubSub.subscribe(token, TOPICS_SERVICE + topic, null);
        }
    }
}
