package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.ForegroundSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class ForegroundSensorDaoImpl extends
        CommonEventDaoImpl<DbForegroundSensor> implements
        ForegroundSensorDao {

    private static final String TAG = ForegroundSensorDaoImpl.class.getSimpleName();

    @Inject
    public ForegroundSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbForegroundSensorDao());
    }

    @Nullable
    @Override
    public ForegroundSensorDto convertObject(DbForegroundSensor sensor) {

        if (sensor == null) {
            return null;
        }

        ForegroundSensorDto result = new ForegroundSensorDto();

        result.setAppName(sensor.getAppName());
        result.setActivityLabel(sensor.getActivityLabel());
        result.setClassName(sensor.getClassName());
        result.setColor(sensor.getColor());
        result.setKeystrokes(sensor.getKeystrokes());
        result.setPackageName(sensor.getPackageName());
        result.setUrl(sensor.getUrl());
        result.setEventType(SensorApiType.getApiName(sensor.getEventType() == null ? -1 : sensor.getEventType()));
        result.setCreated(sensor.getCreated());

        return result;
    }
}