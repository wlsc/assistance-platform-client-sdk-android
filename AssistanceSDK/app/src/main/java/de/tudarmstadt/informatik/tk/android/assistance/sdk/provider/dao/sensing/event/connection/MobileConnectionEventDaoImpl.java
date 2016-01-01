package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.connection;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.MobileConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MobileConnectionEventDaoImpl extends
        CommonEventDaoImpl<DbMobileConnectionEvent> implements
        MobileConnectionEventDao {

    private static final String TAG = MobileConnectionEventDaoImpl.class.getSimpleName();

    private static MobileConnectionEventDao INSTANCE;

    private MobileConnectionEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbMobileConnectionEventDao());
    }

    public static MobileConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MobileConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MobileConnectionEventDto convertObject(DbMobileConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        MobileConnectionEventDto result = new MobileConnectionEventDto();

        result.setCarrierName(sensor.getCarrierName());
        result.setMobileCountryCode(sensor.getMobileCountryCode());
        result.setMobileNetworkCode(sensor.getMobileNetworkCode());
        result.setVoipAvailable(sensor.getVoipAvailable());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbMobileConnectionEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbMobileConnectionEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbMobileConnectionEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMobileConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}