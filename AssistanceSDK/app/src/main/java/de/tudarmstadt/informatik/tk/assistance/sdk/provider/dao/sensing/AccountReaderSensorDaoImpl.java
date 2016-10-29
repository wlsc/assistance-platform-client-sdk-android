package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.AccountReaderSensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
@Singleton
public final class AccountReaderSensorDaoImpl extends
        CommonEventDaoImpl<DbAccountReaderSensor> implements
        AccountReaderSensorDao {

    private static final String TAG = AccountReaderSensorDaoImpl.class.getSimpleName();

    @Inject
    public AccountReaderSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbAccountReaderSensorDao());
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