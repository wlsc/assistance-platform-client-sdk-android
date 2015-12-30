package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.calendar;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.calendar.CalendarEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

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

        result.setEventId(String.valueOf(sensor.getEventId()));
        result.setCalendarId(String.valueOf(sensor.getCalendarId()));
        result.setAllDay(sensor.getAllDay());
        result.setAvailability(sensor.getAvailability());
        result.setDescription(sensor.getDescription());
        result.setStartDate(DateUtils.timestampToISO8601String(sensor.getTimestampStart(), sensor.getTimezoneStart()));
        result.setEndDate(DateUtils.timestampToISO8601String(sensor.getTimestampEnd(), sensor.getTimezoneEnd()));
        result.setDuration(sensor.getDuration());
        result.setLocation(sensor.getLocation());
        result.setRecurrenceExceptionDate(sensor.getRecurrenceExceptionDate());
        result.setRecurrenceExceptionRule(sensor.getRecurrenceExceptionRule());
        result.setLastDate(sensor.getLastDate());
        result.setOriginalAllDay(sensor.getOriginalAllDay());
        result.setOriginalId(sensor.getOriginalId());
        result.setOriginalInstanceTime(sensor.getOriginalInstanceTime());
        result.setRecurrenceDate(sensor.getRecurrenceDate());
        result.setRecurrenceRule(sensor.getRecurrenceRule());
        result.setStatus(sensor.getStatus());
        result.setTitle(sensor.getTitle());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbCalendarEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbCalendarEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
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

    @Override
    public List<DbCalendarEvent> getAllUpdated() {
        return dao
                .queryBuilder()
                .where(DbCalendarEventDao.Properties.IsUpdated.eq(1))
                .build()
                .list();
    }
}