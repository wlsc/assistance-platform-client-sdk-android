package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.content.Context;

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
}