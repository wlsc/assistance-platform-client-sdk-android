package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.power;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerLevelEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.PowerLevelEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
public class PowerLevelEventDaoImpl extends
        CommonEventDaoImpl<DbPowerLevelEvent> implements
        PowerLevelEventDao {

    private static final String TAG = PowerLevelEventDaoImpl.class.getSimpleName();

    private static PowerLevelEventDao INSTANCE;

    private PowerLevelEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerLevelEventDao());
    }

    public static PowerLevelEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerLevelEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbPowerLevelEvent sensor) {

        if (sensor == null) {
            return null;
        }

        PowerLevelEventDto result = new PowerLevelEventDto();

        result.setPercent(sensor.getPercent());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbPowerLevelEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbPowerLevelEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbPowerLevelEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbPowerLevelEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}