package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public class ModuleDaoImpl implements ModuleDao {

    private static final String TAG = ModuleDaoImpl.class.getSimpleName();

    private static ModuleDao INSTANCE;

    private DbModuleDao dao;

    private ModuleDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbModuleDao();
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

        return dao
                .queryBuilder()
                .where(DbModuleDao.Properties.PackageName.eq(modulePackageName))
                .where(DbModuleDao.Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbModule> getAllModules(Long userId) {

        if (userId == null || userId < 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbModuleDao.Properties.UserId.eq(userId))
                .build()
                .list();
    }

    @Override
    public List<DbModule> getAllActiveModules(Long userId) {

        if (userId == null || userId < 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbModuleDao.Properties.UserId.eq(userId))
                .where(DbModuleDao.Properties.Active.eq(Boolean.TRUE))
                .build()
                .list();
    }

    @Override
    public List<DbModule> getAllModules() {
        return dao
                .queryBuilder()
                .build()
                .list();
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
            return -1l;
        }

        return dao.insertOrReplace(module);
    }

    @Override
    public void delete(List<DbModule> dbItems) {

        if (dbItems == null || dbItems.isEmpty()) {
            return;
        }

        dao.deleteInTx(dbItems);
    }
}
