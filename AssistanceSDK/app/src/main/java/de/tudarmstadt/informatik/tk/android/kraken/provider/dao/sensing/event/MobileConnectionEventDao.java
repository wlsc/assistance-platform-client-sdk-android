package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.MobileConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface MobileConnectionEventDao extends CommonEventDao {

    MobileConnectionEventDto convertObject(DbMobileConnectionEvent dbSensor);

}
