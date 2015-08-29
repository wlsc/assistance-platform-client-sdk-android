package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorAccelerometer;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SENSOR_ACCELEROMETER".
*/
public class SensorAccelerometerDao extends AbstractDao<SensorAccelerometer, Long> {

    public static final String TABLENAME = "SENSOR_ACCELEROMETER";

    /**
     * Properties of entity SensorAccelerometer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Accuracy = new Property(1, Integer.class, "accuracy", false, "ACCURACY");
        public final static Property AccelerationX = new Property(2, Float.class, "accelerationX", false, "ACCELERATION_X");
        public final static Property AccelerationY = new Property(3, Float.class, "accelerationY", false, "ACCELERATION_Y");
        public final static Property AccelerationZ = new Property(4, Float.class, "accelerationZ", false, "ACCELERATION_Z");
        public final static Property Timestamp = new Property(5, Long.class, "timestamp", false, "TIMESTAMP");
    };


    public SensorAccelerometerDao(DaoConfig config) {
        super(config);
    }
    
    public SensorAccelerometerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SENSOR_ACCELEROMETER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ACCURACY\" INTEGER," + // 1: accuracy
                "\"ACCELERATION_X\" REAL," + // 2: accelerationX
                "\"ACCELERATION_Y\" REAL," + // 3: accelerationY
                "\"ACCELERATION_Z\" REAL," + // 4: accelerationZ
                "\"TIMESTAMP\" INTEGER);"); // 5: timestamp
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSOR_ACCELEROMETER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SensorAccelerometer entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer accuracy = entity.getAccuracy();
        if (accuracy != null) {
            stmt.bindLong(2, accuracy);
        }
 
        Float accelerationX = entity.getAccelerationX();
        if (accelerationX != null) {
            stmt.bindDouble(3, accelerationX);
        }
 
        Float accelerationY = entity.getAccelerationY();
        if (accelerationY != null) {
            stmt.bindDouble(4, accelerationY);
        }
 
        Float accelerationZ = entity.getAccelerationZ();
        if (accelerationZ != null) {
            stmt.bindDouble(5, accelerationZ);
        }
 
        Long timestamp = entity.getTimestamp();
        if (timestamp != null) {
            stmt.bindLong(6, timestamp);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SensorAccelerometer readEntity(Cursor cursor, int offset) {
        SensorAccelerometer entity = new SensorAccelerometer( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // accuracy
            cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2), // accelerationX
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // accelerationY
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // accelerationZ
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // timestamp
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SensorAccelerometer entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccuracy(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAccelerationX(cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2));
        entity.setAccelerationY(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setAccelerationZ(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setTimestamp(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SensorAccelerometer entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SensorAccelerometer entity) {
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
