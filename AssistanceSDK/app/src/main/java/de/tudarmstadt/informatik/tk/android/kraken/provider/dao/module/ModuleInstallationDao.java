package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.module;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallation;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface ModuleInstallationDao {

    List<DbModuleInstallation> getModuleInstallationsByUserId(long userId);

    List<DbModuleInstallation> getModuleInstallationsByUserId(long userId, long moduleId);

    DbModuleInstallation getModuleInstallationForModuleByUserId(long userId, long moduleId);

    long insertModuleInstallation(DbModuleInstallation moduleInstallation);

    void updateModuleInstallation(DbModuleInstallation moduleInstallation);

    void removeInstalledModules(List<DbModuleInstallation> moduleInstallations);
}
