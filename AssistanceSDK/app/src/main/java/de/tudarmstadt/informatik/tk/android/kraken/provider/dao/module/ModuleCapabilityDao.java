package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleCapability;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface ModuleCapabilityDao {
    long insertModuleCapability(DbModuleCapability moduleCapability);

    void insertModuleCapabilities(List<DbModuleCapability> moduleCapabilities);
}
