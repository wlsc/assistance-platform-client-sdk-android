package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.MobileConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MobileConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        MobileConnectionEventDao {

    private static final String TAG = MobileConnectionEventDaoImpl.class.getSimpleName();

    private static MobileConnectionEventDao INSTANCE;

    private DbMobileConnectionEventDao mobileConnectionEventDao;

    private MobileConnectionEventDaoImpl(DaoSession daoSession) {

        if (mobileConnectionEventDao == null) {
            mobileConnectionEventDao = daoSession.getDbMobileConnectionEventDao();
        }
    }

    public static MobileConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MobileConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MobileConnectionEventDto convertObject(DbMobileConnectionEvent dbSensor) {
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

        Log.d(ConnectionSensor.class.getSimpleName(), "Dumping MOBILE CONNECTION data to db...");

        long result = mobileConnectionEventDao.insertOrReplace((DbMobileConnectionEvent) sensor);

        Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }
}
