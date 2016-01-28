package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public interface ContactSensorDao extends CommonEventDao<DbContactSensor> {

    List<DbContactSensor> getAllUpdated(long deviceId);
}