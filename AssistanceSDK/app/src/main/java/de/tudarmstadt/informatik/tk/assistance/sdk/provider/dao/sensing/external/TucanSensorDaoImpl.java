package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensorDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.external.TucanSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.01.2016
 */
public final class TucanSensorDaoImpl extends
        CommonSocialEventDaoImpl<DbTucanSensor> implements
        TucanSensorDao {

    private static final String TAG = TucanSensorDaoImpl.class.getSimpleName();

    private static TucanSensorDao INSTANCE;

    private TucanSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbTucanSensorDao());
    }

    public static TucanSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new TucanSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbTucanSensor sensor) {

        if (sensor == null) {
            return null;
        }

        TucanSensorDto result = new TucanSensorDto(
                sensor.getUsername(),
                sensor.getPassword(),
                sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbTucanSensor getForUserId(Long userId) {

        if (userId == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }

    @Nullable
    @Override
    public DbTucanSensor getIfChangedForUserId(Long userId) {

        if (userId == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.UserId.eq(userId))
                .where(Properties.WasChanged.eq(Boolean.TRUE))
                .limit(1)
                .build()
                .unique();
    }
}