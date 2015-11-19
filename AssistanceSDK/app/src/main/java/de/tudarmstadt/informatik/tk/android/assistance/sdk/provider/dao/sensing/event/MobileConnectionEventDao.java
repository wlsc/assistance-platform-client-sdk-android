package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.MobileConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface MobileConnectionEventDao extends CommonEventDao {

    MobileConnectionEventDto convertObject(DbMobileConnectionEvent dbSensor);

}
