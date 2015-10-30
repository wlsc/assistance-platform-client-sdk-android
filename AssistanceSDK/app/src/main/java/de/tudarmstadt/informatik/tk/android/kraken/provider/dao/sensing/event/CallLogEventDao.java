package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CallLogEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CallLogEventDao extends CommonEventDao {

    CallLogEventDto convertObject(DbCallLogEvent sensor);

    DbCallLogEvent getLastCallLogEvent();
}
