package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorCalendarEventReminder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SENSOR_CALENDAR_EVENT_REMINDER".
*/
public class SensorCalendarEventReminderDao extends AbstractDao<SensorCalendarEventReminder, Long> {

    public static final String TABLENAME = "SENSOR_CALENDAR_EVENT_REMINDER";

    /**
     * Properties of entity SensorCalendarEventReminder.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ReminderId = new Property(1, Long.class, "reminderId", false, "REMINDER_ID");
        public final static Property EventId = new Property(2, Long.class, "eventId", false, "EVENT_ID");
        public final static Property Method = new Property(3, Integer.class, "method", false, "METHOD");
        public final static Property Minutes = new Property(4, Integer.class, "minutes", false, "MINUTES");
        public final static Property Timestamp = new Property(5, Long.class, "timestamp", false, "TIMESTAMP");
        public final static Property IsNew = new Property(6, Boolean.class, "isNew", false, "IS_NEW");
        public final static Property IsUpdated = new Property(7, Boolean.class, "isUpdated", false, "IS_UPDATED");
        public final static Property IsDeleted = new Property(8, Boolean.class, "isDeleted", false, "IS_DELETED");
    };


    public SensorCalendarEventReminderDao(DaoConfig config) {
        super(config);
    }
    
    public SensorCalendarEventReminderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SENSOR_CALENDAR_EVENT_REMINDER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"REMINDER_ID\" INTEGER," + // 1: reminderId
                "\"EVENT_ID\" INTEGER," + // 2: eventId
                "\"METHOD\" INTEGER," + // 3: method
                "\"MINUTES\" INTEGER," + // 4: minutes
                "\"TIMESTAMP\" INTEGER," + // 5: timestamp
                "\"IS_NEW\" INTEGER," + // 6: isNew
                "\"IS_UPDATED\" INTEGER," + // 7: isUpdated
                "\"IS_DELETED\" INTEGER);"); // 8: isDeleted
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSOR_CALENDAR_EVENT_REMINDER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SensorCalendarEventReminder entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long reminderId = entity.getReminderId();
        if (reminderId != null) {
            stmt.bindLong(2, reminderId);
        }
 
        Long eventId = entity.getEventId();
        if (eventId != null) {
            stmt.bindLong(3, eventId);
        }
 
        Integer method = entity.getMethod();
        if (method != null) {
            stmt.bindLong(4, method);
        }
 
        Integer minutes = entity.getMinutes();
        if (minutes != null) {
            stmt.bindLong(5, minutes);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(6, timestamp);
        }
 
        Boolean isNew = entity.getIsNew();
        if (isNew != null) {
            stmt.bindLong(7, isNew ? 1L: 0L);
        }
 
        Boolean isUpdated = entity.getIsUpdated();
        if (isUpdated != null) {
            stmt.bindLong(8, isUpdated ? 1L: 0L);
        }
 
        Boolean isDeleted = entity.getIsDeleted();
        if (isDeleted != null) {
            stmt.bindLong(9, isDeleted ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SensorCalendarEventReminder readEntity(Cursor cursor, int offset) {
        SensorCalendarEventReminder entity = new SensorCalendarEventReminder( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // reminderId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // eventId
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // method
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // minutes
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // timestamp
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // isNew
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // isUpdated
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0 // isDeleted
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SensorCalendarEventReminder entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setReminderId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setEventId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setMethod(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setMinutes(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTimestamp(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setIsNew(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setIsUpdated(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setIsDeleted(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SensorCalendarEventReminder entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SensorCalendarEventReminder entity) {
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
