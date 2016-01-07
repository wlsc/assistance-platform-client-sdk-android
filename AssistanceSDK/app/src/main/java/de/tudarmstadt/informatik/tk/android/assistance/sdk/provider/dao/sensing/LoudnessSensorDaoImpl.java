package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.LoudnessSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class LoudnessSensorDaoImpl extends
        CommonEventDaoImpl<DbLoudnessSensor> implements
        LoudnessSensorDao {

    private static final String TAG = LoudnessSensorDaoImpl.class.getSimpleName();

    private static LoudnessSensorDao INSTANCE;

    private LoudnessSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbLoudnessSensorDao());
    }

    public static LoudnessSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new LoudnessSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbLoudnessSensor sensor) {

        if (sensor == null) {
            return null;
        }

        LoudnessSensorDto result = new LoudnessSensorDto();

        result.setLoudness(sensor.getLoudness());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbLoudnessSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbLoudnessSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbLoudnessSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbLoudnessSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
