package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUpload;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.UploadLogsDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
@Singleton
public final class SensorUploadLogsDaoImpl extends
        UploadLogsDaoImpl<LogsSensorUpload> implements
        SensorUploadLogsDao {

    private static final String TAG = SensorUploadLogsDaoImpl.class.getSimpleName();

    @Inject
    public SensorUploadLogsDaoImpl(DaoSession daoSession) {
        super(daoSession.getLogsSensorUploadDao());
    }
}