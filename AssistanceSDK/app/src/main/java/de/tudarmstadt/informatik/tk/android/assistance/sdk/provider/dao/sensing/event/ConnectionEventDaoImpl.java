package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.ConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        ConnectionEventDao {

    private static final String TAG = ConnectionEventDaoImpl.class.getSimpleName();

    private static ConnectionEventDao INSTANCE;

    private DbConnectionEventDao dao;

    private ConnectionEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbConnectionEventDao();
        }
    }

    public static ConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public ConnectionEventDto convertObject(DbConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ConnectionEventDto result = new ConnectionEventDto();

        result.setId(sensor.getId());
        result.setIsMobile(sensor.getIsMobile());
        result.setIsWifi(sensor.getIsWifi());
        result.setType(DtoType.CONNECTION);
        result.setTypeStr(DtoType.getApiName(DtoType.CONNECTION));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbConnectionEvent dbSensor : (List<DbConnectionEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

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
                .orderDesc(DbConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping to db...");

        long result = dao.insertOrReplace((DbConnectionEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbConnectionEvent>) events);
    }
}
