package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbWifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.WifiConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class WifiConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        WifiConnectionEventDao {

    private static final String TAG = WifiConnectionEventDaoImpl.class.getSimpleName();

    private static WifiConnectionEventDao INSTANCE;

    private DbWifiConnectionEventDao dao;

    private WifiConnectionEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbWifiConnectionEventDao();
        }
    }

    public static WifiConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new WifiConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public WifiConnectionEventDto convertObject(DbWifiConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        WifiConnectionEventDto result = new WifiConnectionEventDto();

        result.setId(sensor.getId());
        result.setSsid(sensor.getSsid());
        result.setBssid(sensor.getBssid());
        result.setChannel(sensor.getChannel());
        result.setFrequency(sensor.getFrequency());
        result.setLinkSpeed(sensor.getLinkSpeed());
        result.setSignalStrength(sensor.getSignalStrength());
        result.setNetworkId(sensor.getNetworkId());
        result.setType(DtoType.WIFI_CONNECTION);
        result.setTypeStr(DtoType.getApiName(DtoType.WIFI_CONNECTION));
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
            return Collections.emptyList();
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
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbWifiConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping WIFI CONNECTION data to db...");

        long result = dao.insertOrReplace((DbWifiConnectionEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbWifiConnectionEvent>) events);
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbWifiConnectionEvent dbSensor : (List<DbWifiConnectionEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }
}