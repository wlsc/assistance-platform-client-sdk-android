package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CallLogEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.CallLogEvent;
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

    private DbCallLogEventDao callLogEventDao;

    private CallLogEventDaoImpl(DaoSession daoSession) {

        if (callLogEventDao == null) {
            callLogEventDao = daoSession.getDbCallLogEventDao();
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
        return null;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return null;
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {
        return null;
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {
        return null;
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(CallLogEvent.class.getSimpleName(), "Dumping CALL LOG EVENT data to db...");

        long result = callLogEventDao.insertOrReplace((DbCallLogEvent) sensor);

        Log.d(CallLogEvent.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {
        return null;
    }

    /**
     * Returns last element from table
     *
     * @return
     */
    @Override
    public DbCallLogEvent getLastCallLogEvent() {
        return callLogEventDao
                .queryBuilder()
                .orderDesc(DbCallLogEventDao.Properties.Id)
                .limit(1)
                .build()
                .unique();
    }
}
