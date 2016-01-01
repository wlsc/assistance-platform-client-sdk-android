package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.NetworkTrafficEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NetworkTrafficEventDaoImpl extends
        CommonEventDaoImpl<DbNetworkTrafficEvent> implements
        NetworkTrafficEventDao {

    private static final String TAG = NetworkTrafficEventDaoImpl.class.getSimpleName();

    private static NetworkTrafficEventDao INSTANCE;

    private NetworkTrafficEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbNetworkTrafficEventDao());
    }

    public static NetworkTrafficEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new NetworkTrafficEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public NetworkTrafficEventDto convertObject(DbNetworkTrafficEvent sensor) {

        if (sensor == null) {
            return null;
        }

        NetworkTrafficEventDto result = new NetworkTrafficEventDto();

        result.setAppName(sensor.getAppName());
        result.setRxBytes(sensor.getRxBytes());
        result.setTxBytes(sensor.getTxBytes());
        result.setBackground(sensor.getBackground());
        result.setLongitude(sensor.getLongitude());
        result.setLatitude(sensor.getLatitude());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbNetworkTrafficEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbNetworkTrafficEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbNetworkTrafficEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbNetworkTrafficEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficEvent> getAllBackground() {
        return dao
                .queryBuilder()
                .where(DbNetworkTrafficEventDao.Properties.Background.eq(Boolean.TRUE))
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficEvent> getAllForeground() {
        return dao
                .queryBuilder()
                .where(DbNetworkTrafficEventDao.Properties.Background.eq(Boolean.FALSE))
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficEvent> getFirstNBackground(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbNetworkTrafficEventDao.Properties.Background.eq(Boolean.TRUE))
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficEvent> getFirstNForeground(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbNetworkTrafficEventDao.Properties.Background.eq(Boolean.FALSE))
                .limit(amount)
                .build()
                .list();
    }
}