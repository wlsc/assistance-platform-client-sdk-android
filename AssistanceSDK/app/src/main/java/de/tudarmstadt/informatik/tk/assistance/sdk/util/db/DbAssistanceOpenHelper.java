package de.tudarmstadt.informatik.tk.assistance.sdk.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoMaster;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Karsten Planz
 * @edited on 07.09.2015 by Wladimir Schmidt (wlsc.dev@gmail.com)
 */
public class DbAssistanceOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = DbAssistanceOpenHelper.class.getSimpleName();

    public DbAssistanceOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        DaoMaster.createAllTables(db, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "onUpgrade from version " + oldVersion + " to" + newVersion);

        // TODO: exchange drop table with reasonable update logic
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);

        switch (oldVersion) {

            case 1:
//                addColumn(db, SensorContactDao.TABLENAME, SensorContactDao.Properties.GlobalContactId, "LONG");
                break;
            default:
                return;
        }
    }
}