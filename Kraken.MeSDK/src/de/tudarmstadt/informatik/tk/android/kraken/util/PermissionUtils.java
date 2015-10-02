package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.content.Context;

/**
 * Provides permission checks on runtime
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.10.2015
 */
public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static PermissionUtils INSTANCE;

    private final Context mContext;

    private PermissionUtils(Context context) {
        this.mContext = context;
    }

    /**
     * Initializes and returns an instance of this class
     *
     * @param context
     * @return
     */
    public static PermissionUtils getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new PermissionUtils(context);
        }

        return INSTANCE;
    }


//    public boolean isInternetGranted() {
//
//        int permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET);
//
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
