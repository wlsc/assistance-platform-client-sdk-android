package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallation;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallationDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ModuleInstallationDaoImpl implements ModuleInstallationDao {

    private static final String TAG = ModuleCapabilityDaoImpl.class.getSimpleName();

    private static ModuleInstallationDao INSTANCE;

    private DbModuleInstallationDao moduleInstallationDao;

    private ModuleInstallationDaoImpl(DaoSession daoSession) {

        if (moduleInstallationDao == null) {
            moduleInstallationDao = daoSession.getDbModuleInstallationDao();
        }
    }

    public static ModuleInstallationDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleInstallationDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Returns db module installation by user id
     *
     * @param userId
     * @return
     */
    @Override
    public List<DbModuleInstallation> getModuleInstallationsByUserId(long userId) {

        if (userId < 0) {
            return Collections.emptyList();
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .build()
                .list();
    }

    /**
     * Returns db module installations by user id and module id
     *
     * @param userId
     * @param moduleId
     * @return
     */
    @Override
    public List<DbModuleInstallation> getModuleInstallationsByUserId(long userId, long moduleId) {

        if (userId < 0) {
            return Collections.emptyList();
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .where(DbModuleInstallationDao.Properties.ModuleId.eq(moduleId))
                .build()
                .list();
    }

    /**
     * Returns db module installation by user id and module id
     *
     * @param userId
     * @return
     */
    @Override
    public DbModuleInstallation getModuleInstallationForModuleByUserId(long userId, long moduleId) {

        if (userId < 0) {
            return null;
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .where(DbModuleInstallationDao.Properties.ModuleId.eq(moduleId))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Inserts new module installation
     *
     * @param moduleInstallation
     * @return
     */
    @Override
    public long insertModuleInstallation(DbModuleInstallation moduleInstallation) {

        if (moduleInstallation == null) {
            return -1l;
        }

        return moduleInstallationDao.insertOrReplace(moduleInstallation);
    }

    /**
     * Updates module installation
     *
     * @param moduleInstallation
     */
    @Override
    public void updateModuleInstallation(DbModuleInstallation moduleInstallation) {

        if (moduleInstallation == null) {
            return;
        }

        moduleInstallationDao.update(moduleInstallation);
    }

    /**
     * Removes installed modules
     *
     * @param moduleInstallations
     */
    @Override
    public void removeInstalledModules(List<DbModuleInstallation> moduleInstallations) {

        if (moduleInstallations == null) {
            return;
        }

        moduleInstallationDao.deleteInTx(moduleInstallations);
    }
}
