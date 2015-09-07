
package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoMaster;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DBKrakenOpenHelper;

/**
 * Singleton database manager
 */
public class DatabaseManager {

    private static final String DB_NAME = "assistance.sqlite";

    private static DatabaseManager manager;

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * Constructor
     *
     * @param context
     */
    private DatabaseManager(Context context) {

        DBKrakenOpenHelper helper = new DBKrakenOpenHelper(context, DB_NAME, null);
        mDb = helper.getWritableDatabase();

        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);
    }

    /**
     * Get database singleton
     *
     * @param context
     * @return
     */
    public static DatabaseManager getInstance(Context context) {
        if (manager == null)
            manager = new DatabaseManager(context);

        return manager;
    }

    /**
     * Hard reset
     */
    public void hardReset() {
        DaoMaster.dropAllTables(mDb, true);
        manager = null;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }

}
