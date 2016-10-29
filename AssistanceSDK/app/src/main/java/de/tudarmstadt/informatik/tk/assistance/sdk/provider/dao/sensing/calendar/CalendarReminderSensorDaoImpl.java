package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.Property;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensorDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.calendar.CalendarReminder;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class CalendarReminderSensorDaoImpl extends
        CommonEventDaoImpl<DbCalendarReminderSensor> implements
        CalendarReminderSensorDao {

    private static final String TAG = CalendarReminderSensorDaoImpl.class.getSimpleName();

    @Inject
    public CalendarReminderSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbCalendarReminderSensorDao());
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
    public List<DbCalendarReminderSensor> getAllByEventId(long eventId, long deviceId) {

        if (eventId <= 0 || deviceId < 0) {
            return Collections.emptyList();
        }

        for (Property property : properties) {
            if (property.name.equals(DEVICE_ID_FIELD_NAME)) {
                return dao
                        .queryBuilder()
                        .where(Properties.EventId.eq(eventId))
                        .where(property.eq(deviceId))
                        .build()
                        .list();
            }
        }

        return Collections.emptyList();
    }
}