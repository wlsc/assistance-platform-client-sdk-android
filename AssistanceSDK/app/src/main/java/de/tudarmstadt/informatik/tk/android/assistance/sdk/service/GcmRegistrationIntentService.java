package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device.DeviceRegistrationRequestDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.ApiProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

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

    private PreferenceProvider preferenceProvider;
    private DaoProvider daoProvider;

    private Subscription gcmRegistrationSubscriber;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GcmRegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceProvider = PreferenceProvider.getInstance(getApplicationContext());
        daoProvider = DaoProvider.getInstance(getApplicationContext());
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
            preferenceProvider.setSentTokenToServer(true);

        } catch (Exception e) {

            Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            preferenceProvider.setSentTokenToServer(false);
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

        final String userToken = preferenceProvider.getUserToken();
        final long serverDeviceId = preferenceProvider.getServerDeviceId();

        DeviceRegistrationRequestDto deviceRegistrationRequest = new DeviceRegistrationRequestDto();

        deviceRegistrationRequest.setDeviceId(serverDeviceId);
        deviceRegistrationRequest.setRegistrationToken(token);

        Observable<Void> subscription = ApiProvider.getInstance(getApplicationContext())
                .getDeviceApiProvider()
                .getDeviceRegistration(userToken, deviceRegistrationRequest);

        gcmRegistrationSubscriber = subscription.subscribe(new Subscriber<Void>() {

            @Override
            public void onCompleted() {
                Log.d(TAG, "gcmRegistrationSubscriber: onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "gcmRegistrationSubscriber: onError");
            }

            @Override
            public void onNext(Void aVoid) {

                // persist registration
                final String userToken = preferenceProvider.getUserToken();
                DbUser user = daoProvider.getUserDao().getByToken(userToken);

                if (user == null) {
                    Log.d(TAG, "No such user found! Token: " + userToken);
                    return;
                } else {

                    Log.d(TAG, "Saving GCM registration ID into DB...");

                    final long serverDeviceId = preferenceProvider.getServerDeviceId();

                    daoProvider.getDeviceDao().saveRegistrationTokenToDb(
                            token,
                            user.getId(),
                            serverDeviceId);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gcmRegistrationSubscriber != null) {
            gcmRegistrationSubscriber.unsubscribe();
        }
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
