package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbGyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.GyroscopeSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class GyroscopeSensorDaoImpl extends
        CommonEventDaoImpl implements
        GyroscopeSensorDao {

    private static final String TAG = GyroscopeSensorDaoImpl.class.getSimpleName();

    private static GyroscopeSensorDao INSTANCE;

    private DbGyroscopeSensorDao gyroscopeSensorDao;

    private GyroscopeSensorDaoImpl(DaoSession daoSession) {

        if (gyroscopeSensorDao == null) {
            gyroscopeSensorDao = daoSession.getDbGyroscopeSensorDao();
        }
    }

    public static GyroscopeSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new GyroscopeSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public GyroscopeSensorDto convertObject(DbGyroscopeSensor dbSensor) {
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

        Log.d(GyroscopeSensor.class.getSimpleName(), "Dumping data to db...");

        long result = gyroscopeSensorDao.insertOrReplace((DbGyroscopeSensor) sensor);

        Log.d(GyroscopeSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }
}
