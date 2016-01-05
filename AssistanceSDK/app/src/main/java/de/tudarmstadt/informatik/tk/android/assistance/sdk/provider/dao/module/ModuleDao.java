package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module;

import android.support.annotation.Nullable;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public interface ModuleDao extends CommonDao<DbModule> {

    @Nullable
    DbModule getByPackageIdUserId(String modulePackageName, Long userId);

    List<DbModule> getAll(Long userId);

    List<DbModule> getAllActive(Long userId);
}
