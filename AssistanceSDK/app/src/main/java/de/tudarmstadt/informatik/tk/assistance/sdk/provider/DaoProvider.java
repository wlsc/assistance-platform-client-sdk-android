
package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;

import de.tudarmstadt.informatik.tk.assistance.sdk.dagger.component.SdkComponent;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoMaster;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.device.DeviceDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor.SensorUploadLogsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.news.NewsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccountReaderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.BrowserHistorySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CallLogSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.ForegroundSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.GyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LightSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LocationSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LoudnessSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MotionActivitySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RingtoneSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningTasksSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.ConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.MobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.WifiConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.FacebookSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.TucanSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerLevelSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerStateSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.user.UserDao;

/**
 * Singleton database provider
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public final class DaoProvider {

    private static DaoProvider INSTANCE;

    @Inject
    Database mDb;

    @Inject
    DaoSession mDaoSession;

    @Inject
    UserDao userDao;

    @Inject
    DeviceDao deviceDao;

    @Inject
    ModuleDao moduleDao;

    @Inject
    ModuleCapabilityDao moduleCapabilityDao;

    @Inject
    NewsDao newsDao;

    @Inject
    SensorUploadLogsDao sensorUploadLogsDao;

    @Inject
    AccelerometerSensorDao accelerometerSensorDao;

    @Inject
    LocationSensorDao locationSensorDao;

    @Inject
    MotionActivitySensorDao motionActivitySensorDao;

    @Inject
    GyroscopeSensorDao gyroscopeSensorDao;

    @Inject
    LightSensorDao lightSensorDao;

    @Inject
    MagneticFieldSensorDao magneticFieldSensorDao;

    @Inject
    ForegroundSensorDao foregroundSensorDao;

    @Inject
    MobileConnectionSensorDao mobileConnectionSensorDao;

    @Inject
    NetworkTrafficSensorDao networkTrafficSensorDao;

    @Inject
    CallLogSensorDao callLogSensorDao;

    @Inject
    CalendarSensorDao calendarSensorDao;

    @Inject
    CalendarReminderSensorDao calendarReminderSensorDao;

    @Inject
    ConnectionSensorDao connectionSensorDao;

    @Inject
    WifiConnectionSensorDao wifiConnectionSensorDao;

    @Inject
    PowerStateSensorDao powerStateSensorDao;

    @Inject
    PowerLevelSensorDao powerLevelSensorDao;

    @Inject
    LoudnessSensorDao loudnessSensorDao;

    @Inject
    RingtoneSensorDao ringtoneSensorDao;

    @Inject
    AccountReaderSensorDao accountReaderSensorDao;

    @Inject
    BrowserHistorySensorDao browserHistorySensorDao;

    @Inject
    ContactSensorDao contactSensorDao;

    @Inject
    ContactEmailSensorDao contactEmailSensorDao;

    @Inject
    ContactNumberSensorDao contactNumberSensorDao;

    @Inject
    RunningProcessesSensorDao runningProcessesSensorDao;

    @Inject
    RunningServicesSensorDao runningServicesSensorDao;

    @Inject
    RunningTasksSensorDao runningTasksSensorDao;

    @Inject
    TucanSensorDao tucanSensorDao;

    @Inject
    FacebookSensorDao facebookSensorDao;

    /**
     * Constructor
     *
     * @param context
     */
    private DaoProvider(Context context) {
        SdkComponent.Initializer.INSTANCE.init(context).inject(this);
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
        return userDao;
    }

    /**
     * DeviceDao
     *
     * @return
     */
    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    /**
     * ModuleDao
     *
     * @return
     */
    public ModuleDao getModuleDao() {
        return moduleDao;
    }

    /**
     * ModuleCapabilityDao
     *
     * @return
     */
    public ModuleCapabilityDao getModuleCapabilityDao() {
        return moduleCapabilityDao;
    }

    /**
     * NewsDao
     *
     * @return
     */
    public NewsDao getNewsDao() {
        return newsDao;
    }

    /**
     * ************************************************************
     * ******************** LOGS & MEASUREMENTS *******************
     * ************************************************************
     */

    /**
     * SensorUploadLogsDao
     *
     * @return
     */
    public SensorUploadLogsDao getSensorUploadLogsDao() {
        return sensorUploadLogsDao;
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
        return accelerometerSensorDao;
    }

    /**
     * LocationSensorDao
     *
     * @return
     */
    public LocationSensorDao getLocationSensorDao() {
        return locationSensorDao;
    }

    /**
     * MotionActivitySensorDao
     *
     * @return
     */
    public MotionActivitySensorDao getMotionActivitySensorDao() {
        return motionActivitySensorDao;
    }

    /**
     * GyroscopeSensorDao
     *
     * @return
     */
    public GyroscopeSensorDao getGyroscopeSensorDao() {
        return gyroscopeSensorDao;
    }

    /**
     * LightSensorDao
     *
     * @return
     */
    public LightSensorDao getLightSensorDao() {
        return lightSensorDao;
    }

    /**
     * MagneticFieldSensorDao
     *
     * @return
     */
    public MagneticFieldSensorDao getMagneticFieldSensorDao() {
        return magneticFieldSensorDao;
    }

    /**
     * ForegroundSensorDao
     *
     * @return
     */
    public ForegroundSensorDao getForegroundSensorDao() {
        return foregroundSensorDao;
    }

    /**
     * MobileConnectionSensorDao
     *
     * @return
     */
    public MobileConnectionSensorDao getMobileConnectionSensorDao() {
        return mobileConnectionSensorDao;
    }

    /**
     * NetworkTrafficSensorDao
     *
     * @return
     */
    public NetworkTrafficSensorDao getNetworkTrafficSensorDao() {
        return networkTrafficSensorDao;
    }

    /**
     * CallLogSensorDao
     *
     * @return
     */
    public CallLogSensorDao getCallLogSensorDao() {
        return callLogSensorDao;
    }

    /**
     * CalendarSensorDao
     *
     * @return
     */
    public CalendarSensorDao getCalendarSensorDao() {
        return calendarSensorDao;
    }

    /**
     * CalendarReminderSensorDao
     *
     * @return
     */
    public CalendarReminderSensorDao getCalendarReminderSensorDao() {
        return calendarReminderSensorDao;
    }

    /**
     * ConnectionSensorDao
     *
     * @return
     */
    public ConnectionSensorDao getConnectionSensorDao() {
        return connectionSensorDao;
    }

    /**
     * WifiConnectionSensorDao
     *
     * @return
     */
    public WifiConnectionSensorDao getWifiConnectionSensorDao() {
        return wifiConnectionSensorDao;
    }

    /**
     * PowerStateSensorDao
     *
     * @return
     */
    public PowerStateSensorDao getPowerStateSensorDao() {
        return powerStateSensorDao;
    }

    /**
     * PowerLevelSensorDao
     *
     * @return
     */
    public PowerLevelSensorDao getPowerLevelSensorDao() {
        return powerLevelSensorDao;
    }

    /**
     * LoudnessSensorDao
     *
     * @return
     */
    public LoudnessSensorDao getLoudnessSensorDao() {
        return loudnessSensorDao;
    }

    /**
     * RingtoneSensorDao
     *
     * @return
     */
    public RingtoneSensorDao getRingtoneSensorDao() {
        return ringtoneSensorDao;
    }

    /**
     * AccountReaderSensorDao
     *
     * @return
     */
    public AccountReaderSensorDao getAccountReaderSensorDao() {
        return accountReaderSensorDao;
    }

    /**
     * BrowserHistorySensorDao
     *
     * @return
     */
    public BrowserHistorySensorDao getBrowserHistorySensorDao() {
        return browserHistorySensorDao;
    }

    /**
     * ContactSensorDao
     *
     * @return
     */
    public ContactSensorDao getContactSensorDao() {
        return contactSensorDao;
    }

    /**
     * ContactEmailSensorDao
     *
     * @return
     */
    public ContactEmailSensorDao getContactEmailSensorDao() {
        return contactEmailSensorDao;
    }

    /**
     * ContactNumberSensorDao
     *
     * @return
     */
    public ContactNumberSensorDao getContactNumberSensorDao() {
        return contactNumberSensorDao;
    }

    /**
     * RunningProcessesSensorDao
     *
     * @return
     */
    public RunningProcessesSensorDao getRunningProcessesSensorDao() {
        return runningProcessesSensorDao;
    }

    /**
     * RunningServicesSensorDao
     *
     * @return
     */
    public RunningServicesSensorDao getRunningServicesSensorDao() {
        return runningServicesSensorDao;
    }

    /**
     * RunningTasksSensorDao
     *
     * @return
     */
    public RunningTasksSensorDao getRunningTasksSensorDao() {
        return runningTasksSensorDao;
    }

    /**
     * TucanSensorDao
     *
     * @return
     */
    public TucanSensorDao getTucanSensorDao() {
        return tucanSensorDao;
    }

    /**
     * FacebookSensorDao
     *
     * @return
     */
    public FacebookSensorDao getFacebookSensorDao() {
        return facebookSensorDao;
    }

    /**
     * Drop it like its hard!
     */
    public void hardReset() {
        DaoMaster.dropAllTables(mDb, true);
        INSTANCE = null;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public Database getDatabase() {
        return mDb;
    }
}
