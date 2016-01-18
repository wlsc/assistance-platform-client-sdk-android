package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbWifiConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.WifiConnectionSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class WifiConnectionSensorDaoImpl extends
        CommonEventDaoImpl<DbWifiConnectionSensor> implements
        WifiConnectionSensorDao {

    private static final String TAG = WifiConnectionSensorDaoImpl.class.getSimpleName();

    private static WifiConnectionSensorDao INSTANCE;

    private WifiConnectionSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbWifiConnectionSensorDao());
    }

    public static WifiConnectionSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new WifiConnectionSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public WifiConnectionSensorDto convertObject(DbWifiConnectionSensor sensor) {

        if (sensor == null) {
            return null;
        }

        WifiConnectionSensorDto result = new WifiConnectionSensorDto();

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
}