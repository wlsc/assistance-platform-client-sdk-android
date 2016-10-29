package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.RunningServiceSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class RunningServicesSensorDaoImpl extends
        CommonEventDaoImpl<DbRunningServicesSensor> implements
        RunningServicesSensorDao {

    private static final String TAG = RunningServicesSensorDaoImpl.class.getSimpleName();

    @Inject
    public RunningServicesSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningServicesSensorDao());
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbRunningServicesSensor sensor) {

        if (sensor == null) {
            return null;
        }

        RunningServiceSensorDto result = new RunningServiceSensorDto();

        result.setPackageName(sensor.getPackageName());
        result.setClassName(sensor.getClassName());
        result.setCreated(sensor.getCreated());

        return result;
    }
}