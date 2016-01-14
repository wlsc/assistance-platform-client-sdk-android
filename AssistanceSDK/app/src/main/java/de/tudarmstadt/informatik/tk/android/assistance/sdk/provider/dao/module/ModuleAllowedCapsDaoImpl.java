package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleAllowedCapabilities;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleAllowedCapabilitiesDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.StringUtils;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 09.01.2016
 */
public class ModuleAllowedCapsDaoImpl extends
        CommonDaoImpl<DbModuleAllowedCapabilities> implements
        ModuleAllowedCapsDao {

    private static final String TAG = ModuleAllowedCapsDaoImpl.class.getSimpleName();

    private static ModuleAllowedCapsDao INSTANCE;

    private ModuleAllowedCapsDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbModuleAllowedCapabilitiesDao());
    }

    public static ModuleAllowedCapsDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleAllowedCapsDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }


    @Nullable
    @Override
    public DbModuleAllowedCapabilities get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbModuleAllowedCapabilitiesDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbModuleAllowedCapabilities> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbModuleAllowedCapabilitiesDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Nullable
    @Override
    public DbModuleAllowedCapabilities get(String type, Long userId) {

        if (StringUtils.isNullOrEmpty(type) || userId == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbModuleAllowedCapabilitiesDao.Properties.Type.eq(type))
                .where(DbModuleAllowedCapabilitiesDao.Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }
}