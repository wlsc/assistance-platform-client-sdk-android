package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactsEvent extends AbstractContentObserverEvent {

    private static final String TAG = ContactsEvent.class.getSimpleName();

    private static final Uri URI_EMAIL = Email.CONTENT_URI;
    private static final Uri URI_DATA = Data.CONTENT_URI;
    private static final Uri URI_PHONE = Phone.CONTENT_URI;
    private static final Uri URI_RAW_CONTACTS = ContactsContract.RawContacts.CONTENT_URI;
    private static final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;

    private AsyncTask<Void, Void, Void> syncingTask;

    public ContactsEvent(Context context) {
        super(context);
    }

    @Override
    public int getType() {
        return DtoType.CONTACT;
    }

    @Override
    protected void syncData() {

        if (context == null) {
            return;
        }

        //ContactsContract.CommonDataKinds.StructuredName.

        //Cursor cursor = context.getContentResolver().query(URI_RAW_CONTACTS, null, "deleted=?", new String[] { "0" }, null);

        Map<Long, DbContactEvent> allExistingContacts = new HashMap<>();

        Cursor cursor = null;

        try {

            cursor = context
                    .getContentResolver()
                    .query(URI_CONTACTS, null, Data.IN_VISIBLE_GROUP + " = 1", null, null);

            if (cursor == null) {
                return;
            }

            cursor.moveToFirst();

            try {
                allExistingContacts = getAllExistingContacts();

            } catch (Exception e) {
                Log.e(TAG, "Some error: ", e);
                return;
            }

            while (cursor.moveToNext() && isRunning()) {

                String strContactId = getStringByColumnName(cursor, ContactsContract.Contacts._ID);

                System.out.println("sync Contact Id: " + strContactId);

                String strGivenName = null;
                String strFamilyName = null;

                String[] projectionNameParams = new String[]{
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME};
                String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + Data.CONTACT_ID + " = ?";
                String[] whereNameParams = new String[]{
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                        strContactId};

                Cursor nameCur = null;

                try {

                    nameCur = context.getContentResolver().query(URI_DATA, projectionNameParams, whereName, whereNameParams, null);

                    if (nameCur != null) {

                        if (nameCur.moveToFirst()) {

                            strGivenName = getStringByColumnName(
                                    nameCur,
                                    ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                            strFamilyName = getStringByColumnName(
                                    nameCur,
                                    ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Some error: ", e);
                } finally {
                    if (nameCur != null) {
                        nameCur.close();
                    }
                }

                // Fill database object
                DbContactEvent sensorContact = new DbContactEvent();

                sensorContact.setContactId(Long.valueOf(strContactId));
//                sensorContact.setGlobalContactId(KrakenUtils.getGlobalId(context, Long.valueOf(strContactId)));
                sensorContact.setDisplayName(getStringByColumnName(cursor, Data.DISPLAY_NAME_PRIMARY));
                sensorContact.setGivenName(strGivenName);
                sensorContact.setFamilyName(strFamilyName);
                sensorContact.setStarred(getIntByColumnName(cursor, Data.STARRED));
                sensorContact.setLastTimeContacted(getIntByColumnName(cursor, Data.LAST_TIME_CONTACTED));
                sensorContact.setTimesContacted(getIntByColumnName(cursor, Data.TIMES_CONTACTED));
                sensorContact.setNote(getNote(strContactId));
                sensorContact.setIsNew(true);
                sensorContact.setIsDeleted(false);
                sensorContact.setIsUpdated(false);

                try {
                    if (checkForContactChange(allExistingContacts, sensorContact)) {
                        daoProvider.getContactEventDao().insert(sensorContact);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Some error: ", e);
                }

                // get extra data
                syncNumbers(sensorContact);
                syncMails(sensorContact);

            }
        } catch (NullPointerException npe) {
            Log.d(TAG, "NPE in cursor");
        } catch (Exception e) {
            Log.e(TAG, "Some error:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // this deletes implicitly all numbers and mails which have no contact anymore
        for (DbContactEvent contact : allExistingContacts.values()) {

            if (!isRunning()) {
                return;
            }

            syncNumbers(contact);
            syncMails(contact);
        }
        // remaining contacts are deleted
        deleteRemainingEntries(allExistingContacts, true);
    }

    private boolean deleteRemainingEntries(Map<Long, DbContactEvent> allExistingContacts, boolean b) {

        boolean bSomethingDeleted = false;

        for (DbContactEvent entry : allExistingContacts.values()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            entry.setIsDeleted(true);
            entry.setIsNew(false);
            entry.setIsUpdated(false);

            daoProvider.getContactEventDao().delete(entry);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        return bSomethingDeleted;
    }

    private boolean deleteRemainingEmailEntries(Map<String, DbContactEmailEvent> allExistingContactsEmail, boolean b) {

        boolean bSomethingDeleted = false;

        for (DbContactEmailEvent entry : allExistingContactsEmail.values()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            entry.setIsDeleted(true);
            entry.setIsNew(false);
            entry.setIsUpdated(false);

            daoProvider.getContactEmailEventDao().delete(entry);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        return bSomethingDeleted;
    }

    private boolean deleteRemainingNumberEntries(Map<String, DbContactNumberEvent> allExistingContactsNumber, boolean b) {

        boolean bSomethingDeleted = false;

        for (DbContactNumberEvent entry : allExistingContactsNumber.values()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            entry.setIsDeleted(true);
            entry.setIsNew(false);
            entry.setIsUpdated(false);

            daoProvider.getContactNumberEventDao().delete(entry);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        return bSomethingDeleted;
    }

    private void syncMails(DbContactEvent sensorContact) {

        long longContactId = sensorContact.getContactId();
        Map<String, DbContactEmailEvent> mapExistingMails = getExistingMails(longContactId);

        String[] columns = new String[]{Email.ADDRESS, Email.TYPE};

        Cursor emails = null;

        try {

            emails = context
                    .getContentResolver()
                    .query(URI_EMAIL, columns, Email.CONTACT_ID + " = " + longContactId, null, null);

            if (emails == null) {
                return;
            }

            while (emails.moveToNext()) {

                DbContactEmailEvent sensorContactMail = new DbContactEmailEvent();

                sensorContactMail.setMailId(getLongByColumnName(emails, Email._ID));
                sensorContactMail.setContactId(sensorContact.getId());
                sensorContactMail.setAddress(getStringByColumnName(emails, Email.ADDRESS));
                sensorContactMail.setType(getStringByColumnName(emails, Email.TYPE));
                sensorContactMail.setIsNew(true);
                sensorContactMail.setIsDeleted(false);
                sensorContactMail.setIsUpdated(false);

                try {
                    if (checkForContactMailChange(mapExistingMails, sensorContactMail)) {
                        daoProvider.getContactEmailEventDao().insert(sensorContactMail);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Some error: ", e);
                }
            }
        } finally {
            if (emails != null) {
                emails.close();
            }
        }

        // remaining mails are deleted
        deleteRemainingEmailEntries(mapExistingMails, false);
    }

    private boolean checkForContactMailChange(
            Map<String, DbContactEmailEvent> map,
            DbContactEmailEvent newItem) {

        String id = newItem.getAddress();

        DbContactEmailEvent existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(true);
            newItem.setIsUpdated(false);
            newItem.setIsDeleted(false);

            return true;

        } else {
            if (hasContactMailDifference(existingItem, newItem)) {

                newItem.setIsNew(false);
                newItem.setIsUpdated(true);
                newItem.setIsDeleted(false);
                newItem.setId(existingItem.getId());

                return true;
            }
        }

        return false;
    }

    private boolean hasContactMailDifference(
            DbContactEmailEvent existingMail,
            DbContactEmailEvent newMail) {

        return checkForDifference(existingMail.getAddress(), newMail.getAddress()) ||
                checkForDifference(existingMail.getType(), newMail.getType());
    }

    private void syncNumbers(DbContactEvent sensorContact) {

        long longContactId = sensorContact.getGlobalContactId();
        Map<String, DbContactNumberEvent> mapExistingNumbers = getExistingNumbers(longContactId);

        Cursor curPhones = null;

        try {

            curPhones = context
                    .getContentResolver()
                    .query(URI_PHONE, null, Phone.CONTACT_ID + " = " + longContactId, null, null);

            if (curPhones == null) {
                return;
            }

            while (curPhones.moveToNext()) {

                DbContactNumberEvent sensorContactNumber = new DbContactNumberEvent();

                sensorContactNumber.setNumberId(getLongByColumnName(curPhones, Phone._ID));
                sensorContactNumber.setContactId(sensorContact.getId());
                sensorContactNumber.setNumber(getStringByColumnName(curPhones, Phone.NUMBER));
                sensorContactNumber.setType(getStringByColumnName(curPhones, Phone.TYPE));
                sensorContactNumber.setIsNew(true);
                sensorContactNumber.setIsDeleted(false);
                sensorContactNumber.setIsUpdated(false);

                try {
                    if (checkForContactNumberChange(mapExistingNumbers, sensorContactNumber)) {
                        daoProvider.getContactNumberEventDao().insert(sensorContactNumber);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Some error: ", e);
                }
            }
        } catch (NullPointerException npe) {
            Log.d(TAG, "");
        } finally {
            if (curPhones != null) {
                curPhones.close();
            }
        }

        // remaining numbers are deleted
        deleteRemainingNumberEntries(mapExistingNumbers, false);
    }

    private boolean checkForContactNumberChange(
            Map<String, DbContactNumberEvent> map,
            DbContactNumberEvent newItem) {

        String id = newItem.getNumber();

        DbContactNumberEvent existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(true);
            newItem.setIsUpdated(false);
            newItem.setIsDeleted(false);

            return true;

        } else {
            if (hasContactNumberDifference(existingItem, newItem)) {

                newItem.setIsNew(false);
                newItem.setIsUpdated(true);
                newItem.setIsDeleted(false);
                newItem.setId(existingItem.getId());

                return true;
            }
        }

        return false;
    }

    private boolean hasContactNumberDifference(DbContactNumberEvent existingNumber,
                                               DbContactNumberEvent newSensorContactNumber) {

        return checkForDifference(existingNumber.getNumber(), newSensorContactNumber.getNumber()) ||
                checkForDifference(existingNumber.getType(), newSensorContactNumber.getType());

    }

    private boolean checkForContactChange(Map<Long, DbContactEvent> map, DbContactEvent newItem) {
        
        long id = newItem.getContactId();
        DbContactEvent existingReminder = map.get(id);

        boolean result = false;

        DbContactEvent existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(true);
            newItem.setIsUpdated(false);
            newItem.setIsDeleted(false);

            result = true;

        } else {
            if (hasContactDifference(existingItem, newItem)) {

                newItem.setIsNew(false);
                newItem.setIsUpdated(true);
                newItem.setIsDeleted(false);
                newItem.setId(existingItem.getId());

                result = true;
            }
        }

        if (!result) {
            newItem.setId(existingReminder.getId());
        }

        return result;
    }

    private boolean hasContactDifference(DbContactEvent existingReminder, DbContactEvent newSensorContact) {

        if (checkForDifference(existingReminder.getDisplayName(), newSensorContact.getDisplayName()))
            return true;
        if (checkForDifference(existingReminder.getGivenName(), newSensorContact.getGivenName()))
            return true;
        if (checkForDifference(existingReminder.getFamilyName(), newSensorContact.getFamilyName()))
            return true;
        if (checkForDifference(existingReminder.getStarred(), newSensorContact.getStarred()))
            return true;
        if (checkForDifference(existingReminder.getLastTimeContacted(), newSensorContact.getLastTimeContacted()))
            return true;
        if (checkForDifference(existingReminder.getTimesContacted(), newSensorContact.getTimesContacted()))
            return true;
        if (checkForDifference(existingReminder.getNote(), newSensorContact.getNote()))
            return true;
        return false;
    }

    private Map<Long, DbContactEvent> getAllExistingContacts() {

        Map<Long, DbContactEvent> result = new HashMap<>();

        List<DbContactEvent> allContacts = daoProvider.getContactEventDao().getAll();

        for (DbContactEvent event : allContacts) {
            result.put(event.getGlobalContactId(), event);
        }

        return result;
    }

    private Map<String, DbContactNumberEvent> getExistingNumbers(long contactId) {

        List<DbContactNumberEvent> list = daoProvider
                .getContactNumberEventDao()
                .get(contactId);

        Map<String, DbContactNumberEvent> map = new HashMap<>();

        for (DbContactNumberEvent number : list) {
            map.put(number.getNumber(), number);
        }

        return map;
    }

    private Map<String, DbContactEmailEvent> getExistingMails(long contactId) {

        List<DbContactEmailEvent> contactMails = daoProvider
                .getContactEmailEventDao()
                .get(contactId);

        Map<String, DbContactEmailEvent> map = new HashMap<>();

        for (DbContactEmailEvent mail : contactMails) {
            map.put(mail.getAddress(), mail);
        }

        return map;
    }

    private String getNote(String contactId) {

        if (context == null) {
            return null;
        }

        String note = null;
        String[] columns = new String[]{Note.NOTE};
        String where = Data.RAW_CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ?";
        String[] whereParameters = new String[]{contactId, Note.CONTENT_ITEM_TYPE};

        Cursor contacts = null;

        try {

            contacts = context
                    .getContentResolver()
                    .query(URI_DATA, columns, where, whereParameters, null);

            if (contacts != null) {
                if (contacts.moveToFirst()) {
                    note = getStringByColumnName(contacts, Note.NOTE);
                }
            }

        } finally {
            if (contacts != null) {
                contacts.close();
            }
        }

        return note;
    }

    @Override
    public void startSensor() {

        syncingTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                syncData();
                context.getContentResolver().registerContentObserver(URI_CONTACTS, true, mObserver);

                return null;
            }
        }.execute();

        setRunning(true);
    }

    @Override
    public void stopSensor() {

        syncingTask = null;
        setRunning(false);
    }

    @Override
    public EPushType getPushType() {
        return EPushType.MANUALLY_WLAN_ONLY;
    }

    @Override
    public void dumpData() {

    }

    @Override
    public void reset() {

    }
}
