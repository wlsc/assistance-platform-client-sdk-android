package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleCapabilityDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ModuleCapabilityDaoImpl implements ModuleCapabilityDao {

    private static final String TAG = ModuleCapabilityDaoImpl.class.getSimpleName();

    private static ModuleCapabilityDao INSTANCE;

    private DbModuleCapabilityDao moduleCapabilityDao;

    private ModuleCapabilityDaoImpl(DaoSession daoSession) {

        if (moduleCapabilityDao == null) {
            moduleCapabilityDao = daoSession.getDbModuleCapabilityDao();
        }
    }

    public static ModuleCapabilityDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleCapabilityDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Inserts new module capability
     *
     * @param moduleCapability
     * @return
     */
    @Override
    public long insertModuleCapability(DbModuleCapability moduleCapability) {

        if (moduleCapability == null) {
            return -1L;
        }

        return moduleCapabilityDao.insertOrReplace(moduleCapability);
    }

    /**
     * Inserts new module capabilities
     *
     * @param moduleCapabilities
     * @return
     */
    @Override
    public void insertModuleCapabilities(List<DbModuleCapability> moduleCapabilities) {

        if (moduleCapabilities == null) {
            return;
        }

        moduleCapabilityDao.insertInTx(moduleCapabilities);
    }
}
