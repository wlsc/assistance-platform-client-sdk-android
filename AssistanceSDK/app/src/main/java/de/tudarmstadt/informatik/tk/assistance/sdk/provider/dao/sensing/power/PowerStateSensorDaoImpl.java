package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.PowerStateSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateSensorDaoImpl extends
        CommonEventDaoImpl<DbPowerStateSensor> implements
        PowerStateSensorDao {

    private static final String TAG = PowerStateSensorDaoImpl.class.getSimpleName();

    private static PowerStateSensorDao INSTANCE;

    private PowerStateSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerStateSensorDao());
    }

    public static PowerStateSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerStateSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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

    @Nullable
    @Override
    public DbPowerStateSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbPowerStateSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbPowerStateSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbPowerStateSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
