package de.tudarmstadt.informatik.tk.android.kraken.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "foreground_event".
*/
public class DbForegroundEventDao extends AbstractDao<DbForegroundEvent, Long> {

    public static final String TABLENAME = "foreground_event";

    /**
     * Properties of entity DbForegroundEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PackageName = new Property(1, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property AppName = new Property(2, String.class, "appName", false, "APP_NAME");
        public final static Property ClassName = new Property(3, String.class, "className", false, "CLASS_NAME");
        public final static Property ActivityLabel = new Property(4, String.class, "activityLabel", false, "ACTIVITY_LABEL");
        public final static Property Color = new Property(5, String.class, "color", false, "COLOR");
        public final static Property Url = new Property(6, String.class, "url", false, "URL");
        public final static Property EventType = new Property(7, Integer.class, "eventType", false, "EVENT_TYPE");
        public final static Property Keystrokes = new Property(8, Integer.class, "keystrokes", false, "KEYSTROKES");
        public final static Property Created = new Property(9, String.class, "created", false, "CREATED");
    };


    public DbForegroundEventDao(DaoConfig config) {
        super(config);
    }
    
    public DbForegroundEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"foreground_event\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PACKAGE_NAME\" TEXT," + // 1: packageName
                "\"APP_NAME\" TEXT," + // 2: appName
                "\"CLASS_NAME\" TEXT," + // 3: className
                "\"ACTIVITY_LABEL\" TEXT," + // 4: activityLabel
                "\"COLOR\" TEXT," + // 5: color
                "\"URL\" TEXT," + // 6: url
                "\"EVENT_TYPE\" INTEGER," + // 7: eventType
                "\"KEYSTROKES\" INTEGER," + // 8: keystrokes
                "\"CREATED\" TEXT NOT NULL );"); // 9: created
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_foreground_event__id ON foreground_event" +
                " (\"_id\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"foreground_event\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DbForegroundEvent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(2, packageName);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(3, appName);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(4, className);
        }
 
        String activityLabel = entity.getActivityLabel();
        if (activityLabel != null) {
            stmt.bindString(5, activityLabel);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(6, color);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(7, url);
        }
 
        Integer eventType = entity.getEventType();
        if (eventType != null) {
            stmt.bindLong(8, eventType);
        }
 
        Integer keystrokes = entity.getKeystrokes();
        if (keystrokes != null) {
            stmt.bindLong(9, keystrokes);
        }
        stmt.bindString(10, entity.getCreated());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DbForegroundEvent readEntity(Cursor cursor, int offset) {
        DbForegroundEvent entity = new DbForegroundEvent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // packageName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // appName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // className
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // activityLabel
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // color
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // url
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // eventType
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // keystrokes
            cursor.getString(offset + 9) // created
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DbForegroundEvent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPackageName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAppName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setClassName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setActivityLabel(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setColor(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEventType(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setKeystrokes(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setCreated(cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DbForegroundEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DbForegroundEvent entity) {
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
