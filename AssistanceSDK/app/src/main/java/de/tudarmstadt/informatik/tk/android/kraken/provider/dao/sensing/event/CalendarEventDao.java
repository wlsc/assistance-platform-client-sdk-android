package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CalendarEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CalendarEventDao extends CommonEventDao {

    CalendarEventDto convertObject(DbCalendarEvent sensor);

}
