package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.ConnectionEvent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "connection_event".
*/
public class ConnectionEventDao extends AbstractDao<ConnectionEvent, Long> {

    public static final String TABLENAME = "connection_event";

    /**
     * Properties of entity ConnectionEvent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property IsWifi = new Property(1, Boolean.class, "isWifi", false, "IS_WIFI");
        public final static Property IsMobile = new Property(2, Boolean.class, "isMobile", false, "IS_MOBILE");
        public final static Property Created = new Property(3, String.class, "created", false, "CREATED");
    };


    public ConnectionEventDao(DaoConfig config) {
        super(config);
    }
    
    public ConnectionEventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"connection_event\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"IS_WIFI\" INTEGER," + // 1: isWifi
                "\"IS_MOBILE\" INTEGER," + // 2: isMobile
                "\"CREATED\" TEXT NOT NULL );"); // 3: created
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_connection_event__id ON connection_event" +
                " (\"_id\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"connection_event\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ConnectionEvent entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Boolean isWifi = entity.getIsWifi();
        if (isWifi != null) {
            stmt.bindLong(2, isWifi ? 1L: 0L);
        }
 
        Boolean isMobile = entity.getIsMobile();
        if (isMobile != null) {
            stmt.bindLong(3, isMobile ? 1L: 0L);
        }
        stmt.bindString(4, entity.getCreated());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ConnectionEvent readEntity(Cursor cursor, int offset) {
        ConnectionEvent entity = new ConnectionEvent( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0, // isWifi
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // isMobile
            cursor.getString(offset + 3) // created
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ConnectionEvent entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setIsWifi(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1) != 0);
        entity.setIsMobile(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setCreated(cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ConnectionEvent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ConnectionEvent entity) {
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