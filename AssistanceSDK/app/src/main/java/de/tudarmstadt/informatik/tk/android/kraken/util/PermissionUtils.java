package de.tudarmstadt.informatik.tk.android.kraken.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

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

    /**
     * Generic check for given permission (Android M version)
     *
     * @param permission
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isPermissionGranted(String permission) {
        return mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Handles user permission action
     *
     * @param grantResults
     * @return
     */
    public boolean handlePermissionResult(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
