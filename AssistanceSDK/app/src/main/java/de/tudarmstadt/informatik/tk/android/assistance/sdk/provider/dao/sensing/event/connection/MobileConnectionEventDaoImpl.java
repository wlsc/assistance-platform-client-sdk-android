package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.connection;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.MobileConnectionSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MobileConnectionEventDaoImpl extends
        CommonEventDaoImpl<DbMobileConnectionSensor> implements
        MobileConnectionEventDao {

    private static final String TAG = MobileConnectionEventDaoImpl.class.getSimpleName();

    private static MobileConnectionEventDao INSTANCE;

    private MobileConnectionEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMobileConnectionSensorDao());
    }

    public static MobileConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MobileConnectionEventDaoImpl(mDaoSession);
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

    @Nullable
    @Override
    public DbMobileConnectionSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbMobileConnectionSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
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