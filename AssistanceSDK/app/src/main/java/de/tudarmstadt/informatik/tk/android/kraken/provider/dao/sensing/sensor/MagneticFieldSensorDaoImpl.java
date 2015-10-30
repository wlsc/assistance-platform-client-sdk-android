package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.MagneticFieldSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MagneticFieldSensorDaoImpl extends
        CommonEventDaoImpl implements
        MagneticFieldSensorDao {

    private static final String TAG = MagneticFieldSensorDaoImpl.class.getSimpleName();

    private static MagneticFieldSensorDao INSTANCE;

    private DbMagneticFieldSensorDao magneticFieldSensorDao;

    private MagneticFieldSensorDaoImpl(DaoSession daoSession) {

        if (magneticFieldSensorDao == null) {
            magneticFieldSensorDao = daoSession.getDbMagneticFieldSensorDao();
        }
    }

    public static MagneticFieldSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MagneticFieldSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MagneticFieldSensorDto convertObject(DbMagneticFieldSensor dbSensor) {
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

        Log.d(MagneticFieldSensor.class.getSimpleName(), "Dumping data to db...");

        long result = magneticFieldSensorDao.insertOrReplace((DbMagneticFieldSensor) sensor);

        Log.d(MagneticFieldSensor.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }
}
