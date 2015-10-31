package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.MotionActivityEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MotionActivityEventDaoImpl extends
        CommonEventDaoImpl implements
        MotionActivityEventDao {

    private static final String TAG = MotionActivityEventDaoImpl.class.getSimpleName();

    private static MotionActivityEventDao INSTANCE;

    private DbMotionActivityEventDao motionActivityEventDao;

    private MotionActivityEventDaoImpl(DaoSession daoSession) {

        if (motionActivityEventDao == null) {
            motionActivityEventDao = daoSession.getDbMotionActivityEventDao();
        }
    }

    public static MotionActivityEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MotionActivityEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MotionActivityEventDto convertObject(DbMotionActivityEvent sensor) {

        if (sensor == null) {
            return null;
        }

        MotionActivityEventDto result = new MotionActivityEventDto();

        result.setId(sensor.getId());
        result.setType(DtoType.MOTION_ACTIVITY);
        result.setTypeStr(DtoType.getApiName(DtoType.MOTION_ACTIVITY));
        result.setRunning(sensor.getRunning());
        result.setStationary(sensor.getStationary());
        result.setCycling(sensor.getCycling());
        result.setWalking(sensor.getWalking());
        result.setDriving(sensor.getDriving());
        result.setOnFoot(sensor.getOnFoot());
        result.setTilting(sensor.getTilting());
        result.setUnknown(sensor.getUnknown());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new LinkedList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbMotionActivityEvent dbSensor : (List<DbMotionActivityEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return motionActivityEventDao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return motionActivityEventDao
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

        return motionActivityEventDao
                .queryBuilder()
                .orderDesc(DbMotionActivityEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(MotionActivityEvent.class.getSimpleName(), "Dumping data to db...");

        long result = motionActivityEventDao.insertOrReplace((DbMotionActivityEvent) sensor);

        Log.d(MotionActivityEvent.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        motionActivityEventDao.deleteInTx((Iterable<DbMotionActivityEvent>) events);
    }
}
