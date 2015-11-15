package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CallLogEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CallLogEventDaoImpl extends
        CommonEventDaoImpl implements
        CallLogEventDao {

    private static final String TAG = CallLogEventDaoImpl.class.getSimpleName();

    private static CallLogEventDao INSTANCE;

    private DbCallLogEventDao dao;

    private CallLogEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbCallLogEventDao();
        }
    }

    public static CallLogEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CallLogEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public CallLogEventDto convertObject(DbCallLogEvent sensor) {

        if (sensor == null) {
            return null;
        }

        CallLogEventDto result = new CallLogEventDto();

        result.setId(sensor.getId());
        result.setCallId(sensor.getCallId());
        result.setType(sensor.getType());
        result.setName(sensor.getName());
        result.setNumber(sensor.getNumber());
        result.setDate(sensor.getDate());
        result.setDuration(sensor.getDuration());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(sensor.getIsUpdated());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());
        result.setType(DtoType.CALL_LOG);
        result.setTypeStr(DtoType.getApiName(DtoType.CALL_LOG));

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {

        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCallLogEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping data to db...");

        long result = dao.insertOrReplace((DbCallLogEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbCallLogEvent>) events);
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbCallLogEvent dbSensor : (List<DbCallLogEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

    /**
     * Returns last element from table
     *
     * @return
     */
    @Override
    public DbCallLogEvent getLastCallLogEvent() {
        return dao
                .queryBuilder()
                .orderDesc(DbCallLogEventDao.Properties.Id)
                .limit(1)
                .build()
                .unique();
    }
}
