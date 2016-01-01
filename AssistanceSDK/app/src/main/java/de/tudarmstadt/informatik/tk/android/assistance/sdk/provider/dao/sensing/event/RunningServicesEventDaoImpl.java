package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.RunningServiceEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningServicesEventDaoImpl extends
        CommonEventDaoImpl<DbRunningServicesEvent> implements
        RunningServicesEventDao {

    private static final String TAG = RunningServicesEventDaoImpl.class.getSimpleName();

    private static RunningServicesEventDao INSTANCE;

    private RunningServicesEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRunningServicesEventDao());
    }

    public static RunningServicesEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RunningServicesEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbRunningServicesEvent sensor) {

        if (sensor == null) {
            return null;
        }

        RunningServiceEventDto result = new RunningServiceEventDto();

        result.setPackageName(sensor.getPackageName());
        result.setClassName(sensor.getClassName());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbRunningServicesEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbRunningServicesEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbRunningServicesEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbRunningServicesEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
