package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload;

import de.greenrobot.dao.AbstractDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.LogsDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class UploadLogsDaoImpl<T> extends
        LogsDaoImpl<T> implements
        UploadLogsDao<T> {

    public UploadLogsDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);
    }
}