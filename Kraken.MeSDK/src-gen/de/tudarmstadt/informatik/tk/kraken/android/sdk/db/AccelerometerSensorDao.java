package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.AccelerometerSensor;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "accelerometer_sensor".
*/
public class AccelerometerSensorDao extends AbstractDao<AccelerometerSensor, Long> {

    public static final String TABLENAME = "accelerometer_sensor";

    /**
     * Properties of entity AccelerometerSensor.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property X = new Property(1, Double.class, "x", false, "X");
        public final static Property Y = new Property(2, Double.class, "y", false, "Y");
        public final static Property Z = new Property(3, Double.class, "z", false, "Z");
        public final static Property Created = new Property(4, String.class, "created", false, "CREATED");
        public final static Property Accuracy = new Property(5, Integer.class, "accuracy", false, "ACCURACY");
    };


    public AccelerometerSensorDao(DaoConfig config) {
        super(config);
    }
    
    public AccelerometerSensorDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"accelerometer_sensor\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"X\" REAL," + // 1: x
                "\"Y\" REAL," + // 2: y
                "\"Z\" REAL," + // 3: z
                "\"CREATED\" TEXT NOT NULL ," + // 4: created
                "\"ACCURACY\" INTEGER);"); // 5: accuracy
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_accelerometer_sensor__id ON accelerometer_sensor" +
                " (\"_id\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"accelerometer_sensor\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AccelerometerSensor entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Double x = entity.getX();
        if (x != null) {
            stmt.bindDouble(2, x);
        }
 
        Double y = entity.getY();
        if (y != null) {
            stmt.bindDouble(3, y);
        }
 
        Double z = entity.getZ();
        if (z != null) {
            stmt.bindDouble(4, z);
        }
        stmt.bindString(5, entity.getCreated());
 
        Integer accuracy = entity.getAccuracy();
        if (accuracy != null) {
            stmt.bindLong(6, accuracy);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AccelerometerSensor readEntity(Cursor cursor, int offset) {
        AccelerometerSensor entity = new AccelerometerSensor( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // x
            cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2), // y
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // z
            cursor.getString(offset + 4), // created
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // accuracy
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AccelerometerSensor entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setX(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setY(cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2));
        entity.setZ(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setCreated(cursor.getString(offset + 4));
        entity.setAccuracy(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AccelerometerSensor entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AccelerometerSensor entity) {
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
