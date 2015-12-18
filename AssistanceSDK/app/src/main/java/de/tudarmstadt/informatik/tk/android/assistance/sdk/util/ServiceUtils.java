package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.12.2015
 */
public class ServiceUtils {

    private static final String TAG = ServiceUtils.class.getSimpleName();

    private ServiceUtils() {
    }


    /**
     * Checks if sensing service is able to run
     *
     * @param context
     * @return
     */
    public static boolean isHarvesterAbleToRun(Context context) {

        String userToken = PreferenceProvider.getInstance(context).getUserToken();

        if (StringUtils.isNullOrEmpty(userToken)) {
            return false;
        }

        DbUser user = DaoProvider.getInstance(context).getUserDao().getByToken(userToken);

        if (user == null) {
            return false;
        }

        List<DbModule> activeModules = DaoProvider.getInstance(context)
                .getModuleDao()
                .getAllActive(user.getId());

        return activeModules != null && !activeModules.isEmpty();
    }

    /**
     * Checks if some given service is currently running
     *
     * @param context
     * @param clazz
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> clazz) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        // getting all services
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (clazz.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns list of running services
     *
     * @param context
     * @return
     */
    public static List<ActivityManager.RunningServiceInfo> getRunningServices(
            final Context context,
            final int maxAmount) {

        if (context == null || maxAmount <= 0) {
            return Collections.emptyList();
        }

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager == null) {
            return Collections.emptyList();
        }

        return activityManager.getRunningServices(maxAmount);
    }
}