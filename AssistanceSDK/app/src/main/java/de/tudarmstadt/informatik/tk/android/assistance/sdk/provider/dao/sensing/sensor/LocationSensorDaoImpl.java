package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensor.LocationSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class LocationSensorDaoImpl extends
        CommonEventDaoImpl implements
        LocationSensorDao {

    private static final String TAG = LocationSensorDaoImpl.class.getSimpleName();

    private static LocationSensorDao INSTANCE;

    private DbPositionSensorDao dao;

    private LocationSensorDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbPositionSensorDao();
        }
    }

    public static LocationSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new LocationSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Converts db object to request object
     *
     * @param sensor
     * @return
     */
    @Override
    public LocationSensorDto convertObject(DbPositionSensor sensor) {

        if (sensor == null) {
            return null;
        }

        LocationSensorDto result = new LocationSensorDto();

        result.setId(sensor.getId());
        result.setLatitude(sensor.getLatitude());
        result.setLongitude(sensor.getLongitude());
        result.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
        result.setAccuracyVertical(sensor.getAccuracyVertical());
        result.setAltitude(sensor.getAltitude());
        result.setSpeed(sensor.getSpeed());
        result.setType(DtoType.LOCATION);
        result.setTypeStr(DtoType.getApiName(DtoType.LOCATION));
        result.setCreated(sensor.getCreated());

        return result;
    }

    /**
     * Converts list of db objects to request objects
     *
     * @param dbSensors
     * @return
     */
    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbPositionSensor dbSensor : (List<DbPositionSensor>) dbSensors) {
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
            return Collections.emptyList();
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
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbPositionSensorDao.Properties.Id)
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

        long result = dao.insertOrReplace((DbPositionSensor) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbPositionSensor>) events);
    }
}
