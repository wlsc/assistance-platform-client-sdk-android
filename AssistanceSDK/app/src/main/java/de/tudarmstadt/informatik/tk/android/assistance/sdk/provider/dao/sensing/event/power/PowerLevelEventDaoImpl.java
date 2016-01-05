package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.power;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.PowerLevelSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
public class PowerLevelEventDaoImpl extends
        CommonEventDaoImpl<DbPowerLevelSensor> implements
        PowerLevelEventDao {

    private static final String TAG = PowerLevelEventDaoImpl.class.getSimpleName();

    private static PowerLevelEventDao INSTANCE;

    private PowerLevelEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerLevelSensorDao());
    }

    public static PowerLevelEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerLevelEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
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

    @Nullable
    @Override
    public DbPowerLevelSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbPowerLevelSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbPowerLevelSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbPowerLevelSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}