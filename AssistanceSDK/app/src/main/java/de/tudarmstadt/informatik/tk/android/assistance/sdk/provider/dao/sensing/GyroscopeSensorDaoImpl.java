package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbGyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.GyroscopeSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class GyroscopeSensorDaoImpl extends
        CommonEventDaoImpl<DbGyroscopeSensor> implements
        GyroscopeSensorDao {

    private static final String TAG = GyroscopeSensorDaoImpl.class.getSimpleName();

    private static GyroscopeSensorDao INSTANCE;

    private GyroscopeSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbGyroscopeSensorDao());
    }

    public static GyroscopeSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new GyroscopeSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public GyroscopeSensorDto convertObject(DbGyroscopeSensor sensor) {

        if (sensor == null) {
            return null;
        }

        GyroscopeSensorDto result = new GyroscopeSensorDto();

        result.setX(sensor.getX());
        result.setY(sensor.getY());
        result.setZ(sensor.getZ());
        result.setAccuracy(sensor.getAccuracy());
        result.setxUncalibratedNoDrift(sensor.getXUncalibratedNoDrift());
        result.setyUncalibratedNoDrift(sensor.getYUncalibratedNoDrift());
        result.setzUncalibratedNoDrift(sensor.getZUncalibratedNoDrift());
        result.setxUncalibratedEstimatedDrift(sensor.getXUncalibratedEstimatedDrift());
        result.setyUncalibratedEstimatedDrift(sensor.getYUncalibratedEstimatedDrift());
        result.setzUncalibratedEstimatedDrift(sensor.getZUncalibratedEstimatedDrift());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbGyroscopeSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbGyroscopeSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbGyroscopeSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbGyroscopeSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}