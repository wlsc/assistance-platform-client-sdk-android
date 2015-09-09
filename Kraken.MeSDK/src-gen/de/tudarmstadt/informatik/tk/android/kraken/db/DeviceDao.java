package de.tudarmstadt.informatik.tk.android.kraken.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import de.tudarmstadt.informatik.tk.android.kraken.db.Device;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "device".
*/
public class DeviceDao extends AbstractDao<Device, Long> {

    public static final String TABLENAME = "device";

    /**
     * Properties of entity Device.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Device_identifier = new Property(1, String.class, "device_identifier", false, "DEVICE_IDENTIFIER");
        public final static Property Os = new Property(2, String.class, "os", false, "OS");
        public final static Property Os_version = new Property(3, String.class, "os_version", false, "OS_VERSION");
        public final static Property Brand = new Property(4, String.class, "brand", false, "BRAND");
        public final static Property Model = new Property(5, String.class, "model", false, "MODEL");
        public final static Property Created = new Property(6, String.class, "created", false, "CREATED");
        public final static Property Login_id = new Property(7, Long.class, "login_id", false, "LOGIN_ID");
        public final static Property User_id = new Property(8, Long.class, "user_id", false, "USER_ID");
    };

    private DaoSession daoSession;

    private Query<Device> login_DeviceListQuery;
    private Query<Device> user_DeviceListQuery;

    public DeviceDao(DaoConfig config) {
        super(config);
    }
    
    public DeviceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"device\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DEVICE_IDENTIFIER\" TEXT," + // 1: device_identifier
                "\"OS\" TEXT," + // 2: os
                "\"OS_VERSION\" TEXT," + // 3: os_version
                "\"BRAND\" TEXT," + // 4: brand
                "\"MODEL\" TEXT," + // 5: model
                "\"CREATED\" TEXT NOT NULL ," + // 6: created
                "\"LOGIN_ID\" INTEGER," + // 7: login_id
                "\"USER_ID\" INTEGER);"); // 8: user_id
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_device__id ON device" +
                " (\"_id\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_device_LOGIN_ID ON device" +
                " (\"LOGIN_ID\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_device_USER_ID ON device" +
                " (\"USER_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"device\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Device entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String device_identifier = entity.getDevice_identifier();
        if (device_identifier != null) {
            stmt.bindString(2, device_identifier);
        }
 
        String os = entity.getOs();
        if (os != null) {
            stmt.bindString(3, os);
        }
 
        String os_version = entity.getOs_version();
        if (os_version != null) {
            stmt.bindString(4, os_version);
        }
 
        String brand = entity.getBrand();
        if (brand != null) {
            stmt.bindString(5, brand);
        }
 
        String model = entity.getModel();
        if (model != null) {
            stmt.bindString(6, model);
        }
        stmt.bindString(7, entity.getCreated());
 
        Long login_id = entity.getLogin_id();
        if (login_id != null) {
            stmt.bindLong(8, login_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(9, user_id);
        }
    }

    @Override
    protected void attachEntity(Device entity) {
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
    public Device readEntity(Cursor cursor, int offset) {
        Device entity = new Device( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // device_identifier
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // os
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // os_version
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // brand
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // model
            cursor.getString(offset + 6), // created
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // login_id
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // user_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Device entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDevice_identifier(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setOs(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOs_version(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBrand(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setModel(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCreated(cursor.getString(offset + 6));
        entity.setLogin_id(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setUser_id(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Device entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Device entity) {
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
    
    /** Internal query to resolve the "deviceList" to-many relationship of Login. */
    public List<Device> _queryLogin_DeviceList(Long login_id) {
        synchronized (this) {
            if (login_DeviceListQuery == null) {
                QueryBuilder<Device> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Login_id.eq(null));
                login_DeviceListQuery = queryBuilder.build();
            }
        }
        Query<Device> query = login_DeviceListQuery.forCurrentThread();
        query.setParameter(0, login_id);
        return query.list();
    }

    /** Internal query to resolve the "deviceList" to-many relationship of User. */
    public List<Device> _queryUser_DeviceList(Long user_id) {
        synchronized (this) {
            if (user_DeviceListQuery == null) {
                QueryBuilder<Device> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_DeviceListQuery = queryBuilder.build();
            }
        }
        Query<Device> query = user_DeviceListQuery.forCurrentThread();
        query.setParameter(0, user_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getLoginDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM device T");
            builder.append(" LEFT JOIN login T0 ON T.\"LOGIN_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN user T1 ON T.\"USER_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Device loadCurrentDeep(Cursor cursor, boolean lock) {
        Device entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Login login = loadCurrentOther(daoSession.getLoginDao(), cursor, offset);
        entity.setLogin(login);
        offset += daoSession.getLoginDao().getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public Device loadDeep(Long key) {
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
    public List<Device> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Device> list = new ArrayList<Device>(count);
        
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
    
    protected List<Device> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Device> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}