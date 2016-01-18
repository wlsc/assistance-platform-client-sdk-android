package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.01.2016
 */
public interface TucanSensorDao extends CommonEventDao<DbTucanSensor> {

    /**
     * Gets item for supplied userId
     *
     * @param userId
     * @return
     */
    DbTucanSensor getForUser(Long userId);
}
