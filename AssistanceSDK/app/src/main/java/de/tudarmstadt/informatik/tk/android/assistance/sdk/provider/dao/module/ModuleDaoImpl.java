package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public class ModuleDaoImpl extends
        CommonDaoImpl<DbModule> implements
        ModuleDao {

    private static final String TAG = ModuleDaoImpl.class.getSimpleName();

    private static ModuleDao INSTANCE;

    private ModuleDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbModuleDao());
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
    @Nullable
    @Override
    public DbModule getByPackageIdUserId(String modulePackageName, Long userId) {

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
    public List<DbModule> getAll(Long userId) {

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
    public List<DbModule> getAllActive(Long userId) {

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

    @Nullable
    @Override
    public DbModule get(Long moduleId) {

        if (moduleId == null || moduleId < 0) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbModuleDao.Properties.Id.eq(moduleId))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbModule> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbModuleDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
