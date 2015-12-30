package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccountReaderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccountReaderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.AccountReaderEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountReaderEventDaoImpl extends
        CommonEventDaoImpl<DbAccountReaderEvent> implements
        AccountReaderEventDao {

    private static final String TAG = AccountReaderEventDaoImpl.class.getSimpleName();

    private static AccountReaderEventDao INSTANCE;

    private AccountReaderEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbAccountReaderEventDao());
    }

    public static AccountReaderEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new AccountReaderEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbAccountReaderEvent sensor) {

        if (sensor == null) {
            return null;
        }

        AccountReaderEventDto result = new AccountReaderEventDto();

        result.setTypes(sensor.getTypes());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public DbAccountReaderEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbAccountReaderEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbAccountReaderEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbAccountReaderEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
