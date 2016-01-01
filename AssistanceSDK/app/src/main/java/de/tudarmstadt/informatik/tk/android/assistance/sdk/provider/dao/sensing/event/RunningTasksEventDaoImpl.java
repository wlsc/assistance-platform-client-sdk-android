package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.RunningTaskEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningTasksEventDaoImpl extends
        CommonEventDaoImpl<DbRunningTasksEvent> implements
        RunningTasksEventDao {

    private static final String TAG = RunningTasksEventDaoImpl.class.getSimpleName();

    private static RunningTasksEventDao INSTANCE;

    private RunningTasksEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningTasksEventDao());
    }

    public static RunningTasksEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningTasksEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbRunningTasksEvent sensor) {

        if (sensor == null) {
            return null;
        }

        RunningTaskEventDto result = new RunningTaskEventDto();

        result.setName(sensor.getName());
        result.setStackPosition(sensor.getStackPosition());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbRunningTasksEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRunningTasksEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRunningTasksEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRunningTasksEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
