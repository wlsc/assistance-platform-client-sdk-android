package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.MotionActivityEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface MotionActivityEventDao extends CommonEventDao {

    MotionActivityEventDto convertObject(DbMotionActivityEvent dbSensor);

}