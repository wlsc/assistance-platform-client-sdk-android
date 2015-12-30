package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.RunningProcessEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesEventDaoImpl extends
        CommonEventDaoImpl<DbRunningProcessesEvent> implements
        RunningProcessesEventDao {

    private static final String TAG = RunningProcessesEventDaoImpl.class.getSimpleName();

    private static RunningProcessesEventDao INSTANCE;

    private RunningProcessesEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningProcessesEventDao());
    }

    public static RunningProcessesEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningProcessesEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbRunningProcessesEvent sensor) {

        if (sensor == null) {
            return null;
        }

        RunningProcessEventDto result = new RunningProcessEventDto();

        result.setName(sensor.getName());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbRunningProcessesEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRunningProcessesEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRunningProcessesEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRunningProcessesEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
