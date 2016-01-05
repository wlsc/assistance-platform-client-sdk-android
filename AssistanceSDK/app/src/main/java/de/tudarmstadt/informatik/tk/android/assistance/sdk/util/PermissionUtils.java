package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;

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

    private Map<String, String[]> dangerousPermissionsToDtoMapping;

    private PermissionUtils(Context context) {
        this.mContext = context;

        createDangerousPermissionsToDtoMappings();
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
     * creating mapping between API definitions and permissions
     * key: capability type of a module
     * value: needed permissions for that
     */
    private void createDangerousPermissionsToDtoMappings() {

        dangerousPermissionsToDtoMapping = new HashMap<>();

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CALENDAR),
                new String[]{
                        Manifest.permission.READ_CALENDAR//,
//                        Manifest.permission.WRITE_CALENDAR
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CALL_LOG),
                new String[]{
                        Manifest.permission.READ_CALL_LOG//,
//                        Manifest.permission.WRITE_CALL_LOG
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CONTACT),
                new String[]{
//                        Manifest.permission.GET_ACCOUNTS,
//                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.LOCATION),
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }
        );
    }

    public Map<String, String[]> getDangerousPermissionsToDtoMapping() {
        return this.dangerousPermissionsToDtoMapping;
    }

    /**
     * Generic check for given permission (Android M version)
     *
     * @param permission
     * @return
     */
    public boolean isGranted(String permission) {
        return permission != null && ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Handles user permission action
     *
     * @param grantResults
     * @return
     */
    public boolean handlePermissionResult(@NonNull int[] grantResults) {
        return !(grantResults == null || grantResults.length <= 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED;

    }
}
