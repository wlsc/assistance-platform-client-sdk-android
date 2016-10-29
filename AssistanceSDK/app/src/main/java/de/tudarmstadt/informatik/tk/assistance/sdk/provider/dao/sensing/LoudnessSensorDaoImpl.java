package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLoudnessSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.LoudnessSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class LoudnessSensorDaoImpl extends
        CommonEventDaoImpl<DbLoudnessSensor> implements
        LoudnessSensorDao {

    private static final String TAG = LoudnessSensorDaoImpl.class.getSimpleName();

    @Inject
    public LoudnessSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbLoudnessSensorDao());
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
}