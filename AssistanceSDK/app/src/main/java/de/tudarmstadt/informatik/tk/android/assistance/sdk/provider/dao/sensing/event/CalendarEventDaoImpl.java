package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.CalendarEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarEventDaoImpl extends
        CommonEventDaoImpl<DbCalendarEvent> implements
        CalendarEventDao {

    private static final String TAG = CalendarEventDaoImpl.class.getSimpleName();

    private static CalendarEventDao INSTANCE;

    private CalendarEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCalendarEventDao());
    }

    public static CalendarEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public CalendarEventDto convertObject(DbCalendarEvent sensor) {

        if (sensor == null) {
            return null;
        }

        CalendarEventDto result = new CalendarEventDto();

        result.setId(sensor.getId());
        result.setEventId(sensor.getEventId());
        result.setCalendarId(sensor.getCalendarId());
        result.setAllDay(sensor.getAllDay());
        result.setAvailability(sensor.getAvailability());
        result.setDescription(sensor.getDescription());
        result.setTimestampStart(sensor.getTimestampStart());
        result.setTimestampEnd(sensor.getTimestampEnd());
        result.setDuration(sensor.getDuration());
        result.setLocation(sensor.getLocation());
        result.setTimezoneStart(sensor.getTimezoneStart());
        result.setTimezoneEnd(sensor.getTimezoneEnd());
        result.setRecurrenceExceptionDate(sensor.getRecurrenceExceptionDate());
        result.setRecurrenceExceptionRule(sensor.getRecurrenceExceptionRule());
        result.setHasAlarm(sensor.getHasAlarm());
        result.setLastDate(sensor.getLastDate());
        result.setOriginalAllDay(sensor.getOriginalAllDay());
        result.setOriginalId(sensor.getOriginalId());
        result.setOriginalInstanceTime(sensor.getOriginalInstanceTime());
        result.setRecurrenceDate(sensor.getRecurrenceDate());
        result.setRecurrenceRule(sensor.getRecurrenceRule());
        result.setStatus(sensor.getStatus());
        result.setTitle(sensor.getTitle());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(sensor.getIsUpdated());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setType(DtoType.CALENDAR);
        result.setTypeStr(DtoType.getApiName(DtoType.CALENDAR));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbCalendarEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCalendarEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
