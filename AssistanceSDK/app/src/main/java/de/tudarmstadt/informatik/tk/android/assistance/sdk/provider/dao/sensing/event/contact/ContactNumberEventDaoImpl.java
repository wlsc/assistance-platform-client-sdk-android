package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.contact;

import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactNumberEventDaoImpl extends
        CommonEventDaoImpl<DbContactNumberEvent> implements
        ContactNumberEventDao {

    private static final String TAG = ContactNumberEventDaoImpl.class.getSimpleName();

    private static ContactNumberEventDao INSTANCE;

    private ContactNumberEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbContactNumberEventDao());
    }

    public static ContactNumberEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ContactNumberEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbContactNumberEvent sensor) {

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
//        result.setType(DtoType.CONTACT_NUMBER);
//        result.setTypeStr(DtoType.getApiName(DtoType.CONTACT_NUMBER));
//        result.setCreated(sensor.getCreated());

        return null;
    }

    @Nullable
    @Override
    public DbContactNumberEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbContactNumberEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbContactNumberEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbContactNumberEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public List<DbContactNumberEvent> getAll(Long contactId) {

        if (contactId == null) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbContactNumberEventDao.Properties.ContactId.eq(contactId))
                .build()
                .list();
    }
}
