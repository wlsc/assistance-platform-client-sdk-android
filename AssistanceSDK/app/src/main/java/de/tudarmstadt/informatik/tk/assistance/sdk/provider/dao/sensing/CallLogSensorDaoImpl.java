package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.Property;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensorDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.CallLogSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class CallLogSensorDaoImpl extends
        CommonEventDaoImpl<DbCallLogSensor> implements
        CallLogSensorDao {

    private static final String TAG = CallLogSensorDaoImpl.class.getSimpleName();

    @Inject
    public CallLogSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCallLogSensorDao());
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

    /**
     * Returns last element from table
     *
     * @return
     */
    @Override
    public DbCallLogSensor getLastCallLogEvent() {
        return dao
                .queryBuilder()
                .orderDesc(Properties.Id)
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbCallLogSensor> getAllUpdated(long deviceId) {

        if (deviceId < 0) {
            return Collections.emptyList();
        }

        for (Property property : properties) {
            if (property.name.equals(DEVICE_ID_FIELD_NAME)) {
                return dao
                        .queryBuilder()
                        .where(Properties.IsUpdated.eq(Boolean.TRUE))
                        .where(property.eq(deviceId))
                        .build()
                        .list();
            }
        }

        return Collections.emptyList();
    }
}