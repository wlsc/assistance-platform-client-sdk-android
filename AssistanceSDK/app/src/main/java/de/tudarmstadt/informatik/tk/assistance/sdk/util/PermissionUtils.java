package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * Provides permission checks on runtime
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.10.2015
 */
public enum PermissionUtils {
    ;

    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static Map<String, String[]> getDangerousPermissionsToDtoMapping() {

        Map<String, String[]> dangerousPermissionsToDtoMapping = new HashMap<>();

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CALENDAR),
                new String[]{
                        permission.READ_CALENDAR//,
//                        Manifest.permission.WRITE_CALENDAR
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CALL_LOG),
                new String[]{
                        permission.READ_CALL_LOG//,
//                        Manifest.permission.WRITE_CALL_LOG
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.CONTACT),
                new String[]{
//                        Manifest.permission.GET_ACCOUNTS,
//                        Manifest.permission.WRITE_CONTACTS,
                        permission.READ_CONTACTS
                }
        );

        dangerousPermissionsToDtoMapping.put(
                SensorApiType.getApiName(SensorApiType.LOCATION),
                new String[]{
                        permission.ACCESS_COARSE_LOCATION,
                        permission.ACCESS_FINE_LOCATION
                }
        );

        return dangerousPermissionsToDtoMapping;
    }

    /**
     * Generic check for given permission (Android M version)
     *
     * @param permission
     * @return
     */
    public static boolean isGranted(Context context, String permission) {
        return permission != null && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Handles user permission action
     *
     * @param grantResults
     * @return
     */
    public static boolean handlePermissionResult(@NonNull int... grantResults) {
        return !(grantResults == null || grantResults.length <= 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Checks if some of permissions were granted
     *
     * @param perms
     * @return
     */
    public static boolean isGranted(String... perms) {

        if (perms == null) {
            return false;
        }

        boolean result = false;

        for (String perm : perms) {
            if (isGranted(perm)) {
                result = true;
                break;
            }
        }

        return result;
    }
}
