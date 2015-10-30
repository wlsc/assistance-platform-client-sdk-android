package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModule;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public class ModuleDaoImpl implements ModuleDao {

    private static final String TAG = ModuleDaoImpl.class.getSimpleName();

    private static ModuleDao INSTANCE;

    private DbModuleDao moduleDao;

    private ModuleDaoImpl(DaoSession daoSession) {

        if (moduleDao == null) {
            moduleDao = daoSession.getDbModuleDao();
        }
    }

    public static ModuleDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Returns module by its package id and user id
     *
     * @param modulePackageName
     * @param userId
     * @return
     */
    @Override
    public DbModule getModuleByPackageIdUserId(String modulePackageName, Long userId) {

        if (modulePackageName == null || userId < 0) {
            return null;
        }

        return moduleDao
                .queryBuilder()
                .where(DbModuleDao.Properties.PackageName.eq(modulePackageName))
                .where(DbModuleDao.Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Inserts new module
     *
     * @param module
     * @return
     */
    @Override
    public long insertModule(DbModule module) {

        if (module == null) {
            return -1L;
        }

        return moduleDao.insertOrReplace(module);
    }
}
