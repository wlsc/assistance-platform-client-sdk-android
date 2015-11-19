package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.NetworkTrafficEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NetworkTrafficEventDaoImpl extends
        CommonEventDaoImpl implements
        NetworkTrafficEventDao {

    private static final String TAG = NetworkTrafficEventDaoImpl.class.getSimpleName();

    private static NetworkTrafficEventDao INSTANCE;

    private DbNetworkTrafficEventDao dao;

    private NetworkTrafficEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbNetworkTrafficEventDao();
        }
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

        result.setId(sensor.getId());
        result.setAppName(sensor.getAppName());
        result.setRxBytes(sensor.getRxBytes());
        result.setTxBytes(sensor.getTxBytes());
        result.setBackground(sensor.getBackground());
        result.setLongitude(sensor.getLongitude());
        result.setLatitude(sensor.getLatitude());
        result.setType(DtoType.NETWORK_TRAFFIC);
        result.setTypeStr(DtoType.getApiName(DtoType.NETWORK_TRAFFIC));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .orderDesc(DbNetworkTrafficEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping data to db...");

        long result = dao.insertOrReplace((DbNetworkTrafficEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbNetworkTrafficEvent>) events);
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbNetworkTrafficEvent dbSensor : (List<DbNetworkTrafficEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }
}
