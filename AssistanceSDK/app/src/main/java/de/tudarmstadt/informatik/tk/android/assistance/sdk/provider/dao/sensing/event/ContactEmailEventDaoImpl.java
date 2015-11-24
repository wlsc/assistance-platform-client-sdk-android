package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.ContactEmailEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactEmailEventDaoImpl extends
        CommonEventDaoImpl<DbContactEmailEvent> implements
        ContactEmailEventDao {

    private static final String TAG = ContactEmailEventDaoImpl.class.getSimpleName();

    private static ContactEmailEventDao INSTANCE;

    private ContactEmailEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbContactEmailEventDao());
    }

    public static ContactEmailEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ContactEmailEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbContactEmailEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ContactEmailEventDto result = new ContactEmailEventDto();

        result.setId(sensor.getId());
        result.setAddress(sensor.getAddress());
        result.setContactId(sensor.getContactId());
        result.setEventType(sensor.getType());
        result.setIsNew(sensor.getIsNew());
        result.setIsUpdated(sensor.getIsUpdated());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setType(DtoType.CONTACT_EMAIL);
        result.setTypeStr(DtoType.getApiName(DtoType.CONTACT_EMAIL));
        result.setCreated(sensor.getCreated());

        return result;
    }

    @Override
    public List<DbContactEmailEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbContactEmailEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
