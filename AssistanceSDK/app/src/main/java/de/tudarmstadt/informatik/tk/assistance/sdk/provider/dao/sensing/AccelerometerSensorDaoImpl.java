package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.AccelerometerSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class AccelerometerSensorDaoImpl extends
        CommonEventDaoImpl<DbAccelerometerSensor> implements
        AccelerometerSensorDao {

    private static final String TAG = AccelerometerSensorDaoImpl.class.getSimpleName();

    private static AccelerometerSensorDao INSTANCE;

    private AccelerometerSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbAccelerometerSensorDao());
    }

    public static AccelerometerSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new AccelerometerSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Converts db object to request object
     *
     * @param sensor
     * @return
     */
    @Nullable
    @Override
    public AccelerometerSensorDto convertObject(DbAccelerometerSensor sensor) {

        if (sensor == null) {
            return null;
        }

        AccelerometerSensorDto result = new AccelerometerSensorDto();

        result.setX(sensor.getX());
        result.setY(sensor.getY());
        result.setZ(sensor.getZ());
        result.setAccuracy(sensor.getAccuracy());
        result.setCreated(sensor.getCreated());

        return result;
    }
}