package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.greenrobot.dao.Property;

/**
 * @author Karsten Planz
 * @edited on 07.09.2015 by Wladimir Schmidt (wlsc.dev@gmail.com)
 */
public class DBKrakenOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = DBKrakenOpenHelper.class.getSimpleName();

    public DBKrakenOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
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

    /**
     * Adds one more table to database
     *
     * @param db
     * @param table
     * @param property
     * @param type
     */
    public void addColumn(SQLiteDatabase db, String table, Property property, String type) {
        db.execSQL("ALTER TABLE '" + table + "' ADD '" + property.columnName + "' " + type);
    }

    /**
     * Renames a given table
     *
     * @param db
     * @param tableOld
     * @param tableNew
     */
    public void renameTable(SQLiteDatabase db, String tableOld, String tableNew) {
        db.execSQL("ALTER TABLE '" + tableOld + "' RENAME TO '" + tableNew + "'");
    }
}
