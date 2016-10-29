package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerLevelSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.PowerLevelSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
@Singleton
public final class PowerLevelSensorDaoImpl extends
        CommonEventDaoImpl<DbPowerLevelSensor> implements
        PowerLevelSensorDao {

    private static final String TAG = PowerLevelSensorDaoImpl.class.getSimpleName();

    @Inject
    public PowerLevelSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerLevelSensorDao());
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbPowerLevelSensor sensor) {

        if (sensor == null) {
            return null;
        }

        PowerLevelSensorDto result = new PowerLevelSensorDto();

        result.setPercent(sensor.getPercent());
        result.setCreated(sensor.getCreated());

        return result;
    }
}