package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NetworkTrafficSensorDao extends CommonEventDao<DbNetworkTrafficSensor> {

    List<DbNetworkTrafficSensor> getAllBackground();

    List<DbNetworkTrafficSensor> getAllForeground();

    List<DbNetworkTrafficSensor> getFirstNBackground(int amount);

    List<DbNetworkTrafficSensor> getFirstNForeground(int amount);

}
