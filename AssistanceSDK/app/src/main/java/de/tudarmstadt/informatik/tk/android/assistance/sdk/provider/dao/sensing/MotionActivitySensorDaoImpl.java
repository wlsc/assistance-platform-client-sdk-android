package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivitySensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.MotionActivitySensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MotionActivitySensorDaoImpl extends
        CommonEventDaoImpl<DbMotionActivitySensor> implements
        MotionActivitySensorDao {

    private static final String TAG = MotionActivitySensorDaoImpl.class.getSimpleName();

    private static MotionActivitySensorDao INSTANCE;

    private MotionActivitySensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMotionActivitySensorDao());
    }

    public static MotionActivitySensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MotionActivitySensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public MotionActivitySensorDto convertObject(DbMotionActivitySensor sensor) {

        if (sensor == null) {
            return null;
        }

        MotionActivitySensorDto result = new MotionActivitySensorDto();

        result.setRunning(sensor.getRunning());
        result.setStationary(sensor.getStationary());
        result.setCycling(sensor.getCycling());
        result.setWalking(sensor.getWalking());
        result.setDriving(sensor.getDriving());
        result.setOnFoot(sensor.getOnFoot());
        result.setTilting(sensor.getTilting());
        result.setUnknown(sensor.getUnknown());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbMotionActivitySensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbMotionActivitySensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbMotionActivitySensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMotionActivitySensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
