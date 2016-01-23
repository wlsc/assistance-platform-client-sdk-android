package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUpload;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.UploadLogsDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class SensorUploadLogsDaoImpl extends
        UploadLogsDaoImpl<LogsSensorUpload> implements
        SensorUploadLogsDao {

    private static final String TAG = SensorUploadLogsDaoImpl.class.getSimpleName();

    private static SensorUploadLogsDao INSTANCE;

    private SensorUploadLogsDaoImpl(DaoSession daoSession) {
        super(daoSession.getLogsSensorUploadDao());
    }

    public static SensorUploadLogsDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new SensorUploadLogsDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }
}