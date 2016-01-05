package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbBrowserHistoryEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbBrowserHistoryEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event.BrowserHistoryEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class BrowserHistoryEventDaoImpl extends
        CommonEventDaoImpl<DbBrowserHistoryEvent> implements
        BrowserHistoryEventDao {

    private static final String TAG = BrowserHistoryEventDaoImpl.class.getSimpleName();

    private static BrowserHistoryEventDao INSTANCE;

    private BrowserHistoryEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbBrowserHistoryEventDao());
    }

    public static BrowserHistoryEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new BrowserHistoryEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbBrowserHistoryEvent sensor) {

        if (sensor == null) {
            return null;
        }

        BrowserHistoryEventDto result = new BrowserHistoryEventDto();

        result.setUrl(sensor.getUrl());
        result.setTitle(sensor.getTitle());
        result.setLastVisited(sensor.getLastVisited());
        result.setVisits(sensor.getVisits());
        result.setBookmark(sensor.getBookmark());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(result.getIsDeleted());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbBrowserHistoryEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbBrowserHistoryEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbBrowserHistoryEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbBrowserHistoryEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
