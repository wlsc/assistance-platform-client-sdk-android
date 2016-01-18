package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NetworkTrafficSensorDao extends CommonEventDao<DbNetworkTrafficSensor> {

    List<DbNetworkTrafficSensor> getAllBackground(long deviceId);

    List<DbNetworkTrafficSensor> getAllForeground(long deviceId);

    List<DbNetworkTrafficSensor> getFirstNBackground(int amount, long deviceId);

    List<DbNetworkTrafficSensor> getFirstNForeground(int amount, long deviceId);
}