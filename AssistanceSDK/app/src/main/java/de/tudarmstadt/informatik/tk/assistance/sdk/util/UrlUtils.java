package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 20.01.2016
 */
public final class UrlUtils {

    private UrlUtils() {
    }

    /**
     * Checks if given URL is valid URL
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        return URLUtil.isValidUrl(url);
    }

    /**
     * Starts default browser with given URL
     *
     * @param context
     * @param url
     */
    public static void openUrl(Context context, String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}