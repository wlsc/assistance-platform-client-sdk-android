package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.CallLogEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CallLogEventDao extends CommonEventDao {

    CallLogEventDto convertObject(DbCallLogEvent sensor);

    DbCallLogEvent getLastCallLogEvent();
}
