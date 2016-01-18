package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleAllowedCapabilities;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleAllowedCapabilitiesDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.StringUtils;

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