package de.tudarmstadt.informatik.tk.android.kraken.model.notification;

import android.app.Notification;
import android.app.Service;

/**
 * Common notification creator compat interface
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.10.2015
 */
public interface NotificationCreator {

    Notification build();

    void show();

    void showForegroundInService(Service service);

    void cancel();

}
