package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.RunningTaskEventDto;
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

        result.setId(sensor.getId());
        result.setName(sensor.getName());
        result.setStackPosition(sensor.getStackPosition());
        result.setType(DtoType.RUNNING_TASKS);
        result.setTypeStr(DtoType.getApiName(DtoType.RUNNING_TASKS));
        result.setCreated(sensor.getCreated());

        return result;
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
