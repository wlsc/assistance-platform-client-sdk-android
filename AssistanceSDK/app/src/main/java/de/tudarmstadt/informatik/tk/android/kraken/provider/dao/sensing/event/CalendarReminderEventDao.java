package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarReminderEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CalendarReminderEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CalendarReminderEventDao extends CommonEventDao {

    CalendarReminderEventDto convertObject(DbCalendarReminderEvent sensor);

    List<DbCalendarReminderEvent> getAllByEventId(long eventId);

}
