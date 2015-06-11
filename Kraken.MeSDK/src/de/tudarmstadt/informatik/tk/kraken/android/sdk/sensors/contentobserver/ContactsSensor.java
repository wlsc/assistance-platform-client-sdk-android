package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.contentobserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.EPushType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorContact;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorContactMail;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorContactNumber;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorContactMailDao;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorContactNumberDao.Properties;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractContentObserverSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.KrakenUtils;

public class ContactsSensor extends AbstractContentObserverSensor {

	private static final Uri URI_EMAIL = Email.CONTENT_URI;
	private static final Uri URI_DATA = Data.CONTENT_URI;
	private static final Uri URI_PHONE = Phone.CONTENT_URI;
	private static final Uri URI_RAW_CONTACTS = ContactsContract.RawContacts.CONTENT_URI;
	private static final Uri URI_CONTACTS = ContactsContract.Contacts.CONTENT_URI;
	
	// cache
	private Method m_checkDifferenceMethodForContactMailChange;
	private Method m_getKeyMethodForSensorContactMail;
	private Method m_checkDifferenceMethodForContactNumberChange;
	private Method m_getKeyMethodForSensorContactNumber;
	private Method m_checkDifferenceMethodForContactChange;
	private Method m_getKeyMethodForSensorContact;
	private Method m_methodForGEtAllExistingContacts;

	private boolean m_bFlushToServer;
	
	public ContactsSensor(Context context) {
		super(context);
	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.SENSOR_CONTACTS;
	}

	@Override
	protected void syncData() {

		m_bFlushToServer = false;

        //ContactsContract.CommonDataKinds.StructuredName.
	
		//Cursor cursor = context.getContentResolver().query(URI_RAW_CONTACTS, null, "deleted=?", new String[] { "0" }, null);
		Cursor cursor = context.getContentResolver().query(URI_CONTACTS, null, Data.IN_VISIBLE_GROUP + " = 1", null, null);
		cursor.moveToFirst();
		
		HashMap<Long, SensorContact> allExistingContacts;
		try {
			allExistingContacts = getAllExistingContacts();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		while (cursor.moveToNext() && m_bIsRunning) {

			String strContactId = getStringByColumnName(cursor, ContactsContract.Contacts._ID);

			System.out.println("sync Contact Id: " + strContactId);
			
			String strGivenName = null;
			String strFamilyName = null;

			String[] projectionNameParams = new String[] { StructuredName.GIVEN_NAME, StructuredName.FAMILY_NAME };
			String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + Data.CONTACT_ID + " = ?";
			String[] whereNameParams = new String[] { StructuredName.CONTENT_ITEM_TYPE, strContactId };
			Cursor nameCur = context.getContentResolver().query(URI_DATA, projectionNameParams, whereName, whereNameParams, null);
			if (nameCur.moveToFirst()) {
				strGivenName = getStringByColumnName(nameCur, StructuredName.GIVEN_NAME);
				strFamilyName = getStringByColumnName(nameCur, StructuredName.FAMILY_NAME);
			}
			nameCur.close();

			// Fill database object
			SensorContact sensorContact = new SensorContact();
			sensorContact.setContactId(Long.valueOf(strContactId));
			sensorContact.setGlobalContactId(KrakenUtils.getGlobalId(context, Long.valueOf(strContactId)));
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
				if (checkForContactChange(allExistingContacts, sensorContact))
				{
					handleDatabaseObject(sensorContact, !sensorContact.getIsNew());
					m_bFlushToServer = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// get extra data
			syncNumbers(sensorContact);
			syncMails(sensorContact);

		}
		cursor.close();

		// this deletes implicitly all numbers and mails which have no contact anymore
		for (SensorContact contact : allExistingContacts.values()) {
			if (!m_bIsRunning)
				return;
			syncNumbers(contact);
			syncMails(contact);
		}
		// remaining contacts are deleted
		if (deleteRemainingEntries(allExistingContacts, true))
			m_bFlushToServer = true;
		
		
		if (m_bFlushToServer) {
            String strFullqualifiedDatabaseClassName = getSensorType().getFullqualifiedDatabaseClassName();
            ApiMessage.DataWrapper dataContact = flushDataRetro(strFullqualifiedDatabaseClassName);
            ApiMessage.DataWrapper dataMail = flushDataRetro(strFullqualifiedDatabaseClassName + "Mail");
            ApiMessage.DataWrapper dataNumber = flushDataRetro(strFullqualifiedDatabaseClassName + "Number");
            RetroServerPushManager.getInstance(context).flushManually(getPushType(), dataContact, dataMail, dataNumber);
        }
	}

	private void syncMails(SensorContact sensorContact) {
		long longContactId = sensorContact.getContactId();
		HashMap<String, SensorContactMail> mapExistingMails = getExistingMails(longContactId);

		String[] columns = new String[] { Email.ADDRESS, Email.TYPE };
		Cursor emails = context.getContentResolver().query(URI_EMAIL, columns, Email.CONTACT_ID + " = " + longContactId, null, null);
		while (emails.moveToNext()) {
			SensorContactMail sensorContactMail = new SensorContactMail();
//			sensorContactMail.setMailId(getLongByColumnName(emails, Email._ID));
			sensorContactMail.setFkContact(sensorContact.getId());
			sensorContactMail.setAddress(getStringByColumnName(emails, Email.ADDRESS));
			sensorContactMail.setType(getStringByColumnName(emails, Email.TYPE));
			sensorContactMail.setIsNew(true);
			sensorContactMail.setIsDeleted(false);
			sensorContactMail.setIsUpdated(false);

			try {
				if (checkForContactMailChange(mapExistingMails, sensorContactMail))
				{
					handleDatabaseObject(sensorContactMail, !sensorContactMail.getIsNew(), true, false);
					m_bFlushToServer = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		emails.close();

		// remaining mails are deleted
		if (deleteRemainingEntries(mapExistingMails, false))
			m_bFlushToServer = true;
	}

	private boolean checkForContactMailChange(HashMap<String, SensorContactMail> map, SensorContactMail newSensorContactMail) throws Exception {
		try {
			if (m_checkDifferenceMethodForContactMailChange == null || m_getKeyMethodForSensorContactMail == null)
			{
				m_getKeyMethodForSensorContactMail = SensorContactMail.class.getDeclaredMethod("getMailId", new Class[]{});
				m_getKeyMethodForSensorContactMail.setAccessible(true);
				m_checkDifferenceMethodForContactMailChange = getClass().getDeclaredMethod("hasContactMailDifference", new Class[]{SensorContactMail.class, SensorContactMail.class});
				m_checkDifferenceMethodForContactMailChange.setAccessible(true);
			}
			return checkForChange(map, newSensorContactMail, m_getKeyMethodForSensorContactMail, m_checkDifferenceMethodForContactMailChange);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unused")
	private boolean hasContactMailDifference(SensorContactMail existingMail, SensorContactMail newSensorContactMail) {
		if (checkForDifference(existingMail.getAddress(), newSensorContactMail.getAddress()))
			return true;
		if (checkForDifference(existingMail.getType(), newSensorContactMail.getType()))
			return true;
		return false;
	}

	private void syncNumbers(SensorContact sensorContact) {
		long longContactId = sensorContact.getContactId();
		HashMap<Long, SensorContactNumber> mapExistingNumbers = getExistingNumbers(longContactId);

		Cursor curPhones = context.getContentResolver().query(URI_PHONE, null, Phone.CONTACT_ID + " = " + longContactId, null, null);
		while (curPhones.moveToNext()) {
			SensorContactNumber sensorContactNumber = new SensorContactNumber();
			sensorContactNumber.setNumberId(getLongByColumnName(curPhones, Phone._ID));
			sensorContactNumber.setFkContact(sensorContact.getId());
			sensorContactNumber.setNumber(getStringByColumnName(curPhones, Phone.NUMBER));
			sensorContactNumber.setType(getStringByColumnName(curPhones, Phone.TYPE));
			sensorContactNumber.setIsNew(true);
			sensorContactNumber.setIsDeleted(false);
			sensorContactNumber.setIsUpdated(false);

			try {
				if (checkForContactNumberChange(mapExistingNumbers, sensorContactNumber)) {
					handleDatabaseObject(sensorContactNumber, !sensorContactNumber.getIsNew(), true, false);
					m_bFlushToServer = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		curPhones.close();

		// remaining numbers are deleted
		if (deleteRemainingEntries(mapExistingNumbers, false))
			m_bFlushToServer = true;
	}

	private boolean checkForContactNumberChange(HashMap<Long, SensorContactNumber> map, SensorContactNumber newSensorContactNumber) throws Exception {
		try {
			if (m_checkDifferenceMethodForContactNumberChange  == null || m_getKeyMethodForSensorContactNumber == null)
			{
				m_getKeyMethodForSensorContactNumber = SensorContactNumber.class.getDeclaredMethod("getNumberId", new Class[]{});
				m_getKeyMethodForSensorContactNumber.setAccessible(true);
				m_checkDifferenceMethodForContactNumberChange = getClass().getDeclaredMethod("hasContactNumberDifference", new Class[]{SensorContactNumber.class, SensorContactNumber.class});
				m_checkDifferenceMethodForContactNumberChange.setAccessible(true);
			}
			return checkForChange(map, newSensorContactNumber, m_getKeyMethodForSensorContactNumber, m_checkDifferenceMethodForContactNumberChange);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@SuppressWarnings("unused")
	private boolean hasContactNumberDifference(SensorContactNumber existingNumber, SensorContactNumber newSensorContactNumber) {
		if (checkForDifference(existingNumber.getNumber(), newSensorContactNumber.getNumber()))
			return true;
		if (checkForDifference(existingNumber.getType(), newSensorContactNumber.getType()))
			return true;

		return false;
	}

	private boolean checkForContactChange(HashMap<Long, SensorContact> map, SensorContact newSensorContact) throws Exception {
		try {
			long id = newSensorContact.getContactId();
			SensorContact existingReminder = map.get(id);

			if (m_checkDifferenceMethodForContactChange == null || m_getKeyMethodForSensorContact == null)
			{
				m_getKeyMethodForSensorContact = SensorContact.class.getDeclaredMethod("getContactId", new Class[]{});
				m_getKeyMethodForSensorContact.setAccessible(true);
				m_checkDifferenceMethodForContactChange = getClass().getDeclaredMethod("hasContactDifference", new Class[]{SensorContact.class, SensorContact.class});
				m_checkDifferenceMethodForContactChange.setAccessible(true);
			}
			boolean result = checkForChange(map, newSensorContact, m_getKeyMethodForSensorContact, m_checkDifferenceMethodForContactChange);
			if (!result)
			{
				newSensorContact.setId(existingReminder.getId());
			}
			return result;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unused")
	private boolean hasContactDifference(SensorContact existingReminder, SensorContact newSensorContact) {
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

	@SuppressLint("UseSparseArrays")
	private HashMap<Long, SensorContact> getAllExistingContacts() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (m_methodForGEtAllExistingContacts == null)
		{
			m_methodForGEtAllExistingContacts = SensorContact.class.getDeclaredMethod("getContactId", new Class[]{});
			m_methodForGEtAllExistingContacts.setAccessible(true);
		}
		return getAllExistingEntries(SensorContact.class, m_methodForGEtAllExistingContacts);
	}

	@SuppressLint("UseSparseArrays")
	private HashMap<Long, SensorContactNumber> getExistingNumbers(long contactId) {
		List<SensorContactNumber> list = m_daoSession.getSensorContactNumberDao().queryBuilder().where(Properties.ContactId.eq(contactId))
				.list();

		HashMap<Long, SensorContactNumber> map = new HashMap<Long, SensorContactNumber>();
		for (SensorContactNumber number : list)
			map.put(number.getNumberId(), number);

		return map;
	}

	@SuppressLint("UseSparseArrays")
	private HashMap<String, SensorContactMail> getExistingMails(long contactId) {
		List<SensorContactMail> list = m_daoSession.getSensorContactMailDao().queryBuilder()
				.where(SensorContactMailDao.Properties.ContactId.eq(contactId)).list();

		HashMap<String, SensorContactMail> map = new HashMap<String, SensorContactMail>();
		for (SensorContactMail mail : list)
			map.put(mail.getAddress(), mail);

		return map;
	}

	private String getNote(String contactId) {
		String note = null;
		String[] columns = new String[] { Note.NOTE };
		String where = Data.RAW_CONTACT_ID + " = ? AND " + Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { contactId, Note.CONTENT_ITEM_TYPE };
		Cursor contacts = context.getContentResolver().query(URI_DATA, columns, where, whereParameters, null);
		if (contacts.moveToFirst()) {
			note = getStringByColumnName(contacts, Note.NOTE);
		}
		contacts.close();
		return note;
	}

	@Override
	public void startSensor() {
		m_bIsRunning = true;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				syncData();
				context.getContentResolver().registerContentObserver(URI_CONTACTS, true, m_observer);
			}
		});
		thread.setName("ContactsSensorThread");
		thread.start();
	}
	
	@Override
	public EPushType getPushType() {
		return EPushType.MANUALLY_WLAN_ONLY;
	}
}
