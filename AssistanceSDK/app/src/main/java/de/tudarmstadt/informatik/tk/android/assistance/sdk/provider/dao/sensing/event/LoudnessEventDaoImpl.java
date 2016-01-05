package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.LoudnessEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class LoudnessEventDaoImpl extends
        CommonEventDaoImpl<DbLoudnessEvent> implements
        LoudnessEventDao {

    private static final String TAG = LoudnessEventDaoImpl.class.getSimpleName();

    private static LoudnessEventDao INSTANCE;

    private LoudnessEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbLoudnessEventDao());
    }

    public static LoudnessEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new LoudnessEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbLoudnessEvent sensor) {

        if (sensor == null) {
            return null;
        }

        LoudnessEventDto result = new LoudnessEventDto();

        result.setLoudness(sensor.getLoudness());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbLoudnessEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbLoudnessEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbLoudnessEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbLoudnessEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
