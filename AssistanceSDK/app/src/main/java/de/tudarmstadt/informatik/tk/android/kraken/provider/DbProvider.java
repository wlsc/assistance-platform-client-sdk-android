
package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.android.kraken.Config;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoMaster;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbCallLogEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDevice;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDeviceDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbGyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbLightSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModule;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallation;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallationDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNetworkTrafficEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNews;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNewsDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUser;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUserDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbWifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.AccelerometerSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.ForegroundEventRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.LocationSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.MotionActivityEventRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.CallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MotionActivityEvent;
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
    // User
    private DbUserDao userDao;

    // Device
    private DbDeviceDao deviceDao;

    // Modules
    private DbModuleDao moduleDao;
    private DbModuleCapabilityDao moduleCapabilityDao;
    private DbModuleInstallationDao moduleInstallationDao;

    // Assistance news
    private DbNewsDao newsDao;

    // Sensors / Events
    private DbAccelerometerSensorDao accelerometerSensorDao;
    private DbGyroscopeSensorDao gyroscopeSensorDao;
    private DbMagneticFieldSensorDao magneticFieldSensorDao;
    private DbPositionSensorDao positionSensorDao;
    private DbMotionActivityEventDao motionActivityEventDao;
    private DbForegroundEventDao foregroundEventDao;
    private DbLightSensorDao lightSensorDao;
    private DbConnectionEventDao connectionEventDao;
    private DbMobileConnectionEventDao mobileConnectionEventDao;
    private DbWifiConnectionEventDao wifiConnectionEventDao;
    private DbNetworkTrafficEventDao networkTrafficEventDao;
    private DbCallLogEventDao callLogEventDao;

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

        DbAssistanceOpenHelper helper = new DbAssistanceOpenHelper(context, Config.DATABASE_NAME, null);
        mDb = helper.getWritableDatabase();

        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);

        if (userDao == null) {
            userDao = getDaoSession().getDbUserDao();
        }

        if (deviceDao == null) {
            deviceDao = getDaoSession().getDbDeviceDao();
        }

        if (moduleDao == null) {
            moduleDao = getDaoSession().getDbModuleDao();
        }

        if (moduleCapabilityDao == null) {
            moduleCapabilityDao = getDaoSession().getDbModuleCapabilityDao();
        }

        if (moduleInstallationDao == null) {
            moduleInstallationDao = getDaoSession().getDbModuleInstallationDao();
        }

        if (newsDao == null) {
            newsDao = getDaoSession().getDbNewsDao();
        }

        if (accelerometerSensorDao == null) {
            accelerometerSensorDao = getDaoSession().getDbAccelerometerSensorDao();
        }

        if (gyroscopeSensorDao == null) {
            gyroscopeSensorDao = getDaoSession().getDbGyroscopeSensorDao();
        }

        if (magneticFieldSensorDao == null) {
            magneticFieldSensorDao = getDaoSession().getDbMagneticFieldSensorDao();
        }

        if (positionSensorDao == null) {
            positionSensorDao = getDaoSession().getDbPositionSensorDao();
        }

        if (motionActivityEventDao == null) {
            motionActivityEventDao = getDaoSession().getDbMotionActivityEventDao();
        }

        if (foregroundEventDao == null) {
            foregroundEventDao = getDaoSession().getDbForegroundEventDao();
        }

        if (lightSensorDao == null) {
            lightSensorDao = getDaoSession().getDbLightSensorDao();
        }

        if (connectionEventDao == null) {
            connectionEventDao = getDaoSession().getDbConnectionEventDao();
        }

        if (mobileConnectionEventDao == null) {
            mobileConnectionEventDao = getDaoSession().getDbMobileConnectionEventDao();
        }

        if (wifiConnectionEventDao == null) {
            wifiConnectionEventDao = getDaoSession().getDbWifiConnectionEventDao();
        }

        if (networkTrafficEventDao == null) {
            networkTrafficEventDao = getDaoSession().getDbNetworkTrafficEventDao();
        }

        if (callLogEventDao == null) {
            callLogEventDao = getDaoSession().getDbCallLogEventDao();
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
     * Returns list of DB cached user assistance entries
     *
     * @param userId
     * @param moduleId
     * @return
     */
    public List<DbNews> getNews(Long userId) {
        return newsDao
                .queryBuilder()
                .where(DbNewsDao.Properties.UserId.eq(userId))
                .build()
                .list();
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

        final String userToken = PreferenceProvider.getInstance(mContext).getUserToken();
        final long serverDeviceId = PreferenceProvider.getInstance(mContext).getServerDeviceId();

        DbUser user = userDao
                .queryBuilder()
                .where(DbUserDao.Properties.Token.eq(userToken))
                .build()
                .unique();

        if (user == null) {
            Log.d(TAG, "No such user found! Token: " + userToken);
            return false;
        }

        DbDevice device = deviceDao
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

            deviceDao.update(device);

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
                        accelerometerSensorDao.deleteInTx(dbAccelerometerSensors);
                    }
                    break;
                case SensorType.LOCATION:
                    if (dbPositionSensors != null) {
                        positionSensorDao.deleteInTx(dbPositionSensors);
                    }
                    break;
                case SensorType.MOTION_ACTIVITY:
                    if (dbMotionActivityEvents != null) {
                        motionActivityEventDao.deleteInTx(dbMotionActivityEvents);
                    }
                    break;
                case SensorType.FOREGROUND:
                    if (dbForegroundEvents != null) {
                        foregroundEventDao.deleteInTx(dbForegroundEvents);
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

        if (numberElements == 0) {
            dbAccelerometerSensors = accelerometerSensorDao
                    .queryBuilder()
                    .build()
                    .list();
        } else {
            dbAccelerometerSensors = accelerometerSensorDao
                    .queryBuilder()
                    .limit(numberElements)
                    .build()
                    .list();
        }

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

        if (numberElements == 0) {
            dbPositionSensors = positionSensorDao
                    .queryBuilder()
                    .build()
                    .list();
        } else {
            dbPositionSensors = positionSensorDao
                    .queryBuilder()
                    .limit(numberElements)
                    .build()
                    .list();
        }

        if (dbPositionSensors != null) {

            for (int i = 0; i < dbPositionSensors.size(); i++) {

                DbPositionSensor sensor = dbPositionSensors.get(i);

                LocationSensorRequest locationSensorRequest = new LocationSensorRequest();

                locationSensorRequest.setType(SensorType.LOCATION);
                locationSensorRequest.setTypeStr(SensorType.getApiName(SensorType.LOCATION));
                locationSensorRequest.setLatitude(sensor.getLatitude());
                locationSensorRequest.setLongitude(sensor.getLongitude());
                locationSensorRequest.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
                locationSensorRequest.setAccuracyVertical(sensor.getAccuracyVertical());
                locationSensorRequest.setAltitude(sensor.getAltitude());
                locationSensorRequest.setSpeed(sensor.getSpeed());
                locationSensorRequest.setCreated(sensor.getCreated());

                result.add(locationSensorRequest);
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

        if (numberElements == 0) {
            dbMotionActivityEvents = motionActivityEventDao
                    .queryBuilder()
                    .build()
                    .list();
        } else {
            dbMotionActivityEvents = motionActivityEventDao
                    .queryBuilder()
                    .limit(numberElements)
                    .build()
                    .list();
        }

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

        if (numberElements == 0) {
            dbForegroundEvents = foregroundEventDao
                    .queryBuilder()
                    .build()
                    .list();
        } else {
            dbForegroundEvents = foregroundEventDao
                    .queryBuilder()
                    .limit(numberElements)
                    .build()
                    .list();
        }

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
    public long insertEventEntry(IDbSensor sensor, int type) {

        if (sensor == null) {
            return -1l;
        }

        long result = -1l;

        switch (type) {
            case SensorType.ACCELEROMETER:

                Log.d(AccelerometerSensor.class.getSimpleName(), "Dumping data to db...");

                result = accelerometerSensorDao.insertOrReplace((DbAccelerometerSensor) sensor);

                Log.d(AccelerometerSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.GYROSCOPE:

                Log.d(GyroscopeSensor.class.getSimpleName(), "Dumping data to db...");

                result = gyroscopeSensorDao.insertOrReplace((DbGyroscopeSensor) sensor);

                Log.d(GyroscopeSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.MAGNETIC_FIELD:

                Log.d(MagneticFieldSensor.class.getSimpleName(), "Dumping data to db...");

                result = magneticFieldSensorDao.insertOrReplace((DbMagneticFieldSensor) sensor);

                Log.d(MagneticFieldSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.LOCATION:

                Log.d(LocationSensor.class.getSimpleName(), "Dumping data to db...");

                result = positionSensorDao.insertOrReplace((DbPositionSensor) sensor);

                Log.d(LocationSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.FOREGROUND:

                Log.d(ForegroundEvent.class.getSimpleName(), "Dumping data to db...");

                result = foregroundEventDao.insertOrReplace((DbForegroundEvent) sensor);

                Log.d(ForegroundEvent.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.MOTION_ACTIVITY:

                Log.d(MotionActivityEvent.class.getSimpleName(), "Dumping data to db...");

                result = motionActivityEventDao.insertOrReplace((DbMotionActivityEvent) sensor);

                Log.d(MotionActivityEvent.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.LIGHT:

                Log.d(LightSensor.class.getSimpleName(), "Dumping data to db...");

                result = lightSensorDao.insertOrReplace((DbLightSensor) sensor);

                Log.d(LightSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.CONNECTION:

                Log.d(ConnectionSensor.class.getSimpleName(), "Dumping CONNECTION data to db...");

                result = connectionEventDao.insertOrReplace((DbConnectionEvent) sensor);

                Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.MOBILE_DATA_CONNECTION:

                Log.d(ConnectionSensor.class.getSimpleName(), "Dumping MOBILE CONNECTION data to db...");

                result = mobileConnectionEventDao.insertOrReplace((DbMobileConnectionEvent) sensor);

                Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.WIFI_CONNECTION:

                Log.d(ConnectionSensor.class.getSimpleName(), "Dumping WIFI CONNECTION data to db...");

                result = wifiConnectionEventDao.insertOrReplace((DbWifiConnectionEvent) sensor);

                Log.d(ConnectionSensor.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.NETWORK_TRAFFIC:

                Log.d(ForegroundTrafficEvent.class.getSimpleName(),
                        "Dumping NETWORK TRAFFIC data to db...");

                result = networkTrafficEventDao.insertOrReplace((DbNetworkTrafficEvent) sensor);

                Log.d(ForegroundTrafficEvent.class.getSimpleName(), "Finished dumping data");
                break;

            case SensorType.CALL_LOG:

                Log.d(CallLogEvent.class.getSimpleName(),
                        "Dumping CALL LOG EVENT data to db...");

                result = callLogEventDao.insertOrReplace((DbCallLogEvent) sensor);

                Log.d(CallLogEvent.class.getSimpleName(), "Finished dumping data");
                break;
        }

        return result;
    }

    /**
     * Returns db user by email
     *
     * @param userEmail
     * @return
     */
    public DbUser getUserByEmail(String userEmail) {

        if (userEmail == null) {
            return null;
        }

        return userDao
                .queryBuilder()
                .where(DbUserDao.Properties.PrimaryEmail.eq(userEmail))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns db user by registered token
     *
     * @param userToken
     * @return
     */
    public DbUser getUserByToken(String userToken) {

        if (userToken == null) {
            return null;
        }

        return userDao
                .queryBuilder()
                .where(DbUserDao.Properties.Token.eq(userToken))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns db user device by id
     *
     * @param deviceId
     * @return
     */
    public DbDevice getDeviceById(long deviceId) {

        if (deviceId < 0) {
            return null;
        }

        return deviceDao
                .queryBuilder()
                .where(DbDeviceDao.Properties.Id.eq(deviceId))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns db module installation by user id
     *
     * @param userId
     * @return
     */
    public List<DbModuleInstallation> getModuleInstallationsByUserId(long userId) {

        if (userId < 0) {
            return Collections.emptyList();
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .build()
                .list();
    }

    /**
     * Returns db module installations by user id and module id
     *
     * @param userId
     * @param moduleId
     * @return
     */
    public List<DbModuleInstallation> getModuleInstallationsByUserId(long userId, long moduleId) {

        if (userId < 0) {
            return Collections.emptyList();
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .where(DbModuleInstallationDao.Properties.ModuleId.eq(moduleId))
                .build()
                .list();
    }

    /**
     * Returns db module installation by user id and module id
     *
     * @param userId
     * @return
     */
    public DbModuleInstallation getModuleInstallationForModuleByUserId(long userId, long moduleId) {

        if (userId < 0) {
            return null;
        }

        return moduleInstallationDao
                .queryBuilder()
                .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                .where(DbModuleInstallationDao.Properties.ModuleId.eq(moduleId))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns module by its package id and user id
     *
     * @param modulePackageName
     * @param userId
     * @return
     */
    public DbModule getModuleByPackageIdUserId(String modulePackageName, Long userId) {

        if (modulePackageName == null || userId < 0) {
            return null;
        }

        return moduleDao
                .queryBuilder()
                .where(DbModuleDao.Properties.PackageName.eq(modulePackageName))
                .where(DbModuleDao.Properties.UserId.eq(userId))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Inserts new user
     *
     * @return
     */
    public long insertUser(DbUser user) {

        if (user == null) {
            return -1L;
        }

        return userDao.insertOrReplace(user);
    }

    /**
     * Inserts new device
     *
     * @param device
     * @return
     */
    public long insertDevice(DbDevice device) {

        if (device == null) {
            return -1L;
        }

        return deviceDao.insertOrReplace(device);
    }

    /**
     * Inserts new module
     *
     * @param module
     * @return
     */
    public long insertModule(DbModule module) {

        if (module == null) {
            return -1L;
        }

        return moduleDao.insertOrReplace(module);
    }

    /**
     * Inserts new module capability
     *
     * @param moduleCapability
     * @return
     */
    public long insertModuleCapability(DbModuleCapability moduleCapability) {

        if (moduleCapability == null) {
            return -1L;
        }

        return moduleCapabilityDao.insertOrReplace(moduleCapability);
    }

    /**
     * Inserts new module capabilities
     *
     * @param moduleCapabilities
     * @return
     */
    public void insertModuleCapabilities(List<DbModuleCapability> moduleCapabilities) {

        if (moduleCapabilities == null) {
            return;
        }

        moduleCapabilityDao.insertInTx(moduleCapabilities);
    }

    /**
     * Inserts new module installation
     *
     * @param moduleInstallation
     * @return
     */
    public long insertModuleInstallation(DbModuleInstallation moduleInstallation) {

        if (moduleInstallation == null) {
            return -1L;
        }

        return moduleInstallationDao.insertOrReplace(moduleInstallation);
    }

    /**
     * Updates device
     *
     * @param device
     */
    public void updateDevice(DbDevice device) {

        if (device == null) {
            return;
        }

        deviceDao.update(device);
    }

    /**
     * Updates user
     *
     * @param user
     */
    public void updateUser(DbUser user) {

        if (user == null) {
            return;
        }

        userDao.update(user);
    }

    /**
     * Updates module installation
     *
     * @param moduleInstallation
     */
    public void updateModuleInstallation(DbModuleInstallation moduleInstallation) {

        if (moduleInstallation == null) {
            return;
        }

        moduleInstallationDao.update(moduleInstallation);
    }

    /**
     * Removes installed modules
     *
     * @param moduleInstallations
     */
    public void removeInstalledModules(List<DbModuleInstallation> moduleInstallations) {

        if (moduleInstallations == null) {
            return;
        }

        moduleInstallationDao.deleteInTx(moduleInstallations);
    }

    /**
     * Returns last element from table
     *
     * @return
     */
    public DbCallLogEvent getLastCallLogEvent() {
        return callLogEventDao
                .queryBuilder()
                .orderDesc(DbCallLogEventDao.Properties.Id)
                .limit(1)
                .build()
                .unique();
    }
}
