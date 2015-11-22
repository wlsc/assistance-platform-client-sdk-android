package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.ConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class ConnectionEventDaoImpl extends
        CommonEventDaoImpl<DbConnectionEvent> implements
        ConnectionEventDao {

    private static final String TAG = ConnectionEventDaoImpl.class.getSimpleName();

    private static ConnectionEventDao INSTANCE;

    private ConnectionEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbConnectionEventDao());
    }

    public static ConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public ConnectionEventDto convertObject(DbConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ConnectionEventDto result = new ConnectionEventDto();

        result.setId(sensor.getId());
        result.setIsMobile(sensor.getIsMobile());
        result.setIsWifi(sensor.getIsWifi());
        result.setType(DtoType.CONNECTION);
        result.setTypeStr(DtoType.getApiName(DtoType.CONNECTION));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbConnectionEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
