package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.PowerStateEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public interface PowerStatusEventDao extends CommonEventDao {

    PowerStateEventDto convertObject(DbPowerStateEvent dbSensor);

}
