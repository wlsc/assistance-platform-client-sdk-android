package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.sensor.MagneticFieldSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MagneticFieldSensorDaoImpl extends
        CommonEventDaoImpl<DbMagneticFieldSensor> implements
        MagneticFieldSensorDao {

    private static final String TAG = MagneticFieldSensorDaoImpl.class.getSimpleName();

    private static MagneticFieldSensorDao INSTANCE;

    private MagneticFieldSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMagneticFieldSensorDao());
    }

    public static MagneticFieldSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MagneticFieldSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MagneticFieldSensorDto convertObject(DbMagneticFieldSensor sensor) {

        if (sensor == null) {
            return null;
        }

        MagneticFieldSensorDto result = new MagneticFieldSensorDto();

        result.setId(sensor.getId());
        result.setX(sensor.getX());
        result.setY(sensor.getY());
        result.setZ(sensor.getZ());
        result.setAccuracy(sensor.getAccuracy());
        result.setxUncalibratedNoHardIron(sensor.getXUncalibratedNoHardIron());
        result.setyUncalibratedNoHardIron(sensor.getYUncalibratedNoHardIron());
        result.setzUncalibratedNoHardIron(sensor.getZUncalibratedNoHardIron());
        result.setxUncalibratedEstimatedIronBias(sensor.getXUncalibratedEstimatedIronBias());
        result.setyUncalibratedEstimatedIronBias(sensor.getYUncalibratedEstimatedIronBias());
        result.setzUncalibratedEstimatedIronBias(sensor.getZUncalibratedEstimatedIronBias());
        result.setType(DtoType.MAGNETIC_FIELD);
        result.setTypeStr(DtoType.getApiName(DtoType.MAGNETIC_FIELD));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbMagneticFieldSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMagneticFieldSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}