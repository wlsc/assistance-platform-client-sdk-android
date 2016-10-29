package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.MobileConnectionSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class MobileConnectionSensorDaoImpl extends
        CommonEventDaoImpl<DbMobileConnectionSensor> implements
        MobileConnectionSensorDao {

    private static final String TAG = MobileConnectionSensorDaoImpl.class.getSimpleName();

    @Inject
    public MobileConnectionSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMobileConnectionSensorDao());
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
}