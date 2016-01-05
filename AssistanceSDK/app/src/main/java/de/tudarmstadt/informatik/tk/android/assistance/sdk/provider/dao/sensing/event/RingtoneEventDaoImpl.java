package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRingtoneSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.RingtoneSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RingtoneEventDaoImpl extends
        CommonEventDaoImpl<DbRingtoneSensor> implements
        RingtoneEventDao {

    private static final String TAG = RingtoneEventDaoImpl.class.getSimpleName();

    private static RingtoneEventDao INSTANCE;

    private RingtoneEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRingtoneSensorDao());
    }

    public static RingtoneEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RingtoneEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbRingtoneSensor sensor) {

        if (sensor == null) {
            return null;
        }

        RingtoneSensorDto result = new RingtoneSensorDto();

        result.setMode(sensor.getMode());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbRingtoneSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRingtoneSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRingtoneSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRingtoneSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
