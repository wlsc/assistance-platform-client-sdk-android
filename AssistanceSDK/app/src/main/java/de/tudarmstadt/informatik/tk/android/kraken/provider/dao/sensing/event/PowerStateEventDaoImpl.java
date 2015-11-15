package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.PowerStateEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDaoImpl extends
        CommonEventDaoImpl implements
        PowerStateEventDao {

    private static final String TAG = PowerStateEventDaoImpl.class.getSimpleName();

    private static PowerStateEventDao INSTANCE;

    private DbPowerStateEventDao dao;

    private PowerStateEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbPowerStateEventDao();
        }
    }

    public static PowerStateEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerStateEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public PowerStateEventDto convertObject(DbPowerStateEvent dbSensor) {

        if (dbSensor == null) {
            return null;
        }

        PowerStateEventDto result = new PowerStateEventDto();

        result.setId(dbSensor.getId());
        result.setTypeState(dbSensor.getState());
        result.setChargingStatus(dbSensor.getChargingStatus());
        result.setType(DtoType.POWER_STATE);
        result.setTypeStr(DtoType.getApiName(DtoType.POWER_STATE));
        result.setCreated(dbSensor.getCreated());

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
                .orderDesc(DbPowerStateEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping POWER STATE data to db...");

        long result = dao.insertOrReplace((DbPowerStateEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbPowerStateEvent>) events);
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbPowerStateEvent dbSensor : (List<DbPowerStateEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }
}
