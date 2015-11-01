package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarReminderEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarReminderEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CalendarReminderEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminderEventDaoImpl extends
        CommonEventDaoImpl implements
        CalendarReminderEventDao {

    private static final String TAG = CalendarReminderEventDaoImpl.class.getSimpleName();

    private static CalendarReminderEventDao INSTANCE;

    private DbCalendarReminderEventDao dao;

    private CalendarReminderEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbCalendarReminderEventDao();
        }
    }

    public static CalendarReminderEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarReminderEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public CalendarReminderEventDto convertObject(DbCalendarReminderEvent sensor) {

        if (sensor == null) {
            return null;
        }

        CalendarReminderEventDto result = new CalendarReminderEventDto();

        result.setId(sensor.getId());
        result.setReminderId(sensor.getReminderId());
        result.setEventId(sensor.getEventId());
        result.setMethod(sensor.getMethod());
        result.setMinutes(sensor.getMinutes());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(sensor.getIsUpdated());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbCalendarReminderEvent> getAllByEventId(long eventId) {

        if (eventId <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .where(DbCalendarReminderEventDao.Properties.EventId.eq(eventId))
                .build()
                .list();
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
            return Collections.EMPTY_LIST;
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
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCalendarReminderEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(CalendarReminderEventDao.class.getSimpleName(), "Dumping data to db...");

        long result = dao.insertOrReplace((DbCalendarReminderEvent) sensor);

        Log.d(CalendarReminderEventDao.class.getSimpleName(), "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbCalendarReminderEvent>) events);
    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<Sensor> result = new LinkedList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbCalendarReminderEvent dbSensor : (List<DbCalendarReminderEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }
}
