package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.ForegroundSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ForegroundEventDaoImpl extends
        CommonEventDaoImpl<DbForegroundSensor> implements
        ForegroundEventDao {

    private static final String TAG = ForegroundEventDaoImpl.class.getSimpleName();

    private static ForegroundEventDao INSTANCE;

    private ForegroundEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbForegroundSensorDao());
    }

    public static ForegroundEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundEventDaoImpl(mDaoSession);
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
        result.setEventType(SensorApiType.getApiName(sensor.getEventType()));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbForegroundSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbForegroundSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbForegroundSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbForegroundSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}