package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.01.2016
 */
public interface CommonSocialEventDao<T> extends CommonEventDao<T> {

    @Nullable
    T getForUserId(Long userId);

    @Nullable
    T getIfChangedForUserId(Long userId);
}
