package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface ModuleCapabilityDao extends CommonDao<DbModuleCapability> {

    List<DbModuleCapability> getAllActive(Long moduleId);

}
