package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.NetworkTrafficEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NetworkTrafficEventDao extends CommonEventDao {

    NetworkTrafficEventDto convertObject(DbNetworkTrafficEvent dbSensor);

}
