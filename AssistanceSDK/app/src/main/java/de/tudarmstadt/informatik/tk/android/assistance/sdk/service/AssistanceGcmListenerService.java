package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * Google Cloud Messaging receiver
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 22.09.2015
 */
public class AssistanceGcmListenerService extends GcmListenerService {

    private static final String TAG = AssistanceGcmListenerService.class.getSimpleName();

    public AssistanceGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d(TAG, "GCM Message received");

        // make sure sender is OUR server
        if (Config.GCM_SENDER_ID.equals(from)) {

            String message = data.getString("message");

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + message);

            showNotification(message);
        } else {
            Log.d(TAG, "GCM Message was sent from UNKNOWN server. Notification was dropped!");
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void showNotification(String message) {

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_kraken_service)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true);
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}