package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.01.2016
 */
public enum RxUtils {
    ;

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

    /**
     * Unsubscribes group of subscription
     *
     * @param subscriptions
     */
    public static void unsubscribe(CompositeSubscription subscriptions) {

        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }
}
