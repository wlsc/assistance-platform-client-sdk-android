package de.tudarmstadt.informatik.tk.android.assistance.sdk.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoMaster;

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
