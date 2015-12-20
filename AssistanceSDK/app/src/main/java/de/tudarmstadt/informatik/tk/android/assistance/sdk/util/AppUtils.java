package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.UUID;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.10.2015
 */
public class AppUtils {

    private AppUtils() {
    }

    /**
     * Returns true if debuggable flag is enabled
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        return 0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
    }

    /**
     * Generates new random UUID string
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
