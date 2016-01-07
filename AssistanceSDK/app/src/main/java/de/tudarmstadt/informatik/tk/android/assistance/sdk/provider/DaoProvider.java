
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
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.AccountReaderSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.AccountReaderSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.BrowserHistorySensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.BrowserHistorySensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CallLogSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CallLogSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.ForegroundSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.ForegroundSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LoudnessSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LoudnessSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.MotionActivitySensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.MotionActivitySensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RingtoneSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RingtoneSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningProcessesSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningServicesSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningTasksSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.RunningTasksSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.ConnectionSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.ConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.MobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.MobileConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.WifiConnectionSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.connection.WifiConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.contact.ContactSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.power.PowerLevelSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.power.PowerLevelSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.power.PowerStateSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.power.PowerStateSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.AccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.AccelerometerSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.GyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.GyroscopeSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LightSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LightSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LocationSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.LocationSensorDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.MagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.MagneticFieldSensorDaoImpl;
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
     * ************************************************************
     * ******************* SENSORS / SOFT SENSORS *****************
     * ************************************************************
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
     * MotionActivitySensorDao
     *
     * @return
     */
    public MotionActivitySensorDao getMotionActivitySensorDao() {
        return MotionActivitySensorDaoImpl.getInstance(mDaoSession);
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
     * ForegroundSensorDao
     *
     * @return
     */
    public ForegroundSensorDao getForegroundSensorDao() {
        return ForegroundSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * MobileConnectionSensorDao
     *
     * @return
     */
    public MobileConnectionSensorDao getMobileConnectionSensorDao() {
        return MobileConnectionSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * NetworkTrafficSensorDao
     *
     * @return
     */
    public NetworkTrafficSensorDao getNetworkTrafficSensorDao() {
        return NetworkTrafficSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CallLogSensorDao
     *
     * @return
     */
    public CallLogSensorDao getCallLogSensorDao() {
        return CallLogSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CalendarSensorDao
     *
     * @return
     */
    public CalendarSensorDao getCalendarSensorDao() {
        return CalendarSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * CalendarReminderSensorDao
     *
     * @return
     */
    public CalendarReminderSensorDao getCalendarReminderSensorDao() {
        return CalendarReminderSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ConnectionSensorDao
     *
     * @return
     */
    public ConnectionSensorDao getConnectionSensorDao() {
        return ConnectionSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * WifiConnectionSensorDao
     *
     * @return
     */
    public WifiConnectionSensorDao getWifiConnectionSensorDao() {
        return WifiConnectionSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * PowerStateSensorDao
     *
     * @return
     */
    public PowerStateSensorDao getPowerStateSensorDao() {
        return PowerStateSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * PowerLevelSensorDao
     *
     * @return
     */
    public PowerLevelSensorDao getPowerLevelSensorDao() {
        return PowerLevelSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * LoudnessSensorDao
     *
     * @return
     */
    public LoudnessSensorDao getLoudnessSensorDao() {
        return LoudnessSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * RingtoneSensorDao
     *
     * @return
     */
    public RingtoneSensorDao getRingtoneSensorDao() {
        return RingtoneSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * AccountReaderSensorDao
     *
     * @return
     */
    public AccountReaderSensorDao getAccountReaderSensorDao() {
        return AccountReaderSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * BrowserHistorySensorDao
     *
     * @return
     */
    public BrowserHistorySensorDao getBrowserHistorySensorDao() {
        return BrowserHistorySensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ContactSensorDao
     *
     * @return
     */
    public ContactSensorDao getContactSensorDao() {
        return ContactSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ContactEmailSensorDao
     *
     * @return
     */
    public ContactEmailSensorDao getContactEmailSensorDao() {
        return ContactEmailSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * ContactNumberSensorDao
     *
     * @return
     */
    public ContactNumberSensorDao getContactNumberSensorDao() {
        return ContactNumberSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * RunningProcessesSensorDao
     *
     * @return
     */
    public RunningProcessesSensorDao getRunningProcessesSensorDao() {
        return RunningProcessesSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * RunningServicesSensorDao
     *
     * @return
     */
    public RunningServicesSensorDao getRunningServicesSensorDao() {
        return RunningServicesSensorDaoImpl.getInstance(mDaoSession);
    }

    /**
     * RunningTasksSensorDao
     *
     * @return
     */
    public RunningTasksSensorDao getRunningTasksSensorDao() {
        return RunningTasksSensorDaoImpl.getInstance(mDaoSession);
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
