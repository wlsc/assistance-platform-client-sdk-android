package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.ConnectionSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ConnectionSensorDaoImpl extends
        CommonEventDaoImpl<DbConnectionSensor> implements
        ConnectionSensorDao {

    private static final String TAG = ConnectionSensorDaoImpl.class.getSimpleName();

    private static ConnectionSensorDao INSTANCE;

    private ConnectionSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbConnectionSensorDao());
    }

    public static ConnectionSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ConnectionSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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