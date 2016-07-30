package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs;

import org.greenrobot.greendao.AbstractDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class LogsDaoImpl<T> extends
        CommonDaoImpl<T> implements
        LogsDao<T> {

    public LogsDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);
    }
}