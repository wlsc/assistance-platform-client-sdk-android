package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbModule;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public interface ModuleDao {

    DbModule getModuleByPackageIdUserId(String modulePackageName, Long userId);

    long insertModule(DbModule module);
}