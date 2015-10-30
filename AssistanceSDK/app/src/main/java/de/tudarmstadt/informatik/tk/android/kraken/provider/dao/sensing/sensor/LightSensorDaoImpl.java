package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbLightSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.LightSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class LightSensorDaoImpl extends
        CommonEventDaoImpl implements
        LightSensorDao {

    private static final String TAG = LightSensorDaoImpl.class.getSimpleName();

    private static LightSensorDao INSTANCE;

    private DbLightSensorDao lightSensorDao;

    private LightSensorDaoImpl(DaoSession daoSession) {

        if (lightSensorDao == null) {
            lightSensorDao = daoSession.getDbLightSensorDao();
        }
    }

    public static LightSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new LightSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public LightSensorDto convertObject(DbLightSensor dbSensor) {
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

        Log.d(LightSensor.class.getSimpleName(), "Dumping data to db...");

        long result = lightSensorDao.insertOrReplace((DbLightSensor) sensor);

        Log.d(LightSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }
}
