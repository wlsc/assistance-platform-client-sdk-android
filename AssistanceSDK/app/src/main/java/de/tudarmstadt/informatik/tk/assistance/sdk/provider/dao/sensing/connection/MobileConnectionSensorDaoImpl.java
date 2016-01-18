package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.MobileConnectionSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MobileConnectionSensorDaoImpl extends
        CommonEventDaoImpl<DbMobileConnectionSensor> implements
        MobileConnectionSensorDao {

    private static final String TAG = MobileConnectionSensorDaoImpl.class.getSimpleName();

    private static MobileConnectionSensorDao INSTANCE;

    private MobileConnectionSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMobileConnectionSensorDao());
    }

    public static MobileConnectionSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MobileConnectionSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public MobileConnectionSensorDto convertObject(DbMobileConnectionSensor sensor) {

        if (sensor == null) {
            return null;
        }

        MobileConnectionSensorDto result = new MobileConnectionSensorDto();

        result.setCarrierName(sensor.getCarrierName());
        result.setMobileCountryCode(sensor.getMobileCountryCode());
        result.setMobileNetworkCode(sensor.getMobileNetworkCode());
        result.setVoipAvailable(sensor.getVoipAvailable());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbMobileConnectionSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMobileConnectionSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}