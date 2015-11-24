package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.ForegroundEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ForegroundEventDaoImpl extends
        CommonEventDaoImpl<DbForegroundEvent> implements
        ForegroundEventDao {

    private static final String TAG = ForegroundEventDaoImpl.class.getSimpleName();

    private static ForegroundEventDao INSTANCE;

    private ForegroundEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbForegroundEventDao());
    }

    public static ForegroundEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public ForegroundEventDto convertObject(DbForegroundEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ForegroundEventDto result = new ForegroundEventDto();

        result.setId(sensor.getId());
        result.setAppName(sensor.getAppName());
        result.setActivityLabel(sensor.getActivityLabel());
        result.setClassName(sensor.getClassName());
        result.setColor(sensor.getColor());
        result.setKeystrokes(sensor.getKeystrokes());
        result.setPackageName(sensor.getPackageName());
        result.setUrl(sensor.getUrl());
        result.setEventType(DtoType.getApiName(sensor.getEventType()));
        result.setType(DtoType.FOREGROUND);
        result.setTypeStr(DtoType.getApiName(DtoType.FOREGROUND));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbForegroundEvent> getLastN(int amount) {

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
}