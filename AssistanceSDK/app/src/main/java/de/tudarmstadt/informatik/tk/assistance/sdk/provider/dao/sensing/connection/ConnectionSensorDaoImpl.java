package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.ConnectionSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class ConnectionSensorDaoImpl extends
        CommonEventDaoImpl<DbConnectionSensor> implements
        ConnectionSensorDao {

    private static final String TAG = ConnectionSensorDaoImpl.class.getSimpleName();

    @Inject
    public ConnectionSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbConnectionSensorDao());
    }

    @Nullable
    @Override
    public ConnectionSensorDto convertObject(DbConnectionSensor sensor) {

        if (sensor == null) {
            return null;
        }

        ConnectionSensorDto result = new ConnectionSensorDto();

        result.setIsMobile(sensor.getIsMobile());
        result.setIsWifi(sensor.getIsWifi());
        result.setCreated(sensor.getCreated());

        return result;
    }
}