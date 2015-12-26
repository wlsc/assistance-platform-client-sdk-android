package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.sensor.AccelerometerSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

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
     * @param dbSensor
     * @return
     */
    @Override
    public AccelerometerSensorDto convertObject(DbAccelerometerSensor dbSensor) {

        if (dbSensor == null) {
            return null;
        }

        AccelerometerSensorDto result = new AccelerometerSensorDto();

        result.setId(dbSensor.getId());
        result.setX(dbSensor.getX());
        result.setY(dbSensor.getY());
        result.setZ(dbSensor.getZ());
        result.setAccuracy(dbSensor.getAccuracy());
        result.setType(DtoType.ACCELEROMETER);
        result.setTypeStr(DtoType.getApiName(DtoType.ACCELEROMETER));
        result.setCreated(dbSensor.getCreated());

        return result;
    }

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
