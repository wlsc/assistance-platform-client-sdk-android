package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.12.2015
 */
public class ModuleProvider {

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

        Set<Integer> dtoTypes = new HashSet<>();

        for (DbModuleCapability capability : capabilities) {

            // only when that capability was previously active
            if (capability.getActive()) {
                dtoTypes.add(DtoType.getDtoType(capability.getType()));
            }
        }

        if (isActive) {

            sensorProvider.activateSensors(dtoTypes);

            // update collections frequencies
            for (Integer dtoType : dtoTypes) {

                Double collectionFreq = sensorProvider.getCollectionFrequency(
                        DtoType.getApiName(dtoType));

                // fire change event
                EventBus.getDefault().post(new UpdateSensorIntervalEvent(dtoType, collectionFreq));
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

        String typeStr = DtoType.getApiName(dtoType);

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
}