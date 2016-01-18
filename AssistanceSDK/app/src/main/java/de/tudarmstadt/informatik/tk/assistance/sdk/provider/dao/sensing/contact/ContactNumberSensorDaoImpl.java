package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.Property;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

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

    @Override
    public List<DbContactNumberSensor> getAll(Long contactId, long deviceId) {

        if (contactId == null || deviceId < 0) {
            return Collections.emptyList();
        }

        for (Property property : properties) {
            if (property.name.equals(DEVICE_ID_FIELD_NAME)) {
                return dao
                        .queryBuilder()
                        .where(DbContactNumberSensorDao.Properties.ContactId.eq(contactId))
                        .where(property.eq(deviceId))
                        .build()
                        .list();
            }
        }

        return Collections.emptyList();
    }
}