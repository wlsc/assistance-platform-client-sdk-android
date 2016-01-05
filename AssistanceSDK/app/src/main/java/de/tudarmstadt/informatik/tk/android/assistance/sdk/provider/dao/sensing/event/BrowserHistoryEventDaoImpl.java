package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbBrowserHistorySensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbBrowserHistorySensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.sensor.BrowserHistorySensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class BrowserHistoryEventDaoImpl extends
        CommonEventDaoImpl<DbBrowserHistorySensor> implements
        BrowserHistoryEventDao {

    private static final String TAG = BrowserHistoryEventDaoImpl.class.getSimpleName();

    private static BrowserHistoryEventDao INSTANCE;

    private BrowserHistoryEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbBrowserHistorySensorDao());
    }

    public static BrowserHistoryEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new BrowserHistoryEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbBrowserHistorySensor sensor) {

        if (sensor == null) {
            return null;
        }

        BrowserHistorySensorDto result = new BrowserHistorySensorDto();

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
    public DbBrowserHistorySensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbBrowserHistorySensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbBrowserHistorySensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbBrowserHistorySensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
