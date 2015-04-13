package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.sdk.R;

/**
 * ServiceUtils
 *
 * @author Karsten Planz
 */
public class AccessibilityUtils {

    private static final String TAG = "AccessibilityUtils";

    public static void checkAccessibilityService(final Context context) {

        if (!isAccessibilityEnabled(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.alert_accessibility);
            builder.setPositiveButton(R.string.alert_accessibility_button,
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openAccessibiliySettings(context);
                }
            });
            if(!((Activity)context).isFinishing()) {
                builder.create().show();
            }
        }
    }

    public static boolean isAccessibilityEnabled(Context context) {

        //logInstalledAccessiblityServices(context);

        final String id = "de.tudarmstadt.informatik.tk.kraken.android/.KrakenAccessibilityService";

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
