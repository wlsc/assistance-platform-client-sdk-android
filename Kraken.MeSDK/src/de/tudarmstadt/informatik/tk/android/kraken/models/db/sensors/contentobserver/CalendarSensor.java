package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.contentobserver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

import java.lang.reflect.Method;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractContentObserverSensor;

public class CalendarSensor extends AbstractContentObserverSensor {

    private static Uri URI_CALENDAR = android.provider.CalendarContract.Events.CONTENT_URI;
    private static Uri URI_REMINDER = android.provider.CalendarContract.Reminders.CONTENT_URI;

    public CalendarSensor(Context context) {
        super(context);
    }

    private static final String[] PROJECTION_EVENTS = new String[]{Events._ID, Events.ALL_DAY, Events.AVAILABILITY, Events.CALENDAR_ID, Events.DESCRIPTION,
            Events.DTSTART, Events.DTEND, Events.DURATION, Events.EVENT_LOCATION, Events.EVENT_TIMEZONE, Events.EVENT_END_TIMEZONE, Events.EXDATE,
            Events.EXRULE, Events.HAS_ALARM, Events.LAST_DATE, Events.ORIGINAL_ALL_DAY, Events.ORIGINAL_ID, Events.ORIGINAL_INSTANCE_TIME, Events.RDATE,
            Events.RRULE, Events.STATUS, Events.TITLE};

    private static final String[] PROJECTION_REMINDERS = new String[]{Reminders._ID, Reminders.METHOD, Reminders.MINUTES};

    // caching
    private Method m_checkDifferenceMethodForEvent;
    private Method m_getIdMethodForEvent;
    private Method m_checkDifferenceMethodForReminder;
    private Method m_getIdMethodForReminder;
    private Method m_getEventIdForCalendarEvents;
    private boolean m_bFlushToServer;

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_CALENDAR;
    }

    @Override
    public void reset() {

    }

    protected void syncData() {
        // optional selection
        // String selection = null;
        // String[] selectionArgs = new String[] {};

        Log.d("kraken", "CalendarSensor syncData");

        m_bFlushToServer = false;

        // Submit the query
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(URI_CALENDAR, PROJECTION_EVENTS, "deleted=?", new String[]{"0"}, null);

//		HashMap<Long, SensorCalendarEvent> allExistingEvents;
//		try {
//			allExistingEvents = getAllExistingEvents();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}

        // Iterate over event
//		while (cur.moveToNext() && m_bIsRunning) {
//			SensorCalendarEvent event = new SensorCalendarEvent();
//			event.setEventId(getLongByColumnName(cur, Events._ID));
//			event.setAllDay(getBoolByColumnName(cur, Events.ALL_DAY));
//			event.setAvailability(getIntByColumnName(cur, Events.AVAILABILITY));
//			event.setCalendarId(getLongByColumnName(cur, Events.CALENDAR_ID));
//			event.setDescription(getStringByColumnName(cur, Events.DESCRIPTION));
//			event.setTimestampStart(getLongByColumnName(cur, Events.DTSTART));
//			event.setTimestampEnd(getLongByColumnName(cur, Events.DTEND));
//			event.setDuration(getStringByColumnName(cur, Events.DURATION));
//			event.setLocation(getStringByColumnName(cur, Events.EVENT_LOCATION));
//			event.setTimezoneStart(getStringByColumnName(cur, Events.EVENT_TIMEZONE));
//			event.setTimezoneEnd(getStringByColumnName(cur, Events.EVENT_END_TIMEZONE));
//			event.setRecurrenceExceptionDate(getStringByColumnName(cur, Events.EXDATE));
//			event.setRecurrenceExceptionRule(getStringByColumnName(cur, Events.EXRULE));
//			event.setHasAlarm(getBoolByColumnName(cur, Events.HAS_ALARM));
//			event.setLastDate(getLongByColumnName(cur, Events.LAST_DATE));
//			event.setOriginalAllDay(getBoolByColumnName(cur, Events.ORIGINAL_ALL_DAY));
//			event.setOriginalId(getStringByColumnName(cur, Events.ORIGINAL_ID));
//			event.setOriginalInstanceTime(getLongByColumnName(cur, Events.ORIGINAL_INSTANCE_TIME));
//			event.setRecurrenceDate(getStringByColumnName(cur, Events.RDATE));
//			event.setRecurrenceRule(getStringByColumnName(cur, Events.RRULE));
//			event.setStatus(getIntByColumnName(cur, Events.STATUS));
//			event.setTitle(getStringByColumnName(cur, Events.TITLE));
//			event.setIsNew(true);
//			event.setIsDeleted(false);
//			event.setIsUpdated(false);
//
//            //Log.d("kraken", "CalendarSensor: " + event.getTitle());
//
//			try {
//				if (checkForEventChange(allExistingEvents, event)) {
//                    //Log.d("kraken", "checkForEventChange: TRUE " + event.getTitle());
//					handleDBEntry(event, !event.getIsNew(), true, false);
//					m_bFlushToServer = true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			syncReminders(event);
//		}
//		cur.close();
//
//		for (SensorCalendarEvent event : allExistingEvents.values()) {
//			if (!m_bIsRunning)
//				return;
//			syncReminders(event);
//		}
//		// remaining events are deleted
//		if (deleteRemainingEntries(allExistingEvents, true))
//			m_bFlushToServer = true;
//
//		if (m_bFlushToServer) {
//            String strFullqualifiedDatabaseClassName = getSensorType().getFullqualifiedDatabaseClassName();
//            //SensorData dataEvents = flushData(m_daoSession, strFullqualifiedDatabaseClassName);
//            //SensorData dataReminders = flushData(m_daoSession, strFullqualifiedDatabaseClassName + "Reminder");
//            //ServerPushManager.getInstance(context).flushManually(dataEvents, dataReminders);
//            ApiMessage.DataWrapper dataEvents = flushDataRetro(strFullqualifiedDatabaseClassName);
//            ApiMessage.DataWrapper dataReminders = flushDataRetro(strFullqualifiedDatabaseClassName + "Reminder");
//            RetroServerPushManager.getInstance(context).flushManually(getPushType(), dataEvents, dataReminders);
//        }
    }

