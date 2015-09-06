package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.greenrobot.dao.Property;

/**
 * @author Karsten Planz
 */
public class KrakenOpenHelper extends DaoMaster.OpenHelper {

    public KrakenOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("kraken", "onUpgrade: " + oldVersion + ", " + newVersion);
        DaoMaster.createAllTables(db, true);

        switch (oldVersion) {
            // FIXME I guess this is some compatibility stuff, maybe it can go away?
            case 1:
//                addColumn(db, SensorContactDao.TABLENAME, SensorContactDao.Properties.GlobalContactId, "LONG");
                break;
            default:
                return;
        }
    }

    public void addColumn(SQLiteDatabase db, String table, Property property, String type) {
        db.execSQL("ALTER TABLE '" + table + "' ADD '" + property.columnName + "' " + type);
    }

    public void renameTable(SQLiteDatabase db, String tableOld, String tableNew) {
        db.execSQL("ALTER TABLE '" + tableOld + "' RENAME TO '" + tableNew + "'");
    }
}
