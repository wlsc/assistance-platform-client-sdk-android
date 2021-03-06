package de.tudarmstadt.informatik.tk.assistance.sdk.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "contact_email_sensor".
*/
public class DbContactEmailSensorDao extends AbstractDao<DbContactEmailSensor, Long> {

    public static final String TABLENAME = "contact_email_sensor";

    /**
     * Properties of entity DbContactEmailSensor.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property MailId = new Property(1, Long.class, "mailId", false, "MAIL_ID");
        public final static Property Address = new Property(2, String.class, "address", false, "ADDRESS");
        public final static Property Type = new Property(3, String.class, "type", false, "TYPE");
        public final static Property IsNew = new Property(4, Boolean.class, "isNew", false, "IS_NEW");
        public final static Property IsUpdated = new Property(5, Boolean.class, "isUpdated", false, "IS_UPDATED");
        public final static Property IsDeleted = new Property(6, Boolean.class, "isDeleted", false, "IS_DELETED");
        public final static Property Created = new Property(7, String.class, "created", false, "CREATED");
        public final static Property ContactId = new Property(8, long.class, "contactId", false, "CONTACT_ID");
        public final static Property DeviceId = new Property(9, long.class, "deviceId", false, "DEVICE_ID");
    }

    private DaoSession daoSession;

    private Query<DbContactEmailSensor> dbContactSensor_DbContactEmailSensorListQuery;

    public DbContactEmailSensorDao(DaoConfig config) {
        super(config);
    }
    
    public DbContactEmailSensorDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"contact_email_sensor\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"MAIL_ID\" INTEGER," + // 1: mailId
                "\"ADDRESS\" TEXT," + // 2: address
                "\"TYPE\" TEXT," + // 3: type
                "\"IS_NEW\" INTEGER," + // 4: isNew
                "\"IS_UPDATED\" INTEGER," + // 5: isUpdated
                "\"IS_DELETED\" INTEGER," + // 6: isDeleted
                "\"CREATED\" TEXT NOT NULL ," + // 7: created
                "\"CONTACT_ID\" INTEGER NOT NULL ," + // 8: contactId
                "\"DEVICE_ID\" INTEGER NOT NULL );"); // 9: deviceId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_contact_email_sensor__id ON contact_email_sensor" +
                " (\"_id\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_contact_email_sensor_CONTACT_ID ON contact_email_sensor" +
                " (\"CONTACT_ID\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_contact_email_sensor_DEVICE_ID ON contact_email_sensor" +
                " (\"DEVICE_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"contact_email_sensor\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DbContactEmailSensor entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long mailId = entity.getMailId();
        if (mailId != null) {
            stmt.bindLong(2, mailId);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
 
        Boolean isNew = entity.getIsNew();
        if (isNew != null) {
            stmt.bindLong(5, isNew ? 1L: 0L);
        }
 
        Boolean isUpdated = entity.getIsUpdated();
        if (isUpdated != null) {
            stmt.bindLong(6, isUpdated ? 1L: 0L);
        }
 
        Boolean isDeleted = entity.getIsDeleted();
        if (isDeleted != null) {
            stmt.bindLong(7, isDeleted ? 1L: 0L);
        }
        stmt.bindString(8, entity.getCreated());
        stmt.bindLong(9, entity.getContactId());
        stmt.bindLong(10, entity.getDeviceId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DbContactEmailSensor entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long mailId = entity.getMailId();
        if (mailId != null) {
            stmt.bindLong(2, mailId);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(4, type);
        }
 
        Boolean isNew = entity.getIsNew();
        if (isNew != null) {
            stmt.bindLong(5, isNew ? 1L: 0L);
        }
 
        Boolean isUpdated = entity.getIsUpdated();
        if (isUpdated != null) {
            stmt.bindLong(6, isUpdated ? 1L: 0L);
        }
 
        Boolean isDeleted = entity.getIsDeleted();
        if (isDeleted != null) {
            stmt.bindLong(7, isDeleted ? 1L: 0L);
        }
        stmt.bindString(8, entity.getCreated());
        stmt.bindLong(9, entity.getContactId());
        stmt.bindLong(10, entity.getDeviceId());
    }

    @Override
    protected final void attachEntity(DbContactEmailSensor entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DbContactEmailSensor readEntity(Cursor cursor, int offset) {
        DbContactEmailSensor entity = new DbContactEmailSensor( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // mailId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // type
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // isNew
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // isUpdated
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // isDeleted
            cursor.getString(offset + 7), // created
            cursor.getLong(offset + 8), // contactId
            cursor.getLong(offset + 9) // deviceId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DbContactEmailSensor entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMailId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIsNew(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setIsUpdated(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setIsDeleted(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setCreated(cursor.getString(offset + 7));
        entity.setContactId(cursor.getLong(offset + 8));
        entity.setDeviceId(cursor.getLong(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DbContactEmailSensor entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DbContactEmailSensor entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DbContactEmailSensor entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "dbContactEmailSensorList" to-many relationship of DbContactSensor. */
    public List<DbContactEmailSensor> _queryDbContactSensor_DbContactEmailSensorList(long contactId) {
        synchronized (this) {
            if (dbContactSensor_DbContactEmailSensorListQuery == null) {
                QueryBuilder<DbContactEmailSensor> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ContactId.eq(null));
                dbContactSensor_DbContactEmailSensorListQuery = queryBuilder.build();
            }
        }
        Query<DbContactEmailSensor> query = dbContactSensor_DbContactEmailSensorListQuery.forCurrentThread();
        query.setParameter(0, contactId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getDbContactSensorDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getDbDeviceDao().getAllColumns());
            builder.append(" FROM contact_email_sensor T");
            builder.append(" LEFT JOIN contact_sensor T0 ON T.\"CONTACT_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN device T1 ON T.\"DEVICE_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected DbContactEmailSensor loadCurrentDeep(Cursor cursor, boolean lock) {
        DbContactEmailSensor entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DbContactSensor dbContactSensor = loadCurrentOther(daoSession.getDbContactSensorDao(), cursor, offset);
         if(dbContactSensor != null) {
            entity.setDbContactSensor(dbContactSensor);
        }
        offset += daoSession.getDbContactSensorDao().getAllColumns().length;

        DbDevice dbDevice = loadCurrentOther(daoSession.getDbDeviceDao(), cursor, offset);
         if(dbDevice != null) {
            entity.setDbDevice(dbDevice);
        }

        return entity;    
    }

    public DbContactEmailSensor loadDeep(Long key) {
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
    public List<DbContactEmailSensor> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DbContactEmailSensor> list = new ArrayList<DbContactEmailSensor>(count);
        
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
    
    protected List<DbContactEmailSensor> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<DbContactEmailSensor> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
