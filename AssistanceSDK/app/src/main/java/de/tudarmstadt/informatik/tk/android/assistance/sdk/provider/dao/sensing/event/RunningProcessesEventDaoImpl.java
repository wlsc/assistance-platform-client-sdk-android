package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.RunningProcessSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesEventDaoImpl extends
        CommonEventDaoImpl<DbRunningProcessesSensor> implements
        RunningProcessesEventDao {

    private static final String TAG = RunningProcessesEventDaoImpl.class.getSimpleName();

    private static RunningProcessesEventDao INSTANCE;

    private RunningProcessesEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningProcessesSensorDao());
    }

    public static RunningProcessesEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningProcessesEventDaoImpl(mDaoSession);
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
