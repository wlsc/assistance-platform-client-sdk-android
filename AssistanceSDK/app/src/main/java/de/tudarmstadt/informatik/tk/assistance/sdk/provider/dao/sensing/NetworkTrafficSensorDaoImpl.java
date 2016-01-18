package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.NetworkTrafficSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NetworkTrafficSensorDaoImpl extends
        CommonEventDaoImpl<DbNetworkTrafficSensor> implements
        NetworkTrafficSensorDao {

    private static final String TAG = NetworkTrafficSensorDaoImpl.class.getSimpleName();

    private static NetworkTrafficSensorDao INSTANCE;

    private NetworkTrafficSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbNetworkTrafficSensorDao());
    }

    public static NetworkTrafficSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new NetworkTrafficSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public NetworkTrafficSensorDto convertObject(DbNetworkTrafficSensor sensor) {

        if (sensor == null) {
            return null;
        }

        NetworkTrafficSensorDto result = new NetworkTrafficSensorDto();

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
    public List<DbNetworkTrafficSensor> getAllBackground() {
        return dao
                .queryBuilder()
                .where(DbNetworkTrafficSensorDao.Properties.Background.eq(Boolean.TRUE))
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficSensor> getAllForeground() {
        return dao
                .queryBuilder()
                .where(DbNetworkTrafficSensorDao.Properties.Background.eq(Boolean.FALSE))
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficSensor> getFirstNBackground(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbNetworkTrafficSensorDao.Properties.Background.eq(Boolean.TRUE))
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<DbNetworkTrafficSensor> getFirstNForeground(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbNetworkTrafficSensorDao.Properties.Background.eq(Boolean.FALSE))
                .limit(amount)
                .build()
                .list();
    }
}