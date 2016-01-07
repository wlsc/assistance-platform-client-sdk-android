package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.CallLogSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CallLogSensorDaoImpl extends
        CommonEventDaoImpl<DbCallLogSensor> implements
        CallLogSensorDao {

    private static final String TAG = CallLogSensorDaoImpl.class.getSimpleName();

    private static CallLogSensorDao INSTANCE;

    private CallLogSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCallLogSensorDao());
    }

    public static CallLogSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CallLogSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public CallLogSensorDto convertObject(DbCallLogSensor sensor) {

        if (sensor == null) {
            return null;
        }

        CallLogSensorDto result = new CallLogSensorDto();

        result.setId(sensor.getId());
        result.setCallId(sensor.getCallId());
        result.setCallType(sensor.getType());
        result.setName(sensor.getName());
        result.setNumber(sensor.getNumber());
        result.setDate(sensor.getDate());
        result.setDuration(sensor.getDuration());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(sensor.getIsUpdated());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbCallLogSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbCallLogSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbCallLogSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCallLogSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    /**
     * Returns last element from table
     *
     * @return
     */
    @Override
    public DbCallLogSensor getLastCallLogEvent() {
        return dao
                .queryBuilder()
                .orderDesc(DbCallLogSensorDao.Properties.Id)
                .limit(1)
                .build()
                .unique();
    }
}
