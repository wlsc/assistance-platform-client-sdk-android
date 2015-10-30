package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbWifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.WifiConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class WifiConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        WifiConnectionEventDao {

    private static final String TAG = WifiConnectionEventDaoImpl.class.getSimpleName();

    private static WifiConnectionEventDao INSTANCE;

    private DbWifiConnectionEventDao wifiConnectionEventDao;

    private WifiConnectionEventDaoImpl(DaoSession daoSession) {

        if (wifiConnectionEventDao == null) {
            wifiConnectionEventDao = daoSession.getDbWifiConnectionEventDao();
        }
    }

    public static WifiConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new WifiConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public WifiConnectionEventDto convertObject(DbWifiConnectionEvent dbSensor) {
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

        Log.d(ConnectionSensor.class.getSimpleName(), "Dumping WIFI CONNECTION data to db...");

        long result = wifiConnectionEventDao.insertOrReplace((DbWifiConnectionEvent) sensor);

        Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");

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
