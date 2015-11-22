package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ModuleCapabilityDaoImpl extends
        CommonDaoImpl<DbModuleCapability> implements
        ModuleCapabilityDao {

    private static final String TAG = ModuleCapabilityDaoImpl.class.getSimpleName();

    private static ModuleCapabilityDao INSTANCE;

    private ModuleCapabilityDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbModuleCapabilityDao());
    }

    public static ModuleCapabilityDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleCapabilityDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public List<DbModuleCapability> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbModuleCapabilityDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
