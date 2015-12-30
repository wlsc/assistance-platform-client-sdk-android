package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.calendar;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CalendarEventDao extends CommonEventDao<DbCalendarEvent> {

    List<DbCalendarEvent> getAllUpdated();
}
