package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.AccelerometerSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class AccelerometerSensorDaoImpl extends
        CommonEventDaoImpl implements
        AccelerometerSensorDao {

    private static final String TAG = AccelerometerSensorDaoImpl.class.getSimpleName();

    private static AccelerometerSensorDao INSTANCE;

    private DbAccelerometerSensorDao accelerometerSensorDao;

    private AccelerometerSensorDaoImpl(DaoSession daoSession) {

        if (accelerometerSensorDao == null) {
            accelerometerSensorDao = daoSession.getDbAccelerometerSensorDao();
        }
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
        result.setType(DtoType.ACCELEROMETER);
        result.setTypeStr(DtoType.getApiName(DtoType.ACCELEROMETER));
        result.setX(dbSensor.getX());
        result.setY(dbSensor.getY());
        result.setZ(dbSensor.getZ());
        result.setAccuracy(dbSensor.getAccuracy());
        result.setCreated(dbSensor.getCreated());

        return result;
    }

    /**
     * Converts list of db objects to request objects
     *
     * @param dbSensors
     * @return
     */
    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new LinkedList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbAccelerometerSensor dbSensor : (List<DbAccelerometerSensor>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return accelerometerSensorDao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return accelerometerSensorDao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return accelerometerSensorDao
                .queryBuilder()
                .orderDesc(DbAccelerometerSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    /**
     * @param sensor
     * @return
     */
    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping data to db...");

        long result = accelerometerSensorDao.insertOrReplace((DbAccelerometerSensor) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        accelerometerSensorDao.deleteInTx((Iterable<DbAccelerometerSensor>) events);
    }
}
