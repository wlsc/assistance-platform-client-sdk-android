package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.PowerStateEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public interface PowerStateEventDao extends CommonEventDao {

    PowerStateEventDto convertObject(DbPowerStateEvent dbSensor);

}
