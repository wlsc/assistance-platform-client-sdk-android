package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.uni.TucanSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.01.2016
 */
public class TucanSensorDaoImpl extends
        CommonEventDaoImpl<DbTucanSensor> implements
        TucanSensorDao {

    private static final String TAG = TucanSensorDaoImpl.class.getSimpleName();

    private static TucanSensorDao INSTANCE;

    private TucanSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbTucanSensorDao());
    }

    public static TucanSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new TucanSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbTucanSensor sensor) {

        if (sensor == null) {
            return null;
        }

        TucanSensorDto result = new TucanSensorDto(
                sensor.getUsername(),
                sensor.getPassword(),
                sensor.getCreated());

        return result;
    }
}