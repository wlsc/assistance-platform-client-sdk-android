package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarReminderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.CalendarReminderEventDto;
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
        result.setType(DtoType.CALENDAR_REMINDER);
        result.setTypeStr(DtoType.getApiName(DtoType.CALENDAR_REMINDER));
        result.setCreated(sensor.getCreated());

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
