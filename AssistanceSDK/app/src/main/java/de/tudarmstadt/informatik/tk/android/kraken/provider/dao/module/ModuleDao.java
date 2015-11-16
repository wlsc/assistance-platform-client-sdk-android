package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbModule;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public interface ModuleDao {

    DbModule getModuleByPackageIdUserId(String modulePackageName, Long userId);

    List<DbModule> getAllModules(Long userId);

    List<DbModule> getAllActiveModules(Long userId);

    List<DbModule> getAllModules();

    long insertModule(DbModule module);

    void delete(List<DbModule> dbItems);
}
