package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.power;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.PowerStateSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDaoImpl extends
        CommonEventDaoImpl<DbPowerStateSensor> implements
        PowerStateEventDao {

    private static final String TAG = PowerStateEventDaoImpl.class.getSimpleName();

    private static PowerStateEventDao INSTANCE;

    private PowerStateEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerStateSensorDao());
    }

    public static PowerStateEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerStateEventDaoImpl(mDaoSession);
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
