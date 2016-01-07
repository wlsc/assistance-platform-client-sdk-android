package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.RunningProcessSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesSensorDaoImpl extends
        CommonEventDaoImpl<DbRunningProcessesSensor> implements
        RunningProcessesSensorDao {

    private static final String TAG = RunningProcessesSensorDaoImpl.class.getSimpleName();

    private static RunningProcessesSensorDao INSTANCE;

    private RunningProcessesSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningProcessesSensorDao());
    }

    public static RunningProcessesSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningProcessesSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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

    @Nullable
    @Override
    public DbRunningProcessesSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRunningProcessesSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRunningProcessesSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRunningProcessesSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
