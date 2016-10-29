package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningTasksSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.RunningTaskSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class RunningTasksSensorDaoImpl extends
        CommonEventDaoImpl<DbRunningTasksSensor> implements
        RunningTasksSensorDao {

    private static final String TAG = RunningTasksSensorDaoImpl.class.getSimpleName();

    @Inject
    public RunningTasksSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningTasksSensorDao());
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbRunningTasksSensor sensor) {

        if (sensor == null) {
            return null;
        }

        RunningTaskSensorDto result = new RunningTaskSensorDto();

        result.setName(sensor.getName());
        result.setStackPosition(sensor.getStackPosition());
        result.setCreated(sensor.getCreated());

        return result;
    }
}