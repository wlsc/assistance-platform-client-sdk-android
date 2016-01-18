package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar;

import android.support.annotation.Nullable;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarSensorDaoImpl extends
        CommonEventDaoImpl<DbCalendarSensor> implements
        CalendarSensorDao {

    private static final String TAG = CalendarSensorDaoImpl.class.getSimpleName();

    private static CalendarSensorDao INSTANCE;

    private CalendarSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCalendarSensorDao());
    }

    public static CalendarSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public CalendarSensorDto convertObject(DbCalendarSensor sensor) {

        if (sensor == null) {
            return null;
        }

        CalendarSensorDto result = new CalendarSensorDto();

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
    public List<DbCalendarSensor> getAllUpdated() {
        return dao
                .queryBuilder()
                .where(DbCalendarSensorDao.Properties.IsUpdated.eq(1))
                .build()
                .list();
    }
}