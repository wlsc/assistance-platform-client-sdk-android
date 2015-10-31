package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.PowerStateEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDaoImpl extends
        CommonEventDaoImpl implements
        PowerStatusEventDao {

    private static final String TAG = PowerStateEventDaoImpl.class.getSimpleName();

    private static PowerStatusEventDao INSTANCE;

    private DbPowerStateEventDao dao;

    private PowerStateEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbPowerStateEventDao();
        }
    }

    public static PowerStatusEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerStateEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public PowerStateEventDto convertObject(DbPowerStateEvent dbSensor) {
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

        Log.d(ForegroundTrafficEvent.class.getSimpleName(), "Dumping POWER STATE data to db...");

        long result = dao.insertOrReplace((DbPowerStateEvent) sensor);

        Log.d(ForegroundTrafficEvent.class.getSimpleName(), "Finished dumping data");

        return 0;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {
        return null;
    }
}
