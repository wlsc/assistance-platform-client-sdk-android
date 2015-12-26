package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.contact.ContactEmailNumberItemDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.contact.ContactEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactEventDaoImpl extends
        CommonEventDaoImpl<DbContactEvent> implements
        ContactEventDao {

    private static final String TAG = ContactEventDaoImpl.class.getSimpleName();

    private static ContactEventDao INSTANCE;

    private ContactEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbContactEventDao());
    }

    public static ContactEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new ContactEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbContactEvent sensor) {

        if (sensor == null) {
            return null;
        }

        ContactEventDto result = new ContactEventDto();

        result.setId(sensor.getId());
        result.setGlobalContactId(sensor.getGlobalContactId());
        result.setDisplayName(sensor.getDisplayName());
        result.setGivenName(sensor.getGivenName());
        result.setFamilyName(sensor.getFamilyName());
        result.setStarred(sensor.getStarred());
        result.setLastTimeContacted(sensor.getLastTimeContacted());
        result.setTimesContacted(sensor.getTimesContacted());
        result.setNote(sensor.getNote());
        result.setIsDeleted(sensor.getIsDeleted());
        result.setCreated(sensor.getCreated());
        result.setType(DtoType.CONTACT);
        result.setTypeStr(DtoType.getApiName(DtoType.CONTACT));

        List<DbContactEmailEvent> dbEmails = sensor.getDbContactEmailEventList();
        List<DbContactNumberEvent> dbNumbers = sensor.getDbContactNumberEventList();

        if (dbEmails != null && !dbEmails.isEmpty()) {

            Set<ContactEmailNumberItemDto> emailsDto = new HashSet<>(dbEmails.size());

            for (DbContactEmailEvent emailEvent : dbEmails) {

                ContactEmailNumberItemDto emailDto = new ContactEmailNumberItemDto(
                        emailEvent.getType(),
                        emailEvent.getAddress());

                emailsDto.add(emailDto);
            }

            result.setEmailAddresses(emailsDto);
        }

        if (dbNumbers != null && !dbNumbers.isEmpty()) {

            Set<ContactEmailNumberItemDto> numbersDto = new HashSet<>(dbNumbers.size());

            for (DbContactNumberEvent numberEvent : dbNumbers) {

                ContactEmailNumberItemDto emailDto = new ContactEmailNumberItemDto(
                        numberEvent.getType(),
                        numberEvent.getNumber());

                numbersDto.add(emailDto);
            }

            result.setPhoneNumbers(numbersDto);
        }

        return result;
    }

    @Override
    public DbContactEvent get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbContactEventDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
    }

    @Override
    public List<DbContactEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbContactEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}