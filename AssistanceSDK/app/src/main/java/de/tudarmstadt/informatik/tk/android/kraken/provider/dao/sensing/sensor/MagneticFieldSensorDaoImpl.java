package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.MagneticFieldSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MagneticFieldSensorDaoImpl extends
        CommonEventDaoImpl implements
        MagneticFieldSensorDao {

    private static final String TAG = MagneticFieldSensorDaoImpl.class.getSimpleName();

    private static MagneticFieldSensorDao INSTANCE;

    private DbMagneticFieldSensorDao dao;

    private MagneticFieldSensorDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbMagneticFieldSensorDao();
        }
    }

    public static MagneticFieldSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MagneticFieldSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MagneticFieldSensorDto convertObject(DbMagneticFieldSensor sensor) {

        if (sensor == null) {
            return null;
        }

        MagneticFieldSensorDto result = new MagneticFieldSensorDto();

        result.setId(sensor.getId());
        result.setX(sensor.getX());
        result.setY(sensor.getY());
        result.setZ(sensor.getZ());
        result.setAccuracy(sensor.getAccuracy());
        result.setxUncalibratedNoHardIron(sensor.getXUncalibratedNoHardIron());
        result.setyUncalibratedNoHardIron(sensor.getYUncalibratedNoHardIron());
        result.setzUncalibratedNoHardIron(sensor.getZUncalibratedNoHardIron());
        result.setxUncalibratedEstimatedIronBias(sensor.getXUncalibratedEstimatedIronBias());
        result.setyUncalibratedEstimatedIronBias(sensor.getYUncalibratedEstimatedIronBias());
        result.setzUncalibratedEstimatedIronBias(sensor.getZUncalibratedEstimatedIronBias());
        result.setType(DtoType.MAGNETIC_FIELD);
        result.setTypeStr(DtoType.getApiName(DtoType.MAGNETIC_FIELD));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbMagneticFieldSensor dbSensor : (List<DbMagneticFieldSensor>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;

    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
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

        return dao
                .queryBuilder()
                .orderDesc(DbMagneticFieldSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping data to db...");

        long result = dao.insertOrReplace((DbMagneticFieldSensor) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbMagneticFieldSensor>) events);
    }
}
