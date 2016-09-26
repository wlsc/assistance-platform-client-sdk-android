package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external;

import org.greenrobot.greendao.AbstractDao;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.01.2016
 */
public abstract class CommonSocialEventDaoImpl<T> extends
        CommonEventDaoImpl<T> implements
        CommonSocialEventDao<T> {

    public CommonSocialEventDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);
    }
}