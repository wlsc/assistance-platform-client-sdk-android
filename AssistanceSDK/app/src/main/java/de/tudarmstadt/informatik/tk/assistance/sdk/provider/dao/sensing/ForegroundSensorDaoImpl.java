package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.ForegroundSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public final class ForegroundSensorDaoImpl extends
        CommonEventDaoImpl<DbForegroundSensor> implements
        ForegroundSensorDao {

    private static final String TAG = ForegroundSensorDaoImpl.class.getSimpleName();

    private static ForegroundSensorDao INSTANCE;

    private ForegroundSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbForegroundSensorDao());
    }

    public static ForegroundSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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