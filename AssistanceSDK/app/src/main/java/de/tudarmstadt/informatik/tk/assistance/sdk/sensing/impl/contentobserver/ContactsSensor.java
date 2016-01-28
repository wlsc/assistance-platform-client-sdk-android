package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactEmailSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractContentObserverSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class ContactsSensor extends AbstractContentObserverSensor {

    private static final String TAG = ContactsSensor.class.getSimpleName();

    private static ContactsSensor INSTANCE;

    private static final Uri URI_EMAIL = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private static final Uri URI_DATA = Data.CONTENT_URI;
    private static final Uri URI_PHONE = Phone.CONTENT_URI;
    private static final Uri URI_RAW_CONTACTS = ContactsContract.RawContacts.CONTENT_URI;
    private static final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;

    private Handler syncingTask;

    private ContactsSensor(Context context) {
        super(context);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static ContactsSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ContactsSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public int getType() {
        return SensorApiType.CONTACT;
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

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

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
            Map<Long, DbContactSensor> allExistingContacts = getAllExistingContacts();

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
                DbContactSensor sensorContact = new DbContactSensor();

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
                sensorContact.setIsUpdated(Boolean.TRUE);
                sensorContact.setCreated(created);
                sensorContact.setDeviceId(deviceId);

                if (checkForContactChange(allExistingContacts, sensorContact)) {

                    Log.d(TAG, "Insert entry");
                    daoProvider.getContactSensorDao().insert(sensorContact);
                    Log.d(TAG, "Finished");
                }

                // get extra data
                new Handler(Looper.getMainLooper()).post(() -> {
                    syncNumbers(sensorContact);
                });

                new Handler(Looper.getMainLooper()).post(() -> {
                    syncMails(sensorContact);
                });
            }

            // this deletes implicitly all numbers and mails which have no contact anymore
            for (Map.Entry<Long, DbContactSensor> entry : allExistingContacts.entrySet()) {

                if (!isRunning()) {
                    return;
                }

                DbContactSensor contact = entry.getValue();

                new Handler(Looper.getMainLooper()).post(() -> {
                    syncNumbers(contact);
                });

                new Handler(Looper.getMainLooper()).post(() -> {
                    syncMails(contact);
                });
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

    private boolean deleteRemainingEntries(Map<Long, DbContactSensor> allExistingContacts, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactSensor> entriesToDelete = new ArrayList<>();

        for (Map.Entry<Long, DbContactSensor> entry : allExistingContacts.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactSensor dbContact = entry.getValue();

            dbContact.setIsDeleted(Boolean.TRUE);
            dbContact.setIsNew(Boolean.FALSE);
            dbContact.setIsUpdated(Boolean.TRUE);

            entriesToDelete.add(dbContact);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // remove entries
        daoProvider.getContactSensorDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private boolean deleteRemainingEmailEntries(Map<String, DbContactEmailSensor> allExistingContactsEmail, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactEmailSensor> entriesToDelete = new ArrayList<>();

        for (Map.Entry<String, DbContactEmailSensor> entry : allExistingContactsEmail.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactEmailSensor dbContactEmail = entry.getValue();

            dbContactEmail.setIsDeleted(Boolean.TRUE);
            dbContactEmail.setIsNew(Boolean.FALSE);
            dbContactEmail.setIsUpdated(Boolean.TRUE);

            entriesToDelete.add(dbContactEmail);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // delete entries
        daoProvider.getContactEmailSensorDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private boolean deleteRemainingNumberEntries(Map<String, DbContactNumberSensor> allExistingContactsNumber, boolean b) {

        boolean bSomethingDeleted = false;

        List<DbContactNumberSensor> entriesToDelete = new ArrayList<>();

        for (Map.Entry<String, DbContactNumberSensor> entry : allExistingContactsNumber.entrySet()) {

            if (b && !isRunning()) {
                return bSomethingDeleted;
            }

            DbContactNumberSensor dbContactNumber = entry.getValue();

            dbContactNumber.setIsDeleted(Boolean.TRUE);
            dbContactNumber.setIsNew(Boolean.FALSE);
            dbContactNumber.setIsUpdated(Boolean.TRUE);

            entriesToDelete.add(dbContactNumber);

            if (!bSomethingDeleted) {
                bSomethingDeleted = true;
            }
        }

        // delete entries
        daoProvider.getContactNumberSensorDao().delete(entriesToDelete);

        return bSomethingDeleted;
    }

    private void syncMails(DbContactSensor sensorContact) {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();
        long longContactId = sensorContact.getContactId();
        Map<String, DbContactEmailSensor> mapExistingMails = getExistingMails(longContactId);

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

            List<DbContactEmailSensor> entriesToInsert = new ArrayList<>();

            while (emails.moveToNext()) {

                DbContactEmailSensor sensorContactMail = new DbContactEmailSensor();

                sensorContactMail.setMailId(getLongByColumnName(emails, BaseColumns._ID));
                sensorContactMail.setContactId(sensorContact.getId());
                sensorContactMail.setAddress(getStringByColumnName(emails, Email.ADDRESS));
                sensorContactMail.setType(getStringByColumnName(emails, Email.TYPE));
                sensorContactMail.setIsNew(Boolean.TRUE);
                sensorContactMail.setIsDeleted(Boolean.FALSE);
                sensorContactMail.setIsUpdated(Boolean.TRUE);
                sensorContactMail.setCreated(created);
                sensorContactMail.setDeviceId(deviceId);

                if (checkForContactMailChange(mapExistingMails, sensorContactMail)) {

                    entriesToInsert.add(sensorContactMail);
                }
            }

            Log.d(TAG, "Contact email: Insert entry");
            daoProvider.getContactEmailSensorDao().insert(entriesToInsert);
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
            Map<String, DbContactEmailSensor> map,
            DbContactEmailSensor newItem) {

        String id = newItem.getAddress();

        DbContactEmailSensor existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.TRUE);
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
            DbContactEmailSensor existingMail,
            DbContactEmailSensor newMail) {

        return checkForDifference(existingMail.getAddress(), newMail.getAddress()) ||
                checkForDifference(existingMail.getType(), newMail.getType());
    }

    private void syncNumbers(DbContactSensor sensorContact) {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();
        long longContactId = sensorContact.getGlobalContactId();
        Map<String, DbContactNumberSensor> mapExistingNumbers = getExistingNumbers(longContactId);

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

            List<DbContactNumberSensor> entriesToInsert = new ArrayList<>();

            while (curPhones.moveToNext()) {

                DbContactNumberSensor sensorContactNumber = new DbContactNumberSensor();

                sensorContactNumber.setNumberId(getLongByColumnName(curPhones, BaseColumns._ID));
                sensorContactNumber.setContactId(sensorContact.getId());
                sensorContactNumber.setNumber(getStringByColumnName(curPhones, Phone.NUMBER));
                sensorContactNumber.setType(getStringByColumnName(curPhones, Phone.TYPE));
                sensorContactNumber.setIsNew(Boolean.TRUE);
                sensorContactNumber.setIsDeleted(Boolean.FALSE);
                sensorContactNumber.setIsUpdated(Boolean.TRUE);
                sensorContactNumber.setCreated(created);
                sensorContactNumber.setDeviceId(deviceId);

                if (checkForContactNumberChange(mapExistingNumbers, sensorContactNumber)) {

                    entriesToInsert.add(sensorContactNumber);
                }
            }

            Log.d(TAG, "Contact number: Insert entry");
            daoProvider.getContactNumberSensorDao().insert(entriesToInsert);
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
            Map<String, DbContactNumberSensor> map,
            DbContactNumberSensor newItem) {

        String id = newItem.getNumber();

        DbContactNumberSensor existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.TRUE);
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

    private boolean hasContactNumberDifference(DbContactNumberSensor existingNumber,
                                               DbContactNumberSensor newSensorContactNumber) {

        return checkForDifference(existingNumber.getNumber(), newSensorContactNumber.getNumber()) ||
                checkForDifference(existingNumber.getType(), newSensorContactNumber.getType());

    }

    private boolean checkForContactChange(Map<Long, DbContactSensor> map, DbContactSensor newItem) {

        long id = newItem.getContactId();
        DbContactSensor existingReminder = map.get(id);

        boolean result = false;

        DbContactSensor existingItem = map.remove(id);

        if (existingItem == null) {

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.TRUE);
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

    private boolean hasContactDifference(DbContactSensor existingReminder, DbContactSensor newSensorContact) {

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

    private Map<Long, DbContactSensor> getAllExistingContacts() {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        Map<Long, DbContactSensor> result = new HashMap<>();

        List<DbContactSensor> allContacts = daoProvider.getContactSensorDao().getAll(deviceId);

        for (DbContactSensor event : allContacts) {
            result.put(event.getGlobalContactId(), event);
        }

        return result;
    }

    private Map<String, DbContactNumberSensor> getExistingNumbers(long contactId) {

        List<DbContactNumberSensor> list = daoProvider
                .getContactNumberSensorDao()
                .getAll(contactId);

        Map<String, DbContactNumberSensor> map = new HashMap<>();

        for (DbContactNumberSensor number : list) {
            map.put(number.getNumber(), number);
        }

        return map;
    }

    private Map<String, DbContactEmailSensor> getExistingMails(long contactId) {

        List<DbContactEmailSensor> contactMails = daoProvider
                .getContactEmailSensorDao()
                .getAll(contactId);

        Map<String, DbContactEmailSensor> map = new HashMap<>();

        for (DbContactEmailSensor mail : contactMails) {
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

        syncingTask = new Handler(Looper.getMainLooper());

        syncingTask.post(() -> {

            syncData();
            context.getContentResolver().registerContentObserver(URI_CONTACTS, true, mObserver);
        });

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
    public void updateSensorInterval(Double collectionInterval) {

    }

    @Override
    public void reset() {

    }
}