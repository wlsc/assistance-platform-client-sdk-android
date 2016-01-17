package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CalendarSensorDao extends CommonEventDao<DbCalendarSensor> {

    List<DbCalendarSensor> getAllUpdated();
}
