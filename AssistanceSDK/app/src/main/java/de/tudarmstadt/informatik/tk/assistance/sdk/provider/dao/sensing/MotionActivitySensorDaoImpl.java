package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.MotionActivitySensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class MotionActivitySensorDaoImpl extends
        CommonEventDaoImpl<DbMotionActivitySensor> implements
        MotionActivitySensorDao {

    private static final String TAG = MotionActivitySensorDaoImpl.class.getSimpleName();

    @Inject
    public MotionActivitySensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMotionActivitySensorDao());
    }

    @Nullable
    @Override
    public MotionActivitySensorDto convertObject(DbMotionActivitySensor sensor) {

        if (sensor == null) {
            return null;
        }

        MotionActivitySensorDto result = new MotionActivitySensorDto();

        result.setRunning(sensor.getRunning());
        result.setStationary(sensor.getStationary());
        result.setCycling(sensor.getCycling());
        result.setWalking(sensor.getWalking());
        result.setDriving(sensor.getDriving());
        result.setOnFoot(sensor.getOnFoot());
        result.setTilting(sensor.getTilting());
        result.setUnknown(sensor.getUnknown());
        result.setCreated(sensor.getCreated());

        return result;
    }
}