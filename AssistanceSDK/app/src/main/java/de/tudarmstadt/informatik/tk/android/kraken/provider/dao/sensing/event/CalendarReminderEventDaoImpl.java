package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import java.util.Collections;
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

    private DbCalendarReminderEventDao calendarReminderEventDao;

    private CalendarReminderEventDaoImpl(DaoSession daoSession) {

        if (calendarReminderEventDao == null) {
            calendarReminderEventDao = daoSession.getDbCalendarReminderEventDao();
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
        return null;
    }

    @Override
    public List<DbCalendarReminderEvent> getAllByEventId(long eventId) {

        if (eventId <= 0) {
            return Collections.EMPTY_LIST;
        }

        return calendarReminderEventDao
                .queryBuilder()
                .where(DbCalendarReminderEventDao.Properties.EventId.eq(eventId))
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return null;
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {
        return null;
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {
        return null;
    }

    @Override
    public long insert(IDbSensor sensor) {
        return 0;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

    }

    @Override
    public List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors) {
        return null;
    }
}
