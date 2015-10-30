package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.event;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCalendarEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.event.CalendarEventDto;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class CalendarEventDaoImpl extends
        CommonEventDaoImpl implements
        CalendarEventDao {

    private static final String TAG = CalendarEventDaoImpl.class.getSimpleName();

    private static CalendarEventDao INSTANCE;

    private DbCalendarEventDao calendarEventDao;

    private CalendarEventDaoImpl(DaoSession daoSession) {

        if (calendarEventDao == null) {
            calendarEventDao = daoSession.getDbCalendarEventDao();
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
        return null;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return calendarEventDao
                .queryBuilder()
                .build()
                .list();
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
