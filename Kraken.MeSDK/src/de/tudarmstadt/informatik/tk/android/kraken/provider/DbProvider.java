
package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.Settings;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoMaster;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDevice;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDeviceDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUser;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUserDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.AccelerometerSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.ForegroundEventRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.MotionActivityEventRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.PositionSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.util.db.DbAssistanceOpenHelper;

/**
 * Singleton database provider
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class DbProvider {

    private static final String TAG = DbProvider.class.getSimpleName();

    private Context mContext;
    private static DbProvider INSTANCE;

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * DAOs
     */
    private static DbUserDao dbUserDao;
    private static DbDeviceDao dbDeviceDao;
    private static DbAccelerometerSensorDao dbAccelerometerSensorDao;
    private static DbPositionSensorDao dbPositionSensorDao;
    private static DbMotionActivityEventDao dbMotionActivityEventDao;
    private static DbForegroundEventDao dbForegroundEventDao;

    /**
     * Lists with transmitted db objects to remove them after
     */
    private List<DbAccelerometerSensor> dbAccelerometerSensors;
    private List<DbPositionSensor> dbPositionSensors;
    private List<DbMotionActivityEvent> dbMotionActivityEvents;
    private List<DbForegroundEvent> dbForegroundEvents;

    /**
     * Constructor
     *
     * @param context
     */
    private DbProvider(Context context) {

        this.mContext = context;

        DbAssistanceOpenHelper helper = new DbAssistanceOpenHelper(context, Settings.DATABASE_NAME, null);
        mDb = helper.getWritableDatabase();

        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);

        if (dbUserDao == null) {
            dbUserDao = getDaoSession().getDbUserDao();
        }

        if (dbDeviceDao == null) {
            dbDeviceDao = getDaoSession().getDbDeviceDao();
        }

        if (dbAccelerometerSensorDao == null) {
            dbAccelerometerSensorDao = getDaoSession().getDbAccelerometerSensorDao();
        }

        if (dbPositionSensorDao == null) {
            dbPositionSensorDao = getDaoSession().getDbPositionSensorDao();
        }

        if (dbMotionActivityEventDao == null) {
            dbMotionActivityEventDao = getDaoSession().getDbMotionActivityEventDao();
        }

        if (dbForegroundEventDao == null) {
            dbForegroundEventDao = getDaoSession().getDbForegroundEventDao();
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
     * @param registrationToken
     */
    public boolean saveRegistrationTokenToDb(String registrationToken) {

        Log.d(TAG, "Saving GCM registration token to DB...");

        if (registrationToken == null) {
            Log.e(TAG, "GCM registration token IS null!");
            return false;
        }

        final String userToken = PreferenceManager.getInstance(mContext).getUserToken();
        final long serverDeviceId = PreferenceManager.getInstance(mContext).getServerDeviceId();

        DbUser user = dbUserDao
                .queryBuilder()
                .where(DbUserDao.Properties.Token.eq(userToken))
                .build()
                .unique();

        if (user == null) {
            Log.d(TAG, "No such user found! Token: " + userToken);
            return false;
        }

        DbDevice device = dbDeviceDao
                .queryBuilder()
                .where(DbDeviceDao.Properties.UserId.eq(user.getId()))
                .where(DbDeviceDao.Properties.ServerDeviceId.eq(serverDeviceId))
                .build()
                .unique();

        if (device == null) {
            Log.d(TAG, "Not found any device with id: " + serverDeviceId);
            return false;
        } else {

            device.setGcmRegistrationToken(registrationToken);

            dbDeviceDao.update(device);

            Log.d(TAG, "Finished saving GCM token");

            return true;
        }
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
                    if (dbForegroundEvents != null) {
                        dbForegroundEventDao.deleteInTx(dbForegroundEvents);
                    }
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

        List<Sensor> foregroundEventList = getForegroundEventEntries(numberOfElements);
        entries.put(SensorType.FOREGROUND, foregroundEventList);

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
                motionActivityEventRequest.setTypeStr(SensorType.getApiName(SensorType.MOTION_ACTIVITY));
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

    /**
     * Returns number of first entries of foreground events from database
     *
     * @param numberElements
     * @return
     */
    private List<Sensor> getForegroundEventEntries(int numberElements) {

        List<Sensor> result = new LinkedList<>();

        dbForegroundEvents = dbForegroundEventDao
                .queryBuilder()
                .limit(numberElements)
                .build()
                .list();

        if (dbForegroundEvents != null) {

            for (int i = 0; i < dbForegroundEvents.size(); i++) {

                DbForegroundEvent sensor = dbForegroundEvents.get(i);

                ForegroundEventRequest foregroundEventRequest = new ForegroundEventRequest();

                foregroundEventRequest.setType(SensorType.FOREGROUND);
                foregroundEventRequest.setTypeStr(SensorType.getApiName(SensorType.FOREGROUND));
                foregroundEventRequest.setAppName(sensor.getAppName());
                foregroundEventRequest.setActivityLabel(sensor.getActivityLabel());
                foregroundEventRequest.setClassName(sensor.getClassName());
                foregroundEventRequest.setColor(sensor.getColor());
                foregroundEventRequest.setKeystrokes(sensor.getKeystrokes());
                foregroundEventRequest.setPackageName(sensor.getPackageName());
                foregroundEventRequest.setUrl(sensor.getUrl());
                foregroundEventRequest.setEventType(SensorType.getName(sensor.getEventType(), mContext.getResources()));
                foregroundEventRequest.setCreated(sensor.getCreated());

                result.add(foregroundEventRequest);
            }
        }

        return result;
    }

    /**
     * Generic insert for db events/sensors
     *
     * @param sensor
     * @param type
     */
    public long insertEntry(IDbSensor sensor, int type) {

        if (sensor == null) {
            return -1;
        }

        long result = -1;

        switch (type) {
            case SensorType.FOREGROUND:
                result = dbForegroundEventDao.insertOrReplace((DbForegroundEvent) sensor);
                break;
        }

        return result;
    }
}
