package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.contentobserver;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractContentObserverSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28.10.2015
 */
public final class CalendarSensor extends AbstractContentObserverSensor {

    private static final String TAG = CalendarSensor.class.getSimpleName();

    private static CalendarSensor INSTANCE;

    private static final Uri URI_CALENDAR = Events.CONTENT_URI;
    private static final Uri URI_REMINDER = Reminders.CONTENT_URI;

    private final CalendarReminderSensorDao calendarReminderSensorDao;
    private final CalendarSensorDao calendarSensorDao;

    private AsyncTask<Void, Void, Void> asyncTask;

    private ContentResolver contentResolver;

    private static final String[] PROJECTION_EVENTS =
            {
                    Events._ID,
                    Events.ALL_DAY,
                    Events.AVAILABILITY,
                    Events.CALENDAR_ID,
                    Events.DESCRIPTION,
                    Events.DTSTART,
                    Events.DTEND,
                    Events.DURATION,
                    Events.EVENT_LOCATION,
                    Events.EVENT_TIMEZONE,
                    Events.EVENT_END_TIMEZONE,
                    Events.EXDATE,
                    Events.EXRULE,
                    Events.HAS_ALARM,
                    Events.LAST_DATE,
                    Events.ORIGINAL_ALL_DAY,
                    Events.ORIGINAL_ID,
                    Events.ORIGINAL_INSTANCE_TIME,
                    Events.RDATE,
                    Events.RRULE,
                    Events.STATUS,
                    Events.TITLE
            };

    private static final String[] PROJECTION_REMINDERS =
            {
                    Reminders._ID,
                    Reminders.METHOD,
                    Reminders.MINUTES};

    private String reminderSelection;

    private CalendarSensor(Context context) {
        super(context);
        calendarReminderSensorDao = daoProvider.getCalendarReminderSensorDao();
        calendarSensorDao = daoProvider.getCalendarSensorDao();
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static CalendarSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new CalendarSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public int getType() {
        return SensorApiType.CALENDAR;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void syncData() {

        if (context == null || !isRunning()) {
            return;
        }

        Log.d(TAG, "Syncing data...");

        if (ActivityCompat.checkSelfPermission(
                context,
                permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Permission was NOT granted!");
            setRunning(false);

            return;
        }

        contentResolver = context.getContentResolver();

        // for reminders
        reminderSelection = Reminders.EVENT_ID + " = ?";

        // Submit the query
        Cursor cur = contentResolver.query(URI_CALENDAR, PROJECTION_EVENTS, "deleted=?", new String[]{"0"}, null);

        if (cur == null || cur.getCount() <= 0) {
            return;
        }

        LongSparseArray<DbCalendarSensor> allExistingEvents = new LongSparseArray<>();

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();
        List<DbCalendarSensor> events = calendarSensorDao.getAll(deviceId);

        for (DbCalendarSensor event : events) {
            allExistingEvents.put(event.getEventId(), event);
        }

        List<DbCalendarSensor> entriesToInsert = new ArrayList<>(cur.getCount());
        String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

        try {

            // Iterate over event
            while (cur.moveToNext() && isRunning()) {

                DbCalendarSensor event = new DbCalendarSensor();

                event.setEventId(getLongByColumnName(cur, Events._ID));
                event.setCalendarId(getLongByColumnName(cur, Events.CALENDAR_ID));
                event.setAllDay(getBoolByColumnName(cur, Events.ALL_DAY));
                event.setAvailability(getIntByColumnName(cur, Events.AVAILABILITY));
                event.setDescription(getStringByColumnName(cur, Events.DESCRIPTION));
                event.setTimestampStart(getLongByColumnName(cur, Events.DTSTART));
                event.setTimestampEnd(getLongByColumnName(cur, Events.DTEND));
                event.setDuration(getStringByColumnName(cur, Events.DURATION));
                event.setLocation(getStringByColumnName(cur, Events.EVENT_LOCATION));
                event.setTimezoneStart(getStringByColumnName(cur, Events.EVENT_TIMEZONE));
                event.setTimezoneEnd(getStringByColumnName(cur, Events.EVENT_END_TIMEZONE));
                event.setRecurrenceExceptionDate(getStringByColumnName(cur, Events.EXDATE));
                event.setRecurrenceExceptionRule(getStringByColumnName(cur, Events.EXRULE));
                event.setHasAlarm(getBoolByColumnName(cur, Events.HAS_ALARM));
                event.setLastDate(getLongByColumnName(cur, Events.LAST_DATE));
                event.setOriginalAllDay(getBoolByColumnName(cur, Events.ORIGINAL_ALL_DAY));
                event.setOriginalId(getStringByColumnName(cur, Events.ORIGINAL_ID));
                event.setOriginalInstanceTime(getLongByColumnName(cur, Events.ORIGINAL_INSTANCE_TIME));
                event.setRecurrenceDate(getStringByColumnName(cur, Events.RDATE));
                event.setRecurrenceRule(getStringByColumnName(cur, Events.RRULE));
                event.setStatus(getIntByColumnName(cur, Events.STATUS));
                event.setTitle(getStringByColumnName(cur, Events.TITLE));
                event.setIsNew(Boolean.TRUE);
                event.setIsDeleted(Boolean.FALSE);
                event.setIsUpdated(Boolean.TRUE);
                event.setDeviceId(deviceId);
                event.setCreated(created);

                // checking for any changes
                if (checkForChange(allExistingEvents, event)) {
                    entriesToInsert.add(event);
                }

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        syncReminders(event);
                        return null;
                    }

                }.execute();
            }

            if (!entriesToInsert.isEmpty()) {
                Log.d(TAG, "Insert entries");
                calendarSensorDao.insert(entriesToInsert);
                Log.d(TAG, "Finished");
            }

            /**
             * Sync reminder at this point
             */
            for (int i = 0, size = allExistingEvents.size(); i < size; i++) {

                if (!isRunning()) {
                    break;
                }

                DbCalendarSensor existingEvent = allExistingEvents.valueAt(i);

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        syncReminders(existingEvent);
                        return null;
                    }

                }.execute();
            }

        } catch (Exception e) {
            Log.e(TAG, "Cannot get all existing events!", e);
            return;
        } finally {
            cur.close();
        }
    }

    protected void syncReminders(DbCalendarSensor event) {

        if (event == null) {
            return;
        }

        boolean hasAlarm = event.getHasAlarm();

        if (hasAlarm) {

            long eventId = event.getEventId();
            LongSparseArray<DbCalendarReminderSensor> mapExistingReminders = getExistingReminders(eventId);

            // optional reminderSelection
            String[] selectionArgs = {String.valueOf(eventId)};

            Cursor cur = null;

            try {

                long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

                cur = contentResolver.query(URI_REMINDER, PROJECTION_REMINDERS, reminderSelection, selectionArgs, null);

                if (cur == null || cur.getCount() <= 0) {
                    return;
                }

                List<DbCalendarReminderSensor> entriesToInsert = new ArrayList<>(cur.getCount());
                String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

                // Iterate over event
                while (cur.moveToNext() && isRunning()) {

                    DbCalendarReminderSensor reminder = new DbCalendarReminderSensor();

                    reminder.setReminderId(getLongByColumnName(cur, Reminders._ID));
                    reminder.setEventId(eventId);
                    reminder.setMethod(getIntByColumnName(cur, Reminders.METHOD));
                    reminder.setMinutes(getIntByColumnName(cur, Reminders.MINUTES));
                    reminder.setIsNew(Boolean.TRUE);
                    reminder.setIsDeleted(Boolean.FALSE);
                    reminder.setIsUpdated(Boolean.TRUE);
                    reminder.setDeviceId(deviceId);
                    reminder.setCreated(created);

                    if (checkForReminderChange(mapExistingReminders, reminder)) {
                        entriesToInsert.add(reminder);
                    }
                }

                if (!entriesToInsert.isEmpty()) {
                    calendarReminderSensorDao.insert(entriesToInsert);
                }

            } finally {
                if (cur != null) {
                    cur.close();
                }
            }

        }
    }

    private boolean checkForChange(LongSparseArray<DbCalendarSensor> map, DbCalendarSensor newItem) {

        Long eventId = newItem.getEventId();

        if (eventId == null) {
            return false;
        }

        DbCalendarSensor existingItem = map.get(eventId);
        map.delete(eventId);

        if (existingItem == null) {

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.TRUE);
            newItem.setIsDeleted(Boolean.FALSE);

            return true;

        }
        if (hasEventDifference(existingItem, newItem)) {

            newItem.setIsNew(Boolean.FALSE);
            newItem.setIsUpdated(Boolean.TRUE);
            newItem.setIsDeleted(Boolean.FALSE);
            newItem.setId(existingItem.getId());

            return true;
        }

        return false;
    }

    private boolean checkForReminderChange(LongSparseArray<DbCalendarReminderSensor> map, DbCalendarReminderSensor newItem) {

        if (newItem == null) {
            throw new IllegalArgumentException();
        }

        Long eventId = newItem.getEventId();

        if (eventId == null) {
            return false;
        }

        DbCalendarReminderSensor existingItem = map.get(eventId);
        map.delete(eventId);

        if (existingItem == null) {

            newItem.setIsNew(Boolean.TRUE);
            newItem.setIsUpdated(Boolean.TRUE);
            newItem.setIsDeleted(Boolean.FALSE);

            return true;

        }
        if (hasReminderDifference(existingItem, newItem)) {

            newItem.setIsNew(Boolean.FALSE);
            newItem.setIsUpdated(Boolean.TRUE);
            newItem.setIsDeleted(Boolean.FALSE);
            newItem.setId(existingItem.getId());

            return true;
        }

        return false;
    }

    /**
     * Returns map of <reminder_id, reminder_object>
     *
     * @param eventId
     * @return
     */
    private LongSparseArray<DbCalendarReminderSensor> getExistingReminders(long eventId) {

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        List<DbCalendarReminderSensor> list = daoProvider
                .getCalendarReminderSensorDao()
                .getAllByEventId(eventId, deviceId);

        LongSparseArray<DbCalendarReminderSensor> map = new LongSparseArray<>(list.size());

        for (DbCalendarReminderSensor reminder : list) {
            map.put(reminder.getReminderId(), reminder);
        }

        return map;
    }

    private boolean hasEventDifference(DbCalendarSensor existingEvent, DbCalendarSensor newEvent) {

        if (checkForDifference(existingEvent.getAllDay(), newEvent.getAllDay())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getHasAlarm(), newEvent.getHasAlarm())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getOriginalAllDay(), newEvent.getOriginalAllDay())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getAvailability(), newEvent.getAvailability())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getCalendarId(), newEvent.getCalendarId())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getDescription(), newEvent.getDescription())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getDuration(), newEvent.getDuration())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getLastDate(), newEvent.getLastDate())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getLocation(), newEvent.getLocation())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getOriginalId(), newEvent.getOriginalId())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getOriginalInstanceTime(), newEvent.getOriginalInstanceTime())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getRecurrenceDate(), newEvent.getRecurrenceDate())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getRecurrenceExceptionDate(), newEvent.getRecurrenceExceptionDate())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getRecurrenceExceptionRule(), newEvent.getRecurrenceExceptionRule())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getRecurrenceRule(), newEvent.getRecurrenceRule())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getStatus(), newEvent.getStatus())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getTimestampStart(), newEvent.getTimestampStart())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getTimestampEnd(), newEvent.getTimestampEnd())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getTimezoneStart(), newEvent.getTimezoneStart())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getTimezoneEnd(), newEvent.getTimezoneEnd())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingEvent.getTitle(), newEvent.getTitle())) {
            {
                return true;
            }
        }

        return false;
    }

    private boolean hasReminderDifference(DbCalendarReminderSensor existingReminder, DbCalendarReminderSensor newReminder) {

        if (checkForDifference(existingReminder.getMethod(), newReminder.getMethod())) {
            {
                return true;
            }
        }
        if (checkForDifference(existingReminder.getMinutes(), newReminder.getMinutes())) {
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void startSensor() {

        asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                syncData();
                context.getContentResolver().registerContentObserver(URI_CALENDAR, true, mObserver);
                context.getContentResolver().registerContentObserver(URI_REMINDER, true, mObserver);

                return null;
            }

        }.execute();

        setRunning(true);
    }

    @Override
    public void stopSensor() {

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }

        asyncTask = null;

        setRunning(false);
    }

    @Override
    public void dumpData() {

    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }
}
