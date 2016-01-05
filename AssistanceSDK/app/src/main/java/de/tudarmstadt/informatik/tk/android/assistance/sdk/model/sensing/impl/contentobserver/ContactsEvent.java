package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactsEvent extends AbstractContentObserverEvent {

    private static final String TAG = ContactsEvent.class.getSimpleName();

    private static ContactsEvent INSTANCE;

    private static final Uri URI_EMAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private static final Uri URI_DATA = Data.CONTENT_URI;
    private static final Uri URI_PHONE = Phone.CONTENT_URI;
    private static final Uri URI_RAW_CONTACTS = ContactsContract.RawContacts.CONTENT_URI;
    private static final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;

    @Nullable
    private AsyncTask<Void, Void, Void> syncingTask;

    private ContactsEvent(Context context) {
        super(context);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static ContactsEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ContactsEvent(context);
        }

        return INSTANCE;
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

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Permission was NOT granted!");
            setRunning(false);

            return;
        }

        //Cursor cursor = context.getContentResolver().query(URI_RAW_CONTACTS, null, "deleted=?", new String[] { "0" }, null);

        Cursor cursor = null;
        Cursor nameCur = null;

        try {

            cursor = context.getContentResolver()
                    .query(URI_CONTACTS, null, Data.IN_VISIBLE_GROUP + " = 1", null, null);

            if (cursor == null) {
                return;
            }

            String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());
            Map<Long, DbContactEvent> allExistingContacts = getAllExistingContacts();

            while (cursor.moveToNext() && isRunning()) {

                String strContactId = getStringByColumnName(cursor, BaseColumns._ID);

                Log.d(TAG, "sync Contact Id: " + strContactId);

                String strGivenName = null;
                String strFamilyName = null;

                String[] projectionNameParams = {
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME};
                String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + Data.CONTACT_ID + " = ?";
                String[] whereNameParams = {
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                        strContactId};


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

                // Fill database object
                DbContactEvent sensorContact = new DbContactEvent();

                sensorContact.setContactId(Long.valueOf(strContactId));
                sensorContact.setGlobalContactId(Long.valueOf(strContactId));
                sensorContact.setDisplayName(getStringByColumnName(cursor, Data.DISPLAY_NAME_PRIMARY));
                sensorContact.setGivenName(strGivenName);
                sensorContact.setFamilyName(strFamilyName);
                sensorContact.setStarred(getIntByColumnName(cursor, Data.STARRED));
                sensorContact.setLastTimeContacted(getIntByColumnName(cursor, Data.LAST_TIME_CONTACTED));
                sensorContact.setTimesContacted(getIntByColumnName(cursor, Data.TIMES_CONTACTED));
                sensorContact.setNote(getNote(strContactId));
                sensorContact.setIsNew(Boolean.TRUE);
                sensorContact.setIsDeleted(Boolean.FALSE);
                sensorContact.setIsUpdated(Boolean.FALSE);
                sensorContact.setCreated(created);

                if (checkForContactChange(allExistingContacts, sensorContact)) {

                    Log.d(TAG, "Insert entry");
                    daoProvider.getContactEventDao().insert(sensorContact);
                    Log.d(TAG, "Finished");
                }

                // get extra data
                syncNumbers(sensorContact);
                syncMails(sensorContact);
            }


            // this deletes implicitly all numbers and mails which have no contact anymore
            for (Map.Entry<Long, DbContactEvent> entry : allExistingContacts.entrySet()) {

                if (!isRunning()) {
                    return;
                }

                DbContactEvent contact = entry.getValue();

                syncNumbers(contact);
                syncMails(contact);
            }

            // remaining contacts are deleted
            deleteRemainingEntries(allExistingContacts, true);

        } catch (SecurityException se) {
            Log.d(TAG, "Permission was not granted for this event!");
        } catch (NumberFormatException e) {
            Log.d(TAG, "Number format exception", e);
        } catch (Exception e) {
            Log.e(TAG, "Some error:", e);
        } finally {

            if (cursor != null) {
                cursor.close();
            }

            if (nameCur != null) {
                nameCur.close();
            }
        }
    }

    private boolean deleteRemainingEntries(Map<Long, DbContactEvent> allExistingContacts, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactEvent> entriesToDelete = new ArrayList<>();

        for (Map.Entry<Long, DbContactEvent> entry : allExistingContacts.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactEvent dbContact = entry.getValue();

            dbContact.setIsDeleted(Boolean.TRUE);
            dbContact.setIsNew(Boolean.FALSE);
            dbContact.setIsUpdated(Boolean.FALSE);

            entriesToDelete.add(dbContact);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // remove entries
        daoProvider.getContactEventDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private boolean deleteRemainingEmailEntries(Map<String, DbContactEmailEvent> allExistingContactsEmail, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactEmailEvent> entriesToDelete = new ArrayList<>();

        for (Map.Entry<String, DbContactEmailEvent> entry : allExistingContactsEmail.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactEmailEvent dbContactEmail = entry.getValue();

            dbContactEmail.setIsDeleted(Boolean.TRUE);
            dbContactEmail.setIsNew(Boolean.FALSE);
            dbContactEmail.setIsUpdated(Boolean.FALSE);

            entriesToDelete.add(dbContactEmail);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // delete entries
        daoProvider.getContactEmailEventDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private boolean deleteRemainingNumberEntries(Map<String, DbContactNumberEvent> allExistingContactsNumber, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactNumberEvent> entriesToDelete = new ArrayList<>();

        for (Map.Entry<String, DbContactNumberEvent> entry : allExistingContactsNumber.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactNumberEvent dbContactNumber = entry.getValue();

            dbContactNumber.setIsDeleted(Boolean.TRUE);
            dbContactNumber.setIsNew(Boolean.FALSE);
            dbContactNumber.setIsUpdated(Boolean.FALSE);

            entriesToDelete.add(dbContactNumber);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // delete entries
        daoProvider.getContactNumberEventDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private void syncMails(DbContactEvent sensorContact) {

        long longContactId = sensorContact.getContactId();
        Map<String, DbContactEmailEvent> mapExistingMails = getExistingMails(longContactId);

        String[] columns = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.TYPE
        };

        Cursor emails = null;

        try {

            emails = context
                    .getContentResolver()
                    .query(URI_EMAIL,
                            columns,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + longContactId,
                            null,
                            null);

            if (emails == null) {
                return;
            }

            String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

            List<DbContactEmailEvent> entriesToInsert = new ArrayList<>();

            while (emails.moveToNext()) {

                DbContactEmailEvent sensorContactMail = new DbContactEmailEvent();

                sensorContactMail.setMailId(getLongByColumnName(emails, BaseColumns._ID));
                sensorContactMail.setContactId(sensorContact.getId());
                sensorContactMail.setAddress(getStringByColumnName(emails, Email.ADDRESS));
                sensorContactMail.setType(getStringByColumnName(emails, Email.TYPE));
                sensorContactMail.setIsNew(Boolean.TRUE);
                sensorContactMail.setIsDeleted(Boolean.FALSE);
                sensorContactMail.setIsUpdated(Boolean.FALSE);
                sensorContactMail.setCreated(created);

                if (checkForContactMailChange(mapExistingMails, sensorContactMail)) {

                    entriesToInsert.add(sensorContactMail);
                }
            }

            Log.d(TAG, "Contact email: Insert entry");
            daoProvider.getContactEmailEventDao().insert(entriesToInsert);
            Log.d(TAG, "Finished");

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

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.FALSE);
            newItem.setIsDeleted(Boolean.FALSE);

            return true;

        } else {
            if (hasContactMailDifference(existingItem, newItem)) {

                newItem.setIsNew(Boolean.FALSE);
                newItem.setIsUpdated(Boolean.TRUE);
                newItem.setIsDeleted(Boolean.FALSE);
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
                    .query(URI_PHONE,
                            null,
                            Phone.CONTACT_ID + " = " + longContactId,
                            null,
                            null);

            if (curPhones == null) {
                return;
            }

            String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

            List<DbContactNumberEvent> entriesToInsert = new ArrayList<>();

            while (curPhones.moveToNext()) {

                DbContactNumberEvent sensorContactNumber = new DbContactNumberEvent();

                sensorContactNumber.setNumberId(getLongByColumnName(curPhones, BaseColumns._ID));
                sensorContactNumber.setContactId(sensorContact.getId());
                sensorContactNumber.setNumber(getStringByColumnName(curPhones, Phone.NUMBER));
                sensorContactNumber.setType(getStringByColumnName(curPhones, Phone.TYPE));
                sensorContactNumber.setIsNew(Boolean.TRUE);
                sensorContactNumber.setIsDeleted(Boolean.FALSE);
                sensorContactNumber.setIsUpdated(Boolean.FALSE);
                sensorContactNumber.setCreated(created);

                if (checkForContactNumberChange(mapExistingNumbers, sensorContactNumber)) {

                    entriesToInsert.add(sensorContactNumber);
                }
            }

            Log.d(TAG, "Contact number: Insert entry");
            daoProvider.getContactNumberEventDao().insert(entriesToInsert);
            Log.d(TAG, "Finished");

        } catch (Exception e) {
            Log.e(TAG, "Some error:", e);
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

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.FALSE);
            newItem.setIsDeleted(Boolean.FALSE);

            return true;

        } else {
            if (hasContactNumberDifference(existingItem, newItem)) {

                newItem.setIsNew(Boolean.FALSE);
                newItem.setIsUpdated(Boolean.TRUE);
                newItem.setIsDeleted(Boolean.FALSE);
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

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.FALSE);
            newItem.setIsDeleted(Boolean.FALSE);

            result = true;

        } else {
            if (hasContactDifference(existingItem, newItem)) {

                newItem.setIsNew(Boolean.FALSE);
                newItem.setIsUpdated(Boolean.TRUE);
                newItem.setIsDeleted(Boolean.FALSE);
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
                .getAll(contactId);

        Map<String, DbContactNumberEvent> map = new HashMap<>();

        for (DbContactNumberEvent number : list) {
            map.put(number.getNumber(), number);
        }

        return map;
    }

    private Map<String, DbContactEmailEvent> getExistingMails(long contactId) {

        List<DbContactEmailEvent> contactMails = daoProvider
                .getContactEmailEventDao()
                .getAll(contactId);

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

            @Nullable
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