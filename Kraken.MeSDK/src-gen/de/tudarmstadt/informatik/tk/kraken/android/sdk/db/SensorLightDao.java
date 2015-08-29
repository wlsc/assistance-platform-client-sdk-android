package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorLight;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SENSOR_LIGHT".
*/
public class SensorLightDao extends AbstractDao<SensorLight, Long> {

    public static final String TABLENAME = "SENSOR_LIGHT";

    /**
     * Properties of entity SensorLight.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Accuracy = new Property(1, Integer.class, "accuracy", false, "ACCURACY");
        public final static Property Value = new Property(2, Float.class, "value", false, "VALUE");
        public final static Property Timestamp = new Property(3, Long.class, "timestamp", false, "TIMESTAMP");
    };


    public SensorLightDao(DaoConfig config) {
        super(config);
    }
    
    public SensorLightDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SENSOR_LIGHT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ACCURACY\" INTEGER," + // 1: accuracy
                "\"VALUE\" REAL," + // 2: value
                "\"TIMESTAMP\" INTEGER);"); // 3: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSOR_LIGHT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SensorLight entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer accuracy = entity.getAccuracy();
        if (accuracy != null) {
            stmt.bindLong(2, accuracy);
        }
 
        Float value = entity.getValue();
        if (value != null) {
            stmt.bindDouble(3, value);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(4, timestamp);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SensorLight readEntity(Cursor cursor, int offset) {
        SensorLight entity = new SensorLight( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // accuracy
            cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2), // value
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // timestamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SensorLight entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccuracy(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2));
        entity.setTimestamp(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SensorLight entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SensorLight entity) {
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
