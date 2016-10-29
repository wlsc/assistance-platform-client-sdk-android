package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.GyroscopeSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class GyroscopeSensorDaoImpl extends
        CommonEventDaoImpl<DbGyroscopeSensor> implements
        GyroscopeSensorDao {

    private static final String TAG = GyroscopeSensorDaoImpl.class.getSimpleName();

    @Inject
    public GyroscopeSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbGyroscopeSensorDao());
    }

    @Nullable
    @Override
    public GyroscopeSensorDto convertObject(DbGyroscopeSensor sensor) {

        if (sensor == null) {
            return null;
        }

        GyroscopeSensorDto result = new GyroscopeSensorDto();

        result.setX(sensor.getX());
        result.setY(sensor.getY());
        result.setZ(sensor.getZ());
        result.setAccuracy(sensor.getAccuracy());
        result.setxUncalibratedNoDrift(sensor.getXUncalibratedNoDrift());
        result.setyUncalibratedNoDrift(sensor.getYUncalibratedNoDrift());
        result.setzUncalibratedNoDrift(sensor.getZUncalibratedNoDrift());
        result.setxUncalibratedEstimatedDrift(sensor.getXUncalibratedEstimatedDrift());
        result.setyUncalibratedEstimatedDrift(sensor.getYUncalibratedEstimatedDrift());
        result.setzUncalibratedEstimatedDrift(sensor.getZUncalibratedEstimatedDrift());
        result.setCreated(sensor.getCreated());

        return result;
    }
}