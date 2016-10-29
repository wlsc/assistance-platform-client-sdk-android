package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.RunningProcessSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class RunningProcessesSensorDaoImpl extends
        CommonEventDaoImpl<DbRunningProcessesSensor> implements
        RunningProcessesSensorDao {

    private static final String TAG = RunningProcessesSensorDaoImpl.class.getSimpleName();

    @Inject
    public RunningProcessesSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningProcessesSensorDao());
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbRunningProcessesSensor sensor) {

        if (sensor == null) {
            return null;
        }

        RunningProcessSensorDto result = new RunningProcessSensorDto();

        result.setName(sensor.getName());
        result.setCreated(sensor.getCreated());

        return result;
    }
}