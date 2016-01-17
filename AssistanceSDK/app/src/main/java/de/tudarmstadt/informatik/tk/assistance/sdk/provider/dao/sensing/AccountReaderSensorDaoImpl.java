package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.AccountReaderSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountReaderSensorDaoImpl extends
        CommonEventDaoImpl<DbAccountReaderSensor> implements
        AccountReaderSensorDao {

    private static final String TAG = AccountReaderSensorDaoImpl.class.getSimpleName();

    private static AccountReaderSensorDao INSTANCE;

    private AccountReaderSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbAccountReaderSensorDao());
    }

    public static AccountReaderSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new AccountReaderSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbAccountReaderSensor sensor) {

        if (sensor == null) {
            return null;
        }

        AccountReaderSensorDto result = new AccountReaderSensorDto();

        result.setTypes(sensor.getTypes());
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Nullable
    @Override
    public DbAccountReaderSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbAccountReaderSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbAccountReaderSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbAccountReaderSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
