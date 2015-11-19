package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbMobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.event.MobileConnectionEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class MobileConnectionEventDaoImpl extends
        CommonEventDaoImpl implements
        MobileConnectionEventDao {

    private static final String TAG = MobileConnectionEventDaoImpl.class.getSimpleName();

    private static MobileConnectionEventDao INSTANCE;

    private DbMobileConnectionEventDao dao;

    private MobileConnectionEventDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbMobileConnectionEventDao();
        }
    }

    public static MobileConnectionEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new MobileConnectionEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public MobileConnectionEventDto convertObject(DbMobileConnectionEvent sensor) {

        if (sensor == null) {
            return null;
        }

        MobileConnectionEventDto result = new MobileConnectionEventDto();

        result.setId(sensor.getId());
        result.setCarrierName(sensor.getCarrierName());
        result.setMobileCarrierCode(sensor.getMobileCarrierCode());
        result.setMobileNetworkCode(sensor.getMobileNetworkCode());
        result.setVoipAvailable(sensor.getVoipAvailable());
        result.setType(DtoType.MOBILE_DATA_CONNECTION);
        result.setTypeStr(DtoType.getApiName(DtoType.MOBILE_DATA_CONNECTION));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (DbMobileConnectionEvent dbSensor : (List<DbMobileConnectionEvent>) dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

    @Override
    public List<? extends IDbSensor> getAll() {
        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<? extends IDbSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }

        return dao
                .queryBuilder()
                .orderDesc(DbMobileConnectionEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(IDbSensor sensor) {

        if (sensor == null) {
            return -1l;
        }

        Log.d(TAG, "Dumping MOBILE CONNECTION data to db...");

        long result = dao.insertOrReplace((DbMobileConnectionEvent) sensor);

        Log.d(TAG, "Finished dumping data");

        return result;
    }

    @Override
    public void delete(List<? extends IDbSensor> events) {

        if (events == null || events.isEmpty()) {
            return;
        }

        dao.deleteInTx((Iterable<DbMobileConnectionEvent>) events);
    }
}