//	protected void syncReminders(SensorCalendarEvent event) {
//		long longEventId = event.getEventId();
//		boolean bHasAlarm = event.getHasAlarm();
//
//		HashMap<Long, SensorCalendarEventReminder> mapExistingReminders = getExistingReminders(longEventId);
//
//		if (bHasAlarm) {
//			// optional selection
//			final String selection = Reminders.EVENT_ID + " = ?";
//			String[] selectionArgs = new String[] { String.valueOf(longEventId) };
//
//			// Submit the query
//			ContentResolver cr = context.getContentResolver();
//			Cursor cur = cr.query(URI_REMINDER, PROJECTION_REMINDERS, selection, selectionArgs, null);
//
//			// Iterate over eber event
//			while (cur.moveToNext() && m_bIsRunning) {
//
//				SensorCalendarEventReminder reminder = new SensorCalendarEventReminder();
//				reminder.setReminderId(getLongByColumnName(cur, Reminders._ID));
//				reminder.setEventId(longEventId);
//				reminder.setMethod(getIntByColumnName(cur, Reminders.METHOD));
//				reminder.setMinutes(getIntByColumnName(cur, Reminders.MINUTES));
//				reminder.setIsNew(true);
//				reminder.setIsDeleted(false);
//				reminder.setIsUpdated(false);
//
//				try {
//					if (checkForReminderChange(mapExistingReminders, reminder)) {
//						// TODO: Check Set To Handler (looper...)
//						handleDBEntry(reminder, !reminder.getIsNew(), true, false);
//						m_bFlushToServer = true;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//			cur.close();
//
//		}
//		// remaining events are deleted
//		if (deleteRemainingEntries(mapExistingReminders, false)) {
//			m_bFlushToServer = true;
//		}
//	}

//	private HashMap<Long, SensorCalendarEvent> getAllExistingEvents() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException,
//			NoSuchMethodException, InvocationTargetException {
//		if (m_getEventIdForCalendarEvents == null) {
//			m_getEventIdForCalendarEvents = SensorCalendarEvent.class.getDeclaredMethod("getEventId", new Class[] {});
//			m_getEventIdForCalendarEvents.setAccessible(true);
//		}
//		return getAllExistingEntries(SensorCalendarEvent.class, m_getEventIdForCalendarEvents);
//	}

//	private HashMap<Long, SensorCalendarEventReminder> getExistingReminders(long eventId) {
//		List<SensorCalendarEventReminder> list = m_daoSession.getSensorCalendarEventReminderDao().queryBuilder().where(Properties.EventId.eq(eventId)).list();
//
//		HashMap<Long, SensorCalendarEventReminder> map = new HashMap<Long, SensorCalendarEventReminder>();
//		for (SensorCalendarEventReminder reminder : list)
//			map.put(reminder.getReminderId(), reminder);
//
//		return map;
//	}

//	private boolean checkForEventChange(HashMap<Long, SensorCalendarEvent> map, SensorCalendarEvent newEvent) throws Exception {
//		try {
//			if (m_getIdMethodForEvent == null || m_checkDifferenceMethodForEvent == null) {
//				m_getIdMethodForEvent = SensorCalendarEvent.class.getDeclaredMethod("getEventId", new Class[] {});
//				m_checkDifferenceMethodForEvent = getClass().getDeclaredMethod("hasEventDifference",
//						new Class[] { SensorCalendarEvent.class, SensorCalendarEvent.class });
//				m_checkDifferenceMethodForEvent.setAccessible(true);
//			}
//			return checkForChange(map, newEvent, m_getIdMethodForEvent, m_checkDifferenceMethodForEvent);
//		} catch (Exception e) {
//			throw new Exception(e);
//		}
//	}

//	private boolean checkForReminderChange(HashMap<Long, SensorCalendarEventReminder> map, SensorCalendarEventReminder newReminder) throws Exception {
//		try {
//			if (m_getIdMethodForReminder == null || m_checkDifferenceMethodForReminder == null) {
//				m_getIdMethodForReminder = SensorCalendarEventReminder.class.getDeclaredMethod("getReminderId", new Class[] {});
//				m_checkDifferenceMethodForReminder = getClass().getDeclaredMethod("hasReminderDifference",
//						new Class[] { SensorCalendarEventReminder.class, SensorCalendarEventReminder.class });
//				m_checkDifferenceMethodForReminder.setAccessible(true);
//			}
//			return checkForChange(map, newReminder, m_getIdMethodForReminder, m_checkDifferenceMethodForReminder);
//		} catch (Exception e) {
//			throw new Exception(e);
//		}
//	}

//	private boolean hasEventDifference(SensorCalendarEvent existingEvent, SensorCalendarEvent newEvent) {
//		if (checkForDifference(existingEvent.getAllDay(), newEvent.getAllDay()))
//			return true;
//		if (checkForDifference(existingEvent.getHasAlarm(), newEvent.getHasAlarm()))
//			return true;
//		if (checkForDifference(existingEvent.getOriginalAllDay(), newEvent.getOriginalAllDay()))
//			return true;
//		if (checkForDifference(existingEvent.getAvailability(), newEvent.getAvailability()))
//			return true;
//		if (checkForDifference(existingEvent.getCalendarId(), newEvent.getCalendarId()))
//			return true;
//		if (checkForDifference(existingEvent.getDescription(), newEvent.getDescription()))
//			return true;
//		if (checkForDifference(existingEvent.getDuration(), newEvent.getDuration()))
//			return true;
//		if (checkForDifference(existingEvent.getLastDate(), newEvent.getLastDate()))
//			return true;
//		if (checkForDifference(existingEvent.getLocation(), newEvent.getLocation()))
//			return true;
//		if (checkForDifference(existingEvent.getOriginalId(), newEvent.getOriginalId()))
//			return true;
//		if (checkForDifference(existingEvent.getOriginalInstanceTime(), newEvent.getOriginalInstanceTime()))
//			return true;
//		if (checkForDifference(existingEvent.getRecurrenceDate(), newEvent.getRecurrenceDate()))
//			return true;
//		if (checkForDifference(existingEvent.getRecurrenceExceptionDate(), newEvent.getRecurrenceExceptionDate()))
//			return true;
//		if (checkForDifference(existingEvent.getRecurrenceExceptionRule(), newEvent.getRecurrenceExceptionRule()))
//			return true;
//		if (checkForDifference(existingEvent.getRecurrenceRule(), newEvent.getRecurrenceRule()))
//			return true;
//		if (checkForDifference(existingEvent.getStatus(), newEvent.getStatus()))
//			return true;
//		if (checkForDifference(existingEvent.getTimestampStart(), newEvent.getTimestampStart()))
//			return true;
//		if (checkForDifference(existingEvent.getTimestampEnd(), newEvent.getTimestampEnd()))
//			return true;
//		if (checkForDifference(existingEvent.getTimezoneStart(), newEvent.getTimezoneStart()))
//			return true;
//		if (checkForDifference(existingEvent.getTimezoneEnd(), newEvent.getTimezoneEnd()))
//			return true;
//		if (checkForDifference(existingEvent.getTitle(), newEvent.getTitle()))
//			return true;
//
//		return false;
//	}

//	private boolean hasReminderDifference(SensorCalendarEventReminder existingReminder, SensorCalendarEventReminder newReminder) {
//
//		if (checkForDifference(existingReminder.getMethod(), newReminder.getMethod()))
//			return true;
//		if (checkForDifference(existingReminder.getMinutes(), newReminder.getMinutes()))
//			return true;
//
//		return false;
//	}

    @Override
    public void startSensor() {
        m_bIsRunning = true;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                syncData();
                context.getContentResolver().registerContentObserver(URI_CALENDAR, true, m_observer);
                context.getContentResolver().registerContentObserver(URI_REMINDER, true, m_observer);
            }
        });
        thread.setName("CalendarSensorThread");
        thread.start();
    }

    @Override
    protected Query<? extends IDbSensor> getDbQuery(Class<? extends IDbSensor> sensorClass, long longTimestamp, QueryBuilder<? extends IDbSensor> qb)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        Property propIsNew = getPropertiesArgument(sensorClass, "IsNew");
        Property propIsUpdated = getPropertiesArgument(sensorClass, "IsUpdated");
        Property propIsDeleted = getPropertiesArgument(sensorClass, "IsDeleted");
        Query<? extends IDbSensor> query = qb.where(qb.or(propIsNew.eq(true), propIsUpdated.eq(true), propIsDeleted.eq(true))).build();
        return query;
    }

}
