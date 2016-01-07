package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.AccelerometerSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

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

    @Nullable
    @Override
    public DbAccelerometerSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbAccelerometerSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbAccelerometerSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbAccelerometerSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
