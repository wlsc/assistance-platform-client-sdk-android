package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModuleAllowedCapabilities;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 09.01.2016
 */
public interface ModuleAllowedCapsDao extends CommonDao<DbModuleAllowedCapabilities> {

    DbModuleAllowedCapabilities get(String type);
}
