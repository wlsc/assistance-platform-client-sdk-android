package de.tudarmstadt.informatik.tk.assistance.sdk.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLightSensor;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "light_sensor".
*/
public class DbLightSensorDao extends AbstractDao<DbLightSensor, Long> {

    public static final String TABLENAME = "light_sensor";

    /**
     * Properties of entity DbLightSensor.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Accuracy = new Property(1, Integer.class, "accuracy", false, "ACCURACY");
        public final static Property Value = new Property(2, Float.class, "value", false, "VALUE");
        public final static Property Created = new Property(3, String.class, "created", false, "CREATED");
        public final static Property DeviceId = new Property(4, Long.class, "deviceId", false, "DEVICE_ID");
    };

    private DaoSession daoSession;


    public DbLightSensorDao(DaoConfig config) {
        super(config);
    }
    
    public DbLightSensorDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"light_sensor\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ACCURACY\" INTEGER," + // 1: accuracy
                "\"VALUE\" REAL," + // 2: value
                "\"CREATED\" TEXT NOT NULL ," + // 3: created
                "\"DEVICE_ID\" INTEGER);"); // 4: deviceId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_light_sensor__id ON light_sensor" +
                " (\"_id\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_light_sensor_DEVICE_ID ON light_sensor" +
                " (\"DEVICE_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"light_sensor\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DbLightSensor entity) {
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
        stmt.bindString(4, entity.getCreated());
 
        Long deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindLong(5, deviceId);
        }
    }

    @Override
    protected void attachEntity(DbLightSensor entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DbLightSensor readEntity(Cursor cursor, int offset) {
        DbLightSensor entity = new DbLightSensor( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // accuracy
            cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2), // value
            cursor.getString(offset + 3), // created
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // deviceId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DbLightSensor entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccuracy(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getFloat(offset + 2));
        entity.setCreated(cursor.getString(offset + 3));
        entity.setDeviceId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DbLightSensor entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DbLightSensor entity) {
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
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDbDeviceDao().getAllColumns());
            builder.append(" FROM light_sensor T");
            builder.append(" LEFT JOIN device T0 ON T.\"DEVICE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DbLightSensor loadCurrentDeep(Cursor cursor, boolean lock) {
        DbLightSensor entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DbDevice dbDevice = loadCurrentOther(daoSession.getDbDeviceDao(), cursor, offset);
        entity.setDbDevice(dbDevice);

        return entity;    
    }

    public DbLightSensor loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<DbLightSensor> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DbLightSensor> list = new ArrayList<DbLightSensor>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<DbLightSensor> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DbLightSensor> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}