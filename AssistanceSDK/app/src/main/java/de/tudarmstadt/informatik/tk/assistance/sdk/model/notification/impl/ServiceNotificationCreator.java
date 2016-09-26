package de.tudarmstadt.informatik.tk.assistance.sdk.model.notification.impl;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat.Builder;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.R.drawable;
import de.tudarmstadt.informatik.tk.assistance.sdk.R.string;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.10.2015
 */
public class ServiceNotificationCreator extends AbstractNotificationCreator {

    /**
     * Notification stays in notification area
     */
    private static final boolean isOngoing = true;

    private static final boolean isSummaryForGroup = true;

    /**
     * Default constructor
     *
     * @param context
     */
    public ServiceNotificationCreator(Context context) {
        super(context, Config.DEFAULT_NOTIFICATION_ID);
    }

    /**
     * Adjustable constructor for changing notification id
     *
     * @param context
     * @param notificationId
     */
    public ServiceNotificationCreator(Context context, int notificationId) {
        super(context, notificationId);
    }

    @Override
    public Notification build() {

        Builder builder = new Builder(context)
                .setOngoing(isOngoing)
                .setSmallIcon(drawable.ic_assistance_service)
                .setContentTitle(context.getText(string.service_running_notification_title))
                .setContentText(context.getText(string.service_running_notification_text))
                .setGroup(Config.DEFAULT_NOTIFICATION_GROUP)
                .setGroupSummary(isSummaryForGroup);

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), drawable.kraki_big));

        return builder.build();
    }
}
