package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public final class ModuleDaoImpl extends
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
                .where(Properties.PackageName.eq(modulePackageName))
                .where(Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }

    @Nullable
    @Override
    public DbModule getAnyByPackageId(String modulePackageName) {

        if (modulePackageName == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.PackageName.eq(modulePackageName))
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
                .where(Properties.UserId.eq(userId))
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
                .where(Properties.UserId.eq(userId))
                .where(Properties.Active.eq(Boolean.TRUE))
                .build()
                .list();
    }
}