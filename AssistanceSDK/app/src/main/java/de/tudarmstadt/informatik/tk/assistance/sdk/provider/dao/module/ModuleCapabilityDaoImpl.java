package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapabilityDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class ModuleCapabilityDaoImpl extends
        CommonDaoImpl<DbModuleCapability> implements
        ModuleCapabilityDao {

    private static final String TAG = ModuleCapabilityDaoImpl.class.getSimpleName();

    @Inject
    public ModuleCapabilityDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbModuleCapabilityDao());
    }

    @Override
    public List<DbModuleCapability> getAllActive(Long moduleId) {

        if (moduleId == null || moduleId < 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(Properties.ModuleId.eq(moduleId))
                .where(Properties.Active.eq(Boolean.TRUE))
                .build()
                .list();
    }

    @Override
    public List<DbModuleCapability> getAllActiveRequired(Long moduleId) {

        if (moduleId == null || moduleId < 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(Properties.ModuleId.eq(moduleId))
                .where(Properties.Active.eq(Boolean.TRUE))
                .where(Properties.Required.eq(Boolean.TRUE))
                .build()
                .list();
    }
}
