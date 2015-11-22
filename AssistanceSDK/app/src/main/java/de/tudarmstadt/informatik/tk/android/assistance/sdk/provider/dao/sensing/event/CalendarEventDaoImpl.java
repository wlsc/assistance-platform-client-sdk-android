package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.CalendarEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarEventDaoImpl extends
        CommonEventDaoImpl implements
        CalendarEventDao {

    private static final String TAG = CalendarEventDaoImpl.class.getSimpleName();

    private static CalendarEventDao INSTANCE;

    private DbCalendarEventDao dao;

    private CalendarEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbCalendarEventDao();
        }
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
    public List<? extends IDbSensor> getAll() {
        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbForegroundEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping data to db...");

        long result = dao.insertOrReplace((DbCalendarEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbCalendarEvent>) events);
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbCalendarEvent dbSensor : (List<DbCalendarEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }
}
