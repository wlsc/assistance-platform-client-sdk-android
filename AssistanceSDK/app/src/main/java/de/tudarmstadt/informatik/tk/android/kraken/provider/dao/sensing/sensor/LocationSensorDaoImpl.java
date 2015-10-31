package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.LocationSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class LocationSensorDaoImpl extends
        CommonEventDaoImpl implements
        LocationSensorDao {

    private static final String TAG = LocationSensorDaoImpl.class.getSimpleName();

    private static LocationSensorDao INSTANCE;

    private DbPositionSensorDao locationSensorDao;

    private LocationSensorDaoImpl(DaoSession daoSession) {

        if (locationSensorDao == null) {
            locationSensorDao = daoSession.getDbPositionSensorDao();
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
        result.setType(DtoType.LOCATION);
        result.setTypeStr(DtoType.getApiName(DtoType.LOCATION));
        result.setLatitude(sensor.getLatitude());
        result.setLongitude(sensor.getLongitude());
        result.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
        result.setAccuracyVertical(sensor.getAccuracyVertical());
        result.setAltitude(sensor.getAltitude());
        result.setSpeed(sensor.getSpeed());
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
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new LinkedList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbPositionSensor dbSensor : (List<DbPositionSensor>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return locationSensorDao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return locationSensorDao
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

        return locationSensorDao
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

        Log.d(LocationSensor.class.getSimpleName(), "Dumping data to db...");

        long result = locationSensorDao.insertOrReplace((DbPositionSensor) sensor);

        Log.d(LocationSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        locationSensorDao.deleteInTx((Iterable<DbPositionSensor>) events);
    }
}
