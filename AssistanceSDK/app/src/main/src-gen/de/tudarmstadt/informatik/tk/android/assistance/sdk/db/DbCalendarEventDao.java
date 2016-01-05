package de.tudarmstadt.informatik.tk.android.assistance.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.Nullable;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbCalendarEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "calendar_event".
*/
public class DbCalendarEventDao extends AbstractDao<DbCalendarEvent, Long> {

    public static final String TABLENAME = "calendar_event";

    /**
     * Properties of entity DbCalendarEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property EventId = new Property(1, Long.class, "eventId", false, "EVENT_ID");
        public final static Property CalendarId = new Property(2, Long.class, "calendarId", false, "CALENDAR_ID");
        public final static Property AllDay = new Property(3, Boolean.class, "allDay", false, "ALL_DAY");
        public final static Property Availability = new Property(4, Integer.class, "availability", false, "AVAILABILITY");
        public final static Property Description = new Property(5, String.class, "description", false, "DESCRIPTION");
        public final static Property TimestampStart = new Property(6, Long.class, "timestampStart", false, "TIMESTAMP_START");
        public final static Property TimestampEnd = new Property(7, Long.class, "timestampEnd", false, "TIMESTAMP_END");
        public final static Property Duration = new Property(8, String.class, "duration", false, "DURATION");
        public final static Property Location = new Property(9, String.class, "location", false, "LOCATION");
        public final static Property TimezoneStart = new Property(10, String.class, "timezoneStart", false, "TIMEZONE_START");
        public final static Property TimezoneEnd = new Property(11, String.class, "timezoneEnd", false, "TIMEZONE_END");
        public final static Property RecurrenceExceptionDate = new Property(12, String.class, "recurrenceExceptionDate", false, "RECURRENCE_EXCEPTION_DATE");
        public final static Property RecurrenceExceptionRule = new Property(13, String.class, "recurrenceExceptionRule", false, "RECURRENCE_EXCEPTION_RULE");
        public final static Property HasAlarm = new Property(14, Boolean.class, "hasAlarm", false, "HAS_ALARM");
        public final static Property LastDate = new Property(15, Long.class, "lastDate", false, "LAST_DATE");
        public final static Property OriginalAllDay = new Property(16, Boolean.class, "originalAllDay", false, "ORIGINAL_ALL_DAY");
        public final static Property OriginalId = new Property(17, String.class, "originalId", false, "ORIGINAL_ID");
        public final static Property OriginalInstanceTime = new Property(18, Long.class, "originalInstanceTime", false, "ORIGINAL_INSTANCE_TIME");
        public final static Property RecurrenceDate = new Property(19, String.class, "recurrenceDate", false, "RECURRENCE_DATE");
        public final static Property RecurrenceRule = new Property(20, String.class, "recurrenceRule", false, "RECURRENCE_RULE");
        public final static Property Status = new Property(21, Integer.class, "status", false, "STATUS");
        public final static Property Title = new Property(22, String.class, "title", false, "TITLE");
        public final static Property IsNew = new Property(23, Boolean.class, "isNew", false, "IS_NEW");
        public final static Property IsUpdated = new Property(24, Boolean.class, "isUpdated", false, "IS_UPDATED");
        public final static Property IsDeleted = new Property(25, Boolean.class, "isDeleted", false, "IS_DELETED");
        public final static Property Created = new Property(26, String.class, "created", false, "CREATED");
    };


    public DbCalendarEventDao(DaoConfig config) {
        super(config);
    }
    
    public DbCalendarEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"calendar_event\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EVENT_ID\" INTEGER," + // 1: eventId
                "\"CALENDAR_ID\" INTEGER," + // 2: calendarId
                "\"ALL_DAY\" INTEGER," + // 3: allDay
                "\"AVAILABILITY\" INTEGER," + // 4: availability
                "\"DESCRIPTION\" TEXT," + // 5: description
                "\"TIMESTAMP_START\" INTEGER," + // 6: timestampStart
                "\"TIMESTAMP_END\" INTEGER," + // 7: timestampEnd
                "\"DURATION\" TEXT," + // 8: duration
                "\"LOCATION\" TEXT," + // 9: location
                "\"TIMEZONE_START\" TEXT," + // 10: timezoneStart
                "\"TIMEZONE_END\" TEXT," + // 11: timezoneEnd
                "\"RECURRENCE_EXCEPTION_DATE\" TEXT," + // 12: recurrenceExceptionDate
                "\"RECURRENCE_EXCEPTION_RULE\" TEXT," + // 13: recurrenceExceptionRule
                "\"HAS_ALARM\" INTEGER," + // 14: hasAlarm
                "\"LAST_DATE\" INTEGER," + // 15: lastDate
                "\"ORIGINAL_ALL_DAY\" INTEGER," + // 16: originalAllDay
                "\"ORIGINAL_ID\" TEXT," + // 17: originalId
                "\"ORIGINAL_INSTANCE_TIME\" INTEGER," + // 18: originalInstanceTime
                "\"RECURRENCE_DATE\" TEXT," + // 19: recurrenceDate
                "\"RECURRENCE_RULE\" TEXT," + // 20: recurrenceRule
                "\"STATUS\" INTEGER," + // 21: status
                "\"TITLE\" TEXT," + // 22: title
                "\"IS_NEW\" INTEGER," + // 23: isNew
                "\"IS_UPDATED\" INTEGER," + // 24: isUpdated
                "\"IS_DELETED\" INTEGER," + // 25: isDeleted
                "\"CREATED\" TEXT NOT NULL );"); // 26: created
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_calendar_event__id ON calendar_event" +
                " (\"_id\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"calendar_event\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DbCalendarEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long eventId = entity.getEventId();
        if (eventId != null) {
            stmt.bindLong(2, eventId);
        }
 
        Long calendarId = entity.getCalendarId();
        if (calendarId != null) {
            stmt.bindLong(3, calendarId);
        }
 
        Boolean allDay = entity.getAllDay();
        if (allDay != null) {
            stmt.bindLong(4, allDay ? 1L: 0L);
        }
 
        Integer availability = entity.getAvailability();
        if (availability != null) {
            stmt.bindLong(5, availability);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(6, description);
        }
 
        Long timestampStart = entity.getTimestampStart();
        if (timestampStart != null) {
            stmt.bindLong(7, timestampStart);
        }
 
        Long timestampEnd = entity.getTimestampEnd();
        if (timestampEnd != null) {
            stmt.bindLong(8, timestampEnd);
        }
 
        String duration = entity.getDuration();
        if (duration != null) {
            stmt.bindString(9, duration);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(10, location);
        }
 
        String timezoneStart = entity.getTimezoneStart();
        if (timezoneStart != null) {
            stmt.bindString(11, timezoneStart);
        }
 
        String timezoneEnd = entity.getTimezoneEnd();
        if (timezoneEnd != null) {
            stmt.bindString(12, timezoneEnd);
        }
 
        String recurrenceExceptionDate = entity.getRecurrenceExceptionDate();
        if (recurrenceExceptionDate != null) {
            stmt.bindString(13, recurrenceExceptionDate);
        }
 
        String recurrenceExceptionRule = entity.getRecurrenceExceptionRule();
        if (recurrenceExceptionRule != null) {
            stmt.bindString(14, recurrenceExceptionRule);
        }
 
        Boolean hasAlarm = entity.getHasAlarm();
        if (hasAlarm != null) {
            stmt.bindLong(15, hasAlarm ? 1L: 0L);
        }
 
        Long lastDate = entity.getLastDate();
        if (lastDate != null) {
            stmt.bindLong(16, lastDate);
        }
 
        Boolean originalAllDay = entity.getOriginalAllDay();
        if (originalAllDay != null) {
            stmt.bindLong(17, originalAllDay ? 1L: 0L);
        }
 
        String originalId = entity.getOriginalId();
        if (originalId != null) {
            stmt.bindString(18, originalId);
        }
 
        Long originalInstanceTime = entity.getOriginalInstanceTime();
        if (originalInstanceTime != null) {
            stmt.bindLong(19, originalInstanceTime);
        }
 
        String recurrenceDate = entity.getRecurrenceDate();
        if (recurrenceDate != null) {
            stmt.bindString(20, recurrenceDate);
        }
 
        String recurrenceRule = entity.getRecurrenceRule();
        if (recurrenceRule != null) {
            stmt.bindString(21, recurrenceRule);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(22, status);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(23, title);
        }
 
        Boolean isNew = entity.getIsNew();
        if (isNew != null) {
            stmt.bindLong(24, isNew ? 1L: 0L);
        }
 
        Boolean isUpdated = entity.getIsUpdated();
        if (isUpdated != null) {
            stmt.bindLong(25, isUpdated ? 1L: 0L);
        }
 
        Boolean isDeleted = entity.getIsDeleted();
        if (isDeleted != null) {
            stmt.bindLong(26, isDeleted ? 1L: 0L);
        }
        stmt.bindString(27, entity.getCreated());
    }

    /** @inheritdoc */
    @Nullable
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DbCalendarEvent readEntity(Cursor cursor, int offset) {
        DbCalendarEvent entity = new DbCalendarEvent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // eventId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // calendarId
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // allDay
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // availability
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // description
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // timestampStart
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // timestampEnd
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // duration
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // location
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // timezoneStart
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // timezoneEnd
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // recurrenceExceptionDate
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // recurrenceExceptionRule
            cursor.isNull(offset + 14) ? null : cursor.getShort(offset + 14) != 0, // hasAlarm
            cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15), // lastDate
            cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0, // originalAllDay
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // originalId
            cursor.isNull(offset + 18) ? null : cursor.getLong(offset + 18), // originalInstanceTime
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // recurrenceDate
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // recurrenceRule
            cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21), // status
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // title
            cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0, // isNew
            cursor.isNull(offset + 24) ? null : cursor.getShort(offset + 24) != 0, // isUpdated
            cursor.isNull(offset + 25) ? null : cursor.getShort(offset + 25) != 0, // isDeleted
            cursor.getString(offset + 26) // created
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DbCalendarEvent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEventId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCalendarId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setAllDay(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setAvailability(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setDescription(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTimestampStart(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setTimestampEnd(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setDuration(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLocation(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTimezoneStart(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTimezoneEnd(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRecurrenceExceptionDate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setRecurrenceExceptionRule(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setHasAlarm(cursor.isNull(offset + 14) ? null : cursor.getShort(offset + 14) != 0);
        entity.setLastDate(cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15));
        entity.setOriginalAllDay(cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0);
        entity.setOriginalId(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setOriginalInstanceTime(cursor.isNull(offset + 18) ? null : cursor.getLong(offset + 18));
        entity.setRecurrenceDate(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setRecurrenceRule(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setStatus(cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21));
        entity.setTitle(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setIsNew(cursor.isNull(offset + 23) ? null : cursor.getShort(offset + 23) != 0);
        entity.setIsUpdated(cursor.isNull(offset + 24) ? null : cursor.getShort(offset + 24) != 0);
        entity.setIsDeleted(cursor.isNull(offset + 25) ? null : cursor.getShort(offset + 25) != 0);
        entity.setCreated(cursor.getString(offset + 26));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DbCalendarEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Nullable
    @Override
    public Long getKey(DbCalendarEvent entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
