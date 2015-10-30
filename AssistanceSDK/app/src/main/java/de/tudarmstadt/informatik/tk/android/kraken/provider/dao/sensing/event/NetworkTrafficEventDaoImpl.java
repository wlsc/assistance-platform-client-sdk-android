package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNetworkTrafficEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.NetworkTrafficEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NetworkTrafficEventDaoImpl extends
        CommonEventDaoImpl implements
        NetworkTrafficEventDao {

    private static final String TAG = NetworkTrafficEventDaoImpl.class.getSimpleName();

    private static NetworkTrafficEventDao INSTANCE;

    private DbNetworkTrafficEventDao networkTrafficEventDao;

    private NetworkTrafficEventDaoImpl(DaoSession daoSession) {

        if (networkTrafficEventDao == null) {
            networkTrafficEventDao = daoSession.getDbNetworkTrafficEventDao();
        }
    }

    public static NetworkTrafficEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new NetworkTrafficEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public NetworkTrafficEventDto convertObject(DbNetworkTrafficEvent dbSensor) {
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

        Log.d(ForegroundTrafficEvent.class.getSimpleName(), "Dumping NETWORK TRAFFIC data to db...");

        long result = networkTrafficEventDao.insertOrReplace((DbNetworkTrafficEvent) sensor);

        Log.d(ForegroundTrafficEvent.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {
        return null;
    }
}
