package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPowerStateEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.PowerStateEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateEventDaoImpl extends
        CommonEventDaoImpl<DbPowerStateEvent> implements
        PowerStateEventDao {

    private static final String TAG = PowerStateEventDaoImpl.class.getSimpleName();

    private static PowerStateEventDao INSTANCE;

    private PowerStateEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbPowerStateEventDao());
    }

    public static PowerStateEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new PowerStateEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public PowerStateEventDto convertObject(DbPowerStateEvent dbSensor) {

        if (dbSensor == null) {
            return null;
        }

        PowerStateEventDto result = new PowerStateEventDto();

        result.setIsCharging(dbSensor.getIsCharging());
        result.setPercent(dbSensor.getPercent());
        result.setChargingState(dbSensor.getChargingState());
        result.setChargingMode(dbSensor.getChargingMode());
        result.setPowerSaveMode(dbSensor.getPowerSaveMode());
        result.setType(DtoType.POWER_STATE);
        result.setTypeStr(DtoType.getApiName(DtoType.POWER_STATE));
        result.setCreated(dbSensor.getCreated());

        return result;
    }

    @Override
    public List<DbPowerStateEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbPowerStateEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
