package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.contact;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactNumberSensorDaoImpl extends
        CommonEventDaoImpl<DbContactNumberSensor> implements
        ContactNumberSensorDao {

    private static final String TAG = ContactNumberSensorDaoImpl.class.getSimpleName();

    private static ContactNumberSensorDao INSTANCE;

    private ContactNumberSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbContactNumberSensorDao());
    }

    public static ContactNumberSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ContactNumberSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbContactNumberSensor sensor) {

        if (sensor == null) {
            return null;
        }

//        ContactNumberEventDto result = new ContactNumberEventDto();
//
//        result.setId(sensor.getId());
//        result.setNumberId(sensor.getNumberId());
//        result.setEventType(sensor.getType());
//        result.setNumber(sensor.getNumber());
//        result.setIsNew(sensor.getIsNew());
//        result.setIsUpdated(sensor.getIsUpdated());
//        result.setIsDeleted(sensor.getIsDeleted());
//        result.setContactId(sensor.getContactId());
//        result.setType(SensorApiType.CONTACT_NUMBER);
//        result.setTypeStr(SensorApiType.getApiName(SensorApiType.CONTACT_NUMBER));
//        result.setCreated(sensor.getCreated());

        return null;
    }

    @Nullable
    @Override
    public DbContactNumberSensor get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbContactNumberSensorDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbContactNumberSensor> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbContactNumberSensorDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<DbContactNumberSensor> getAll(Long contactId) {

        if (contactId == null) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbContactNumberSensorDao.Properties.ContactId.eq(contactId))
                .build()
                .list();
    }
}