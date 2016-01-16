package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.service.AssistanceAccessibilityService;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * ServiceUtils
 *
 * @author Karsten Planz
 */
public class AccessibilityUtils {

    private static final String TAG = AccessibilityUtils.class.getSimpleName();

    private AccessibilityUtils() {
    }

    public static boolean isAccessibilityEnabled(Context context) {

        final String id = AssistanceAccessibilityService.class.getCanonicalName();

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (id.equals(service.getId())) {
                return true;
            }
        }

        return false;
    }

    private static void logInstalledAccessiblityServices(Context context) {

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getInstalledAccessibilityServiceList();
        for (AccessibilityServiceInfo service : runningServices) {
            Log.i(TAG, service.getId());
        }
    }

    public static void openAccessibiliySettings(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }
}
