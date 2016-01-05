package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.RunningServiceSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningServicesSensorDaoImpl extends
        CommonEventDaoImpl<DbRunningServicesSensor> implements
        RunningServicesSensorDao {

    private static final String TAG = RunningServicesSensorDaoImpl.class.getSimpleName();

    private static RunningServicesSensorDao INSTANCE;

    private RunningServicesSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningServicesSensorDao());
    }

    public static RunningServicesSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningServicesSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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

    @Nullable
    @Override
    public DbRunningServicesSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRunningServicesSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRunningServicesSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRunningServicesSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
