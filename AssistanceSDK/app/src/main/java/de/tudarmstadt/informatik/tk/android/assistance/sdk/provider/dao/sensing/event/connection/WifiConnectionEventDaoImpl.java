package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.connection;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.WifiConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class WifiConnectionEventDaoImpl extends
        CommonEventDaoImpl<DbWifiConnectionEvent> implements
        WifiConnectionEventDao {

    private static final String TAG = WifiConnectionEventDaoImpl.class.getSimpleName();

    private static WifiConnectionEventDao INSTANCE;

    private WifiConnectionEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbWifiConnectionEventDao());
    }

    public static WifiConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new WifiConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public WifiConnectionEventDto convertObject(DbWifiConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        WifiConnectionEventDto result = new WifiConnectionEventDto();

        result.setSsid(sensor.getSsid());
        result.setBssid(sensor.getBssid());
        result.setChannel(sensor.getChannel());
        result.setFrequency(sensor.getFrequency());
        result.setLinkSpeed(sensor.getLinkSpeed());
        result.setSignalStrength(sensor.getSignalStrength());
        result.setNetworkId(sensor.getNetworkId());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbWifiConnectionEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbWifiConnectionEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbWifiConnectionEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbWifiConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}