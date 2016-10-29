package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.PowerStateSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
@Singleton
public final class PowerStateSensorDaoImpl extends
        CommonEventDaoImpl<DbPowerStateSensor> implements
        PowerStateSensorDao {

    private static final String TAG = PowerStateSensorDaoImpl.class.getSimpleName();

    @Inject
    public PowerStateSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerStateSensorDao());
    }

    @Nullable
    @Override
    public PowerStateSensorDto convertObject(DbPowerStateSensor sensor) {

        if (sensor == null) {
            return null;
        }

        PowerStateSensorDto result = new PowerStateSensorDto(
                sensor.getIsCharging(),
                sensor.getPercent(),
                sensor.getChargingState(),
                sensor.getChargingMode(),
                sensor.getPowerSaveMode(),
                sensor.getCreated()
        );

        return result;
    }
}