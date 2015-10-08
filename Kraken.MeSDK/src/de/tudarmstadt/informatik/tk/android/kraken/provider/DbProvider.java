
package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.android.kraken.Settings;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoMaster;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.AccelerometerSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.MotionActivityEventRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.PositionSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.util.db.DbAssistanceOpenHelper;

/**
 * Singleton database manager
 */
public class DbProvider {

    private static final String TAG = DbProvider.class.getSimpleName();

    private static DbProvider INSTANCE;

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static DbAccelerometerSensorDao dbAccelerometerSensorDao;
    private static DbPositionSensorDao dbPositionSensorDao;
    private static DbMotionActivityEventDao dbMotionActivityEventDao;

    private List<DbAccelerometerSensor> dbAccelerometerSensors;
    private List<DbPositionSensor> dbPositionSensors;
    private List<DbMotionActivityEvent> dbMotionActivityEvents;

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

        if (dbAccelerometerSensorDao == null) {
            dbAccelerometerSensorDao = getDaoSession().getDbAccelerometerSensorDao();
        }

        if (dbPositionSensorDao == null) {
            dbPositionSensorDao = getDaoSession().getDbPositionSensorDao();
        }

        if (dbMotionActivityEventDao == null) {
            dbMotionActivityEventDao = getDaoSession().getDbMotionActivityEventDao();
        }
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

    /**
     * Saves device GCM registration id to db
     *
     * @param deviceId
     * @param registrationToken
     */
    public void saveRegistrationTokenToDb(long deviceId, String registrationToken) {

        Log.d(TAG, "Saving GCM registration token to DB...");

    }

    /**
     * Removes successful transmitted entries from database
     *
     * @param events
     */
    public void removeDbSentEvents(SparseArrayCompat<List<Sensor>> events) {

        Log.d(TAG, "Removing sent events from db...");

        for (int i = 0; i < events.size(); i++) {

            int sensorType = events.keyAt(i);

            switch (sensorType) {
                case SensorType.ACCELEROMETER:
                    if (dbAccelerometerSensors != null) {
                        dbAccelerometerSensorDao.deleteInTx(dbAccelerometerSensors);
                    }
                    break;
                case SensorType.LOCATION:
                    if (dbPositionSensors != null) {
                        dbPositionSensorDao.deleteInTx(dbPositionSensors);
                    }
                    break;
                case SensorType.MOTION_ACTIVITY:
                    if (dbMotionActivityEvents != null) {
                        dbMotionActivityEventDao.deleteInTx(dbMotionActivityEvents);
                    }
                    break;
                case SensorType.FOREGROUND:
                    break;
            }
        }

        Log.d(TAG, "Finished removing data from db");
    }

    /**
     * Returns events for upload to server
     *
     * @param numberOfElements
     * @return
     */
    public SparseArrayCompat<List<Sensor>> getEntriesForUpload(int numberOfElements) {

        SparseArrayCompat<List<Sensor>> entries = new SparseArrayCompat<>();

        List<Sensor> accelerometerList = getAccelerometerEntries(numberOfElements);
        entries.put(SensorType.ACCELEROMETER, accelerometerList);

        List<Sensor> positionList = getPositionEntries(numberOfElements);
        entries.put(SensorType.LOCATION, positionList);

        List<Sensor> motionActivityList = getMotionActivityEntries(numberOfElements);
        entries.put(SensorType.MOTION_ACTIVITY, motionActivityList);

        return entries;
    }

    /**
     * Harvest particular number of accelerometer sensor entries from database
     *
     * @return
     */
    private List<Sensor> getAccelerometerEntries(int numberElements) {

        List<Sensor> result = new LinkedList<>();

        dbAccelerometerSensors = dbAccelerometerSensorDao
                .queryBuilder()
                .limit(numberElements)
                .build()
                .list();

        if (dbAccelerometerSensors != null) {

            for (int i = 0; i < dbAccelerometerSensors.size(); i++) {

                DbAccelerometerSensor sensor = dbAccelerometerSensors.get(i);

                AccelerometerSensorRequest accelerometerSensorRequest = new AccelerometerSensorRequest();

                accelerometerSensorRequest.setType(SensorType.ACCELEROMETER);
                accelerometerSensorRequest.setTypeStr(SensorType.getApiName(SensorType.ACCELEROMETER));
                accelerometerSensorRequest.setX(sensor.getX());
                accelerometerSensorRequest.setY(sensor.getY());
                accelerometerSensorRequest.setZ(sensor.getZ());
                accelerometerSensorRequest.setAccuracy(sensor.getAccuracy());
                accelerometerSensorRequest.setCreated(sensor.getCreated());

                result.add(accelerometerSensorRequest);
            }
        }

        return result;
    }

    /**
     * Returns number of first entries of position events from database
     *
     * @return
     */
    private List<Sensor> getPositionEntries(int numberElements) {

        List<Sensor> result = new LinkedList<>();

        dbPositionSensors = dbPositionSensorDao
                .queryBuilder()
                .limit(numberElements)
                .build()
                .list();

        if (dbPositionSensors != null) {

            for (int i = 0; i < dbPositionSensors.size(); i++) {

                DbPositionSensor sensor = dbPositionSensors.get(i);

                PositionSensorRequest positionSensorRequest = new PositionSensorRequest();

                positionSensorRequest.setType(SensorType.LOCATION);
                positionSensorRequest.setTypeStr(SensorType.getApiName(SensorType.LOCATION));
                positionSensorRequest.setLatitude(sensor.getLatitude());
                positionSensorRequest.setLongitude(sensor.getLongitude());
                positionSensorRequest.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
                positionSensorRequest.setAccuracyVertical(sensor.getAccuracyVertical());
                positionSensorRequest.setAltitude(sensor.getAltitude());
                positionSensorRequest.setSpeed(sensor.getSpeed());
                positionSensorRequest.setCreated(sensor.getCreated());

                result.add(positionSensorRequest);
            }
        }

        return result;
    }

    /**
     * Returns number of first entries of motion activity events from database
     *
     * @return
     */
    private List<Sensor> getMotionActivityEntries(int numberElements) {

        List<Sensor> result = new LinkedList<>();

        dbMotionActivityEvents = dbMotionActivityEventDao
                .queryBuilder()
                .limit(numberElements)
                .build()
                .list();

        if (dbMotionActivityEvents != null) {

            for (int i = 0; i < dbMotionActivityEvents.size(); i++) {

                DbMotionActivityEvent sensor = dbMotionActivityEvents.get(i);

                MotionActivityEventRequest motionActivityEventRequest = new MotionActivityEventRequest();

                motionActivityEventRequest.setType(SensorType.MOTION_ACTIVITY);
                motionActivityEventRequest.setTypeStr(SensorType.getApiName(SensorType.LOCATION));
                motionActivityEventRequest.setRunning(sensor.getRunning());
                motionActivityEventRequest.setStationary(sensor.getStationary());
                motionActivityEventRequest.setCycling(sensor.getCycling());
                motionActivityEventRequest.setWalking(sensor.getWalking());
                motionActivityEventRequest.setDriving(sensor.getDriving());
                motionActivityEventRequest.setOnFoot(sensor.getOnFoot());
                motionActivityEventRequest.setTilting(sensor.getTilting());
                motionActivityEventRequest.setUnknown(sensor.getUnknown());
                motionActivityEventRequest.setCreated(sensor.getCreated());

                result.add(motionActivityEventRequest);
            }
        }

        return result;
    }
}
