package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorConnection;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SENSOR_CONNECTION".
*/
public class SensorConnectionDao extends AbstractDao<SensorConnection, Long> {

    public static final String TABLENAME = "SENSOR_CONNECTION";

    /**
     * Properties of entity SensorConnection.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ActiveNetwork = new Property(1, Integer.class, "activeNetwork", false, "ACTIVE_NETWORK");
        public final static Property MobileIsAvailable = new Property(2, Boolean.class, "mobileIsAvailable", false, "MOBILE_IS_AVAILABLE");
        public final static Property MobileState = new Property(3, Integer.class, "mobileState", false, "MOBILE_STATE");
        public final static Property WlanState = new Property(4, Integer.class, "wlanState", false, "WLAN_STATE");
        public final static Property WlanIsAvailable = new Property(5, Boolean.class, "wlanIsAvailable", false, "WLAN_IS_AVAILABLE");
        public final static Property Timestamp = new Property(6, Long.class, "timestamp", false, "TIMESTAMP");
    };


    public SensorConnectionDao(DaoConfig config) {
        super(config);
    }
    
    public SensorConnectionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SENSOR_CONNECTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ACTIVE_NETWORK\" INTEGER," + // 1: activeNetwork
                "\"MOBILE_IS_AVAILABLE\" INTEGER," + // 2: mobileIsAvailable
                "\"MOBILE_STATE\" INTEGER," + // 3: mobileState
                "\"WLAN_STATE\" INTEGER," + // 4: wlanState
                "\"WLAN_IS_AVAILABLE\" INTEGER," + // 5: wlanIsAvailable
                "\"TIMESTAMP\" INTEGER);"); // 6: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSOR_CONNECTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SensorConnection entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer activeNetwork = entity.getActiveNetwork();
        if (activeNetwork != null) {
            stmt.bindLong(2, activeNetwork);
        }
 
        Boolean mobileIsAvailable = entity.getMobileIsAvailable();
        if (mobileIsAvailable != null) {
            stmt.bindLong(3, mobileIsAvailable ? 1L: 0L);
        }
 
        Integer mobileState = entity.getMobileState();
        if (mobileState != null) {
            stmt.bindLong(4, mobileState);
        }
 
        Integer wlanState = entity.getWlanState();
        if (wlanState != null) {
            stmt.bindLong(5, wlanState);
        }
 
        Boolean wlanIsAvailable = entity.getWlanIsAvailable();
        if (wlanIsAvailable != null) {
            stmt.bindLong(6, wlanIsAvailable ? 1L: 0L);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(7, timestamp);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SensorConnection readEntity(Cursor cursor, int offset) {
        SensorConnection entity = new SensorConnection( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // activeNetwork
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // mobileIsAvailable
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // mobileState
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // wlanState
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // wlanIsAvailable
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // timestamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SensorConnection entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setActiveNetwork(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setMobileIsAvailable(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setMobileState(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setWlanState(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setWlanIsAvailable(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setTimestamp(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SensorConnection entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SensorConnection entity) {
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
