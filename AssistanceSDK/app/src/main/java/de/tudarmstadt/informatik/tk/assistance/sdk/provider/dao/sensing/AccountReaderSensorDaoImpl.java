package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.AccountReaderSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public final class AccountReaderSensorDaoImpl extends
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
}