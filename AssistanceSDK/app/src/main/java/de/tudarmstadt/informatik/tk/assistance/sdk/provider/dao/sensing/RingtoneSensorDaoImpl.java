package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.RingtoneSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public final class RingtoneSensorDaoImpl extends
        CommonEventDaoImpl<DbRingtoneSensor> implements
        RingtoneSensorDao {

    private static final String TAG = RingtoneSensorDaoImpl.class.getSimpleName();

    private static RingtoneSensorDao INSTANCE;

    private RingtoneSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbRingtoneSensorDao());
    }

    public static RingtoneSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new RingtoneSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
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