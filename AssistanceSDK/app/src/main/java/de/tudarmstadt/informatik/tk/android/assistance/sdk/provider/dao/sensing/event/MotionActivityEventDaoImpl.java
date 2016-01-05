package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.MotionActivityEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MotionActivityEventDaoImpl extends
        CommonEventDaoImpl<DbMotionActivityEvent> implements
        MotionActivityEventDao {

    private static final String TAG = MotionActivityEventDaoImpl.class.getSimpleName();

    private static MotionActivityEventDao INSTANCE;

    private MotionActivityEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMotionActivityEventDao());
    }

    public static MotionActivityEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MotionActivityEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public MotionActivityEventDto convertObject(DbMotionActivityEvent sensor) {

        if (sensor == null) {
            return null;
        }

        MotionActivityEventDto result = new MotionActivityEventDto();

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
    public DbMotionActivityEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbMotionActivityEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbMotionActivityEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMotionActivityEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
