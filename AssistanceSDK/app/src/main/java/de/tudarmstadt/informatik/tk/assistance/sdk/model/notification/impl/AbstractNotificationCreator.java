package de.tudarmstadt.informatik.tk.assistance.sdk.model.notification.impl;

import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.notification.NotificationCreator;

/**
 * Abstract notification creator compat
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.10.2015
 */
public abstract class AbstractNotificationCreator implements NotificationCreator {

    private static final String TAG = AbstractNotificationCreator.class.getSimpleName();

    protected Context context;

    protected final int NOTIFICATION_ID;

    public AbstractNotificationCreator(Context context, int notificationId) {

        this.context = context;
        this.NOTIFICATION_ID = notificationId;
    }

    /**
     * Shows current notification
     */
    @Override
    public void show() {

        final NotificationManagerCompat from = NotificationManagerCompat.from(context);

        if (from != null) {
            from.notify(NOTIFICATION_ID, build());
        }
    }

    /**
     * Shows current notification in given foreground service
     *
     * @param service
     */
    @Override
    public void showForegroundInService(Service service) {

        if (service != null) {
            service.startForeground(NOTIFICATION_ID, build());
        }
    }

    /**
     * Cancels current running notification
     */
    @Override
    public void cancel() {

        final NotificationManagerCompat from = NotificationManagerCompat.from(context);

        if (from != null) {
            from.cancel(NOTIFICATION_ID);
        }
    }
}
