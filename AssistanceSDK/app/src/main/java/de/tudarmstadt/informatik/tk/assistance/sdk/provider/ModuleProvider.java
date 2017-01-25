package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.PermissionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.12.2015
 */
public final class ModuleProvider {

    private static final String TAG = ModuleProvider.class.getSimpleName();

    private static Context mContext;

    private static ModuleProvider INSTANCE;

    private static PreferenceProvider preferenceProvider;

    private static SensorProvider sensorProvider;

    private static DaoProvider daoProvider;

    private ModuleProvider(Context context) {
        this.mContext = context;
    }

    public static ModuleProvider getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = new ModuleProvider(context);

            preferenceProvider = PreferenceProvider.getInstance(context);
            daoProvider = DaoProvider.getInstance(context);
            sensorProvider = SensorProvider.getInstance(context);
        }

        mContext = context;

        return INSTANCE;
    }

    /**
     * Enables or disables module
     *
     * @param moduleId
     * @param isActive
     */
    public void toggleModuleState(long moduleId, boolean isActive) {

        // update db entry
        boolean wasUpdated = updateDb(moduleId, isActive);

        // update running sensors
        if (wasUpdated) {
            updateRunningSensors(moduleId, isActive);
        } else {
            Log.d(TAG, "Module was NOT updated in DB.");
        }
    }

    /**
     * Updates running sensors/events state
     *
     * @param moduleId
     * @param isActive
     */
    private void updateRunningSensors(long moduleId, boolean isActive) {

        DbModule module = daoProvider.getModuleDao().get(moduleId);

        if (module == null) {
            Log.d(TAG, "Module was NULL!");
            return;
        }

        List<DbModuleCapability> capabilities = module.getDbModuleCapabilityList();

        if (capabilities == null) {
            Log.d(TAG, "Module capabilities were NULL!");
            return;
        }

        final Set<Integer> dtoTypes = new HashSet<>(capabilities.size());

        for (DbModuleCapability capability : capabilities) {

            // only when that capability was previously active
            if (capability.getActive()) {
                dtoTypes.add(SensorApiType.getDtoType(capability.getType()));
            }
        }

        if (isActive) {

            sensorProvider.activateSensors(dtoTypes);

            EventBus eventBus = EventBus.getDefault();

            if (eventBus.hasSubscriberForEvent(UpdateSensorIntervalEvent.class)) {

                // update collections frequencies
                for (Integer dtoType : dtoTypes) {

                    Double collectionFreq = sensorProvider.getCollectionInterval(
                            SensorApiType.getApiName(dtoType));

                    // fire change event
                    eventBus.post(new UpdateSensorIntervalEvent(dtoType, collectionFreq));
                }
            }

        } else {
            // deactivate given sensors

            // filter module capabilities
            // check if deactivating module is in required capabilities from another module
            Iterator<Integer> iterator = dtoTypes.iterator();
            while (iterator.hasNext()) {

                Integer dtoType = iterator.next();

                boolean isAnotherModuleRequired = isAnotherModuleUsingRequired(module, dtoType);

                if (isAnotherModuleRequired) {
                    iterator.remove();
                }
            }

            if (!dtoTypes.isEmpty()) {
                sensorProvider.deactivateSensors(dtoTypes);
            }
        }
    }

    /**
     * Checks if given DTO type is being used in anothers module required capabilities
     *
     * @param module
     * @param dtoType
     * @return
     */
    private boolean isAnotherModuleUsingRequired(DbModule module, Integer dtoType) {

        if (module == null || dtoType == null) {
            return false;
        }

        String typeStr = SensorApiType.getApiName(dtoType);

        List<DbModule> allActiveModules = daoProvider.getModuleDao().getAllActive(module.getUserId());

        for (DbModule activeModule : allActiveModules) {

            if (!activeModule.getId().equals(module.getId())) {

                List<DbModuleCapability> capabilities = activeModule.getDbModuleCapabilityList();

                for (DbModuleCapability cap : capabilities) {

                    if (cap.getRequired() && typeStr.equals(cap.getType())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Updates module's state in database
     *
     * @param moduleId
     * @param isActive
     * @return
     */
    private boolean updateDb(long moduleId, boolean isActive) {

        Log.d(TAG, "Updating database...");

        DbModule module = daoProvider.getModuleDao().get(moduleId);

        if (module == null) {
            Log.d(TAG, "Module was NULL!");
            return false;
        }

        module.setActive(isActive);

        daoProvider.getModuleDao().update(module);

        Log.d(TAG, "Finished db update");

        return true;
    }

    /**
     * Checks optional module capability permissions
     *
     * @param activity
     * @param capability
     */
    public String[] getNotGrantedModuleCapabilityPermission(Activity activity, DbModuleCapability capability) {

        List<String> result = new ArrayList<>();
        String[] permsToCheck = PermissionUtils.getDangerousPermissionsToDtoMapping()
                .get(capability.getType());

        if (permsToCheck != null) {

            for (String perm : permsToCheck) {

                // check permissions for that action
                int res = ContextCompat.checkSelfPermission(
                        mContext,
                        perm
                );

                if (res != PackageManager.PERMISSION_GRANTED) {
                    result.add(perm);
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * Returns title of a module
     *
     * @param modulePackageName
     * @param userId
     * @return
     */
    public String getModuleTitle(String modulePackageName, long userId) {

        if (modulePackageName == null || modulePackageName.isEmpty()) {
            return "";
        }

        DbModule module = daoProvider.getModuleDao().getByPackageIdUserId(modulePackageName, userId);

        if (module == null) {
            return "";
        }

        return module.getTitle();
    }

    /**
     * Returns title of a module
     *
     * @param modulePackageName
     * @return
     */
    public String getModuleTitle(String modulePackageName) {

        if (modulePackageName == null || modulePackageName.isEmpty()) {
            return "";
        }

        DbModule module = daoProvider.getModuleDao().getAnyByPackageId(modulePackageName);

        if (module == null) {
            return "";
        }

        return module.getTitle();
    }
}