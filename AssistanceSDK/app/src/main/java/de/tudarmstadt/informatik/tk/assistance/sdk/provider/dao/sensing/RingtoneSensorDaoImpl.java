package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.RingtoneSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class RingtoneSensorDaoImpl extends
        CommonEventDaoImpl<DbRingtoneSensor> implements
        RingtoneSensorDao {

    private static final String TAG = RingtoneSensorDaoImpl.class.getSimpleName();

    @Inject
    public RingtoneSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRingtoneSensorDao());
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
}