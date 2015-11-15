package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.ForegroundEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ForegroundEventDaoImpl extends
        CommonEventDaoImpl implements
        ForegroundEventDao {

    private static final String TAG = ForegroundEventDaoImpl.class.getSimpleName();

    private static ForegroundEventDao INSTANCE;

    private DbForegroundEventDao dao;

    private ForegroundEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbForegroundEventDao();
        }
    }

    public static ForegroundEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public ForegroundEventDto convertObject(DbForegroundEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ForegroundEventDto result = new ForegroundEventDto();

        result.setId(sensor.getId());
        result.setAppName(sensor.getAppName());
        result.setActivityLabel(sensor.getActivityLabel());
        result.setClassName(sensor.getClassName());
        result.setColor(sensor.getColor());
        result.setKeystrokes(sensor.getKeystrokes());
        result.setPackageName(sensor.getPackageName());
        result.setUrl(sensor.getUrl());
        result.setEventType(DtoType.getApiName(sensor.getEventType()));
        result.setType(DtoType.FOREGROUND);
        result.setTypeStr(DtoType.getApiName(DtoType.FOREGROUND));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbForegroundEvent dbSensor : (List<DbForegroundEvent>) dbSensors) {
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
                .orderDesc(DbForegroundEventDao.Properties.Id)
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

        long result = dao.insertOrReplace((DbForegroundEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbForegroundEvent>) events);
    }
}
