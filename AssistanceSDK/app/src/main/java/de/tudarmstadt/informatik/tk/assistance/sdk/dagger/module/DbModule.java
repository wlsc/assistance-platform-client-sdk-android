package de.tudarmstadt.informatik.tk.assistance.sdk.dagger.module;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoMaster;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.device.DeviceDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.device.DeviceDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor.SensorUploadLogsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.logs.upload.sensor.SensorUploadLogsDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleCapabilityDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.module.ModuleDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.news.NewsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.news.NewsDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccelerometerSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccountReaderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.AccountReaderSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.BrowserHistorySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.BrowserHistorySensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CallLogSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CallLogSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.ForegroundSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.ForegroundSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.GyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.GyroscopeSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LightSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LightSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LocationSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LocationSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LoudnessSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.LoudnessSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MagneticFieldSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MotionActivitySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.MotionActivitySensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RingtoneSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RingtoneSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningProcessesSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningServicesSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningTasksSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningTasksSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarReminderSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.calendar.CalendarSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.ConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.ConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.MobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.MobileConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.WifiConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.connection.WifiConnectionSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactEmailSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactNumberSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.contact.ContactSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.FacebookSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.FacebookSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.TucanSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.external.TucanSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerLevelSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerLevelSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerStateSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.power.PowerStateSensorDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.user.UserDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.user.UserDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.db.DbAssistanceOpenHelper;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17/10/2016
 */
@Module
public class DbModule {

    private Context context;

    private DbAssistanceOpenHelper helper;

    private Database mDb;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    public DbModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    DbAssistanceOpenHelper provideHelper() {
        helper = new DbAssistanceOpenHelper(context, Config.DATABASE_NAME, null);
        return helper;
    }

    @Singleton
    @Provides
    Database provideDatabase() {
        if (helper == null) {
            helper = provideHelper();
        }
        mDb = helper.getWritableDb();
        return mDb;
    }

    @Singleton
    @Provides
    DaoMaster provideDaoMaster() {
        if (mDb == null) {
            mDb = provideDatabase();
        }
        mDaoMaster = new DaoMaster(mDb);
        return mDaoMaster;
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession() {
        if (mDaoMaster == null) {
            mDaoMaster = provideDaoMaster();
        }
        mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);
        return mDaoSession;
    }

    @Singleton
    @Provides
    UserDao provideUserDao() {
        return new UserDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    DeviceDao provideDeviceDao() {
        return new DeviceDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ModuleDao provideModuleDao() {
        return new ModuleDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ModuleCapabilityDao provideModuleCapabilityDao() {
        return new ModuleCapabilityDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    NewsDao provideNewsDao() {
        return new NewsDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    SensorUploadLogsDao provideSensorUploadLogsDao() {
        return new SensorUploadLogsDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    AccelerometerSensorDao provideAccelerometerSensorDao() {
        return new AccelerometerSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    LocationSensorDao provideLocationSensorDao() {
        return new LocationSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    MotionActivitySensorDao provideMotionActivitySensorDao() {
        return new MotionActivitySensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    GyroscopeSensorDao provideGyroscopeSensorDao() {
        return new GyroscopeSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    LightSensorDao provideLightSensorDao() {
        return new LightSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    MagneticFieldSensorDao provideMagneticFieldSensorDao() {
        return new MagneticFieldSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ForegroundSensorDao provideForegroundSensorDao() {
        return new ForegroundSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    MobileConnectionSensorDao provideMobileConnectionSensorDao() {
        return new MobileConnectionSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    NetworkTrafficSensorDao provideNetworkTrafficSensorDao() {
        return new NetworkTrafficSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    CallLogSensorDao provideCallLogSensorDao() {
        return new CallLogSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    CalendarSensorDao provideCalendarSensorDao() {
        return new CalendarSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    CalendarReminderSensorDao provideCalendarReminderSensorDao() {
        return new CalendarReminderSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ConnectionSensorDao provideConnectionSensorDao() {
        return new ConnectionSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    WifiConnectionSensorDao provideWifiConnectionSensorDao() {
        return new WifiConnectionSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    PowerStateSensorDao providePowerStateSensorDao() {
        return new PowerStateSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    PowerLevelSensorDao providePowerLevelSensorDao() {
        return new PowerLevelSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    LoudnessSensorDao provideLoudnessSensorDao() {
        return new LoudnessSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    RingtoneSensorDao provideRingtoneSensorDao() {
        return new RingtoneSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    AccountReaderSensorDao provideAccountReaderSensorDao() {
        return new AccountReaderSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    BrowserHistorySensorDao provideBrowserHistorySensorDao() {
        return new BrowserHistorySensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ContactSensorDao provideContactSensorDao() {
        return new ContactSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ContactEmailSensorDao provideContactEmailSensorDao() {
        return new ContactEmailSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    ContactNumberSensorDao provideContactNumberSensorDao() {
        return new ContactNumberSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    RunningProcessesSensorDao provideRunningProcessesSensorDao() {
        return new RunningProcessesSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    RunningServicesSensorDao provideRunningServicesSensorDao() {
        return new RunningServicesSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    RunningTasksSensorDao provideRunningTasksSensorDao() {
        return new RunningTasksSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    TucanSensorDao provideTucanSensorDao() {
        return new TucanSensorDaoImpl(mDaoSession);
    }

    @Singleton
    @Provides
    FacebookSensorDao provideFacebookSensorDao() {
        return new FacebookSensorDaoImpl(mDaoSession);
    }
}
