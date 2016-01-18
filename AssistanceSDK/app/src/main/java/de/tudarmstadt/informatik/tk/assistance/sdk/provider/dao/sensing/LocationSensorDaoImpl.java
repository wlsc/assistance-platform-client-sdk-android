package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.LocationSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class LocationSensorDaoImpl extends
        CommonEventDaoImpl<DbPositionSensor> implements
        LocationSensorDao {

    private static final String TAG = LocationSensorDaoImpl.class.getSimpleName();

    private static LocationSensorDao INSTANCE;

    private LocationSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPositionSensorDao());
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
    @Nullable
    @Override
    public LocationSensorDto convertObject(DbPositionSensor sensor) {

        if (sensor == null) {
            return null;
        }

        LocationSensorDto result = new LocationSensorDto();

        result.setLatitude(sensor.getLatitude());
        result.setLongitude(sensor.getLongitude());
        result.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
        result.setAccuracyVertical(sensor.getAccuracyVertical());
        result.setAltitude(sensor.getAltitude());
        result.setSpeed(sensor.getSpeed());
        result.setCreated(sensor.getCreated());

        return result;
    }
}