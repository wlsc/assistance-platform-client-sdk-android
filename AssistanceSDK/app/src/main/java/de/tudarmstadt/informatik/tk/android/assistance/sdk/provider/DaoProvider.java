
package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoMaster;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.device.DeviceDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.device.DeviceDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module.ModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module.ModuleCapabilityDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module.ModuleDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.module.ModuleDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.news.NewsDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.news.NewsDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.AccountReaderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.AccountReaderEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.BrowserHistoryEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.BrowserHistoryEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CalendarEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CalendarEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CalendarReminderEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CalendarReminderEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CallLogEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.CallLogEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.ConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.ConnectionEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.ForegroundEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.ForegroundEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.LoudnessEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.LoudnessEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.MobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.MobileConnectionEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.MotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.MotionActivityEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.NetworkTrafficEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.NetworkTrafficEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.PowerStateEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.PowerStateEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.RingtoneEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.RingtoneEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.WifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.WifiConnectionEventDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.AccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.AccelerometerSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.GyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.GyroscopeSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.LightSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.LightSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.LocationSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.LocationSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.MagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor.MagneticFieldSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.user.UserDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.user.UserDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.db.DbAssistanceOpenHelper;

/**
 * Singleton database provider
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class DaoProvider {

    private static final String TAG = DaoProvider.class.getSimpleName();

    private static DaoProvider INSTANCE;

    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    /**
     * Constructor
     *
     * @param context
     */
    private DaoProvider(Context context) {

        DbAssistanceOpenHelper helper = new DbAssistanceOpenHelper(context, Config.DATABASE_NAME, null);
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
    public static DaoProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new DaoProvider(context);
        }

        return INSTANCE;
    }

    /**
     * UserDao
     *
     * @return
     */
    public UserDao getUserDao() {
        return UserDaoImpl.getInstance(mDaoSession);
    }

    /**
     * DeviceDao
     *
     * @return
     */
    public DeviceDao getDeviceDao() {
        return DeviceDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ModuleDao
     *
     * @return
     */
    public ModuleDao getModuleDao() {
        return ModuleDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ModuleCapabilityDao
     *
     * @return
     */
    public ModuleCapabilityDao getModuleCapabilityDao() {
        return ModuleCapabilityDaoImpl.getInstance(mDaoSession);
    }

    /**
     * NewsDao
     *
     * @return
     */
    public NewsDao getNewsDao() {
        return NewsDaoImpl.getInstance(mDaoSession);
    }

    /**
     * **************************************************************
     * ******************* SENSORS / EVENTS *************************
     * **************************************************************
     */

    /**
     * AccelerometerSensorDao
     *
     * @return
     */
    public AccelerometerSensorDao getAccelerometerSensorDao() {
        return AccelerometerSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * LocationSensorDao
     *
     * @return
     */
    public LocationSensorDao getLocationSensorDao() {
        return LocationSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * MotionActivityEventDao
     *
     * @return
     */
    public MotionActivityEventDao getMotionActivityEventDao() {
        return MotionActivityEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * GyroscopeSensorDao
     *
     * @return
     */
    public GyroscopeSensorDao getGyroscopeSensorDao() {
        return GyroscopeSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * LightSensorDao
     *
     * @return
     */
    public LightSensorDao getLightSensorDao() {
        return LightSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * MagneticFieldSensorDao
     *
     * @return
     */
    public MagneticFieldSensorDao getMagneticFieldSensorDao() {
        return MagneticFieldSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ForegroundEventDao
     *
     * @return
     */
    public ForegroundEventDao getForegroundEventDao() {
        return ForegroundEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * MobileConnectionEventDao
     *
     * @return
     */
    public MobileConnectionEventDao getMobileConnectionEventDao() {
        return MobileConnectionEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * NetworkTrafficEventDao
     *
     * @return
     */
    public NetworkTrafficEventDao getNetworkTrafficEventDao() {
        return NetworkTrafficEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CallLogEventDao
     *
     * @return
     */
    public CallLogEventDao getCallLogEventDao() {
        return CallLogEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CalendarEventDao
     *
     * @return
     */
    public CalendarEventDao getCalendarEventDao() {
        return CalendarEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CalendarReminderEventDao
     *
     * @return
     */
    public CalendarReminderEventDao getCalendarReminderEventDao() {
        return CalendarReminderEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ConnectionEventDao
     *
     * @return
     */
    public ConnectionEventDao getConnectionEventDao() {
        return ConnectionEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * WifiConnectionEventDao
     *
     * @return
     */
    public WifiConnectionEventDao getWifiConnectionEventDao() {
        return WifiConnectionEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * PowerStateEventDao
     *
     * @return
     */
    public PowerStateEventDao getPowerStateEventDao() {
        return PowerStateEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * LoudnessEventDao
     *
     * @return
     */
    public LoudnessEventDao getLoudnessEventDao() {
        return LoudnessEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * RingtoneEventDao
     *
     * @return
     */
    public RingtoneEventDao getRingtoneEventDao() {
        return RingtoneEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * AccountReaderEventDao
     *
     * @return
     */
    public AccountReaderEventDao getAccountReaderEventDao() {
        return AccountReaderEventDaoImpl.getInstance(mDaoSession);
    }

    /**
     * BrowserHistoryEventDao
     *
     * @return
     */
    public BrowserHistoryEventDao getBrowserHistoryEventDao() {
        return BrowserHistoryEventDaoImpl.getInstance(mDaoSession);
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
