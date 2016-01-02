package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import rx.Subscription;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.01.2016
 */
public class RxUtils {

    private RxUtils() {
    }

    /**
     * Unsubscribes a subscription
     *
     * @param subscription
     */
    public static void unsubscribe(Subscription subscription) {

        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
