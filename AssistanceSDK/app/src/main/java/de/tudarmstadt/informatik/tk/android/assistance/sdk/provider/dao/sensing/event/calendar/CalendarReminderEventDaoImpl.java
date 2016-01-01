package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.calendar;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminderEventDaoImpl extends
        CommonEventDaoImpl<DbCalendarReminderEvent> implements
        CalendarReminderEventDao {

    private static final String TAG = CalendarReminderEventDaoImpl.class.getSimpleName();

    private static CalendarReminderEventDao INSTANCE;

    private CalendarReminderEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCalendarReminderEventDao());
    }

    public static CalendarReminderEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarReminderEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public CalendarReminder convertObject(DbCalendarReminderEvent sensor) {

        if (sensor == null) {
            return null;
        }

        Boolean isDefaultOffset = sensor.getMinutes() == null ? Boolean.TRUE : Boolean.FALSE;

        CalendarReminder result = null;

        if (isDefaultOffset) {
            result = new CalendarReminder(
                    isDefaultOffset,
                    sensor.getMethod()
            );
        } else {
            result = new CalendarReminder(
                    isDefaultOffset,
                    sensor.getMethod(),
                    sensor.getMinutes()
            );
        }


        return result;
    }

    @Override
    public List<DbCalendarReminderEvent> getAllByEventId(long eventId) {

        if (eventId <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbCalendarReminderEventDao.Properties.EventId.eq(eventId))
                .build()
                .list();
    }

    @Override
    public DbCalendarReminderEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbCalendarReminderEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbCalendarReminderEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCalendarReminderEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
