package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.LocationSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class LocationSensorDaoImpl extends
        CommonEventDaoImpl<DbPositionSensor> implements
        LocationSensorDao {

    private static final String TAG = LocationSensorDaoImpl.class.getSimpleName();

    @Inject
    public LocationSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPositionSensorDao());
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