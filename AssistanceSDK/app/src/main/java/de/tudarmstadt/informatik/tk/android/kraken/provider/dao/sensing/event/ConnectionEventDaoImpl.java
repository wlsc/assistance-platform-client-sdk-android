package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.ConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        ConnectionEventDao {

    private static final String TAG = ConnectionEventDaoImpl.class.getSimpleName();

    private static ConnectionEventDao INSTANCE;

    private DbConnectionEventDao connectionEventDao;

    private ConnectionEventDaoImpl(DaoSession daoSession) {

        if (connectionEventDao == null) {
            connectionEventDao = daoSession.getDbConnectionEventDao();
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
        return null;
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {
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

        Log.d(ConnectionSensor.class.getSimpleName(), "Dumping CONNECTION data to db...");

        long result = connectionEventDao.insertOrReplace((DbConnectionEvent) sensor);

        Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }
}
