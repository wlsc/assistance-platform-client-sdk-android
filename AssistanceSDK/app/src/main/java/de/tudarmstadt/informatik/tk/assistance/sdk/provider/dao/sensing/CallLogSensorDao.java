package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CallLogSensorDao extends CommonEventDao<DbCallLogSensor> {

    DbCallLogSensor getLastCallLogEvent();

}
