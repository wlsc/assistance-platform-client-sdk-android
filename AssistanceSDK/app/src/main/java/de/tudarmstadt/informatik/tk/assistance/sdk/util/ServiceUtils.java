package de.tudarmstadt.informatik.tk.assistance.sdk.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.SparseArray;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.ISensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.12.2015
 */
public class ServiceUtils {

    private static final String TAG = ServiceUtils.class.getSimpleName();

    private ServiceUtils() {
    }


    /**
     * Checks if sensing service is able to run -> user have active modules
     *
     * @param context
     * @return
     */
    public static boolean isHarvesterAbleToRun(Context context) {

        String userToken = PreferenceProvider.getInstance(context).getUserToken();

        if (StringUtils.isNullOrEmpty(userToken)) {
            Log.d(TAG, "User token is null");
            return false;
        }

        DbUser user = DaoProvider.getInstance(context).getUserDao().getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "User is null");
            return false;
        }

        List<DbModule> activeModules = DaoProvider.getInstance(context)
                .getModuleDao()
                .getAllActive(user.getId());

        SparseArray<ISensor> runningSensors = SensorProvider.getInstance(context).getRunningSensors();

        return activeModules != null && !activeModules.isEmpty() && runningSensors.size() > 0;
    }

    /**
     * Returns true if user has any modules installed
     *
     * @param context
     * @return
     */
    public static boolean hasUserModules(Context context) {

        String userToken = PreferenceProvider.getInstance(context).getUserToken();

        if (StringUtils.isNullOrEmpty(userToken)) {
            Log.d(TAG, "User token is null");
            return false;
        }

        DbUser user = DaoProvider.getInstance(context).getUserDao().getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "User is null");
            return false;
        }

        List<DbModule> userModules = DaoProvider.getInstance(context)
                .getModuleDao()
                .getAll(user.getId());

        return userModules != null && !userModules.isEmpty();
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