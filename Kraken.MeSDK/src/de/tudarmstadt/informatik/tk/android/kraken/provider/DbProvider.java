
package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.android.kraken.Settings;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoMaster;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.util.db.DbAssistanceOpenHelper;

/**
 * Singleton database manager
 */
public class DbProvider {

    private static DbProvider INSTANCE;

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * Constructor
     *
     * @param context
     */
    private DbProvider(Context context) {

        DbAssistanceOpenHelper helper = new DbAssistanceOpenHelper(context, Settings.DATABASE_NAME, null);
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
    public static DbProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new DbProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Hard reset, really hard!
     */
    public void hardReset() {
        DaoMaster.dropAllTables(mDb, true);
        INSTANCE = null;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }
}
