package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.calendar;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarReminderSensorDaoImpl extends
        CommonEventDaoImpl<DbCalendarReminderSensor> implements
        CalendarReminderSensorDao {

    private static final String TAG = CalendarReminderSensorDaoImpl.class.getSimpleName();

    private static CalendarReminderSensorDao INSTANCE;

    private CalendarReminderSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCalendarReminderSensorDao());
    }

    public static CalendarReminderSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarReminderSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public CalendarReminder convertObject(DbCalendarReminderSensor sensor) {

        if (sensor == null) {
            return null;
        }

        Boolean isDefaultOffset = sensor.getMinutes() == null ? Boolean.TRUE : Boolean.FALSE;

        CalendarReminder result;

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
    public List<DbCalendarReminderSensor> getAllByEventId(long eventId) {

        if (eventId <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbCalendarReminderSensorDao.Properties.EventId.eq(eventId))
                .build()
                .list();
    }

    @Nullable
    @Override
    public DbCalendarReminderSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbCalendarReminderSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbCalendarReminderSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbCalendarReminderSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
