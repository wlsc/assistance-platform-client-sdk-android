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
@Module(includes = ContextModule.class)
public class DbModule {

    @Singleton
    @Provides
    DbAssistanceOpenHelper provideHelper(Context context) {
        return new DbAssistanceOpenHelper(context, Config.DATABASE_NAME, null);
    }

    @Singleton
    @Provides
    Database provideDatabase(DbAssistanceOpenHelper helper) {
        return helper.getWritableDb();
    }

    @Singleton
    @Provides
    DaoMaster provideDaoMaster(Database db) {
        return new DaoMaster(db);
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession(IdentityScopeType.None);
    }

    @Singleton
    @Provides
    UserDao provideUserDao(DaoSession daoSession) {
        return new UserDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    DeviceDao provideDeviceDao(DaoSession daoSession) {
        return new DeviceDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ModuleDao provideModuleDao(DaoSession daoSession) {
        return new ModuleDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ModuleCapabilityDao provideModuleCapabilityDao(DaoSession daoSession) {
        return new ModuleCapabilityDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    NewsDao provideNewsDao(DaoSession daoSession) {
        return new NewsDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    SensorUploadLogsDao provideSensorUploadLogsDao(DaoSession daoSession) {
        return new SensorUploadLogsDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    AccelerometerSensorDao provideAccelerometerSensorDao(DaoSession daoSession) {
        return new AccelerometerSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    LocationSensorDao provideLocationSensorDao(DaoSession daoSession) {
        return new LocationSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    MotionActivitySensorDao provideMotionActivitySensorDao(DaoSession daoSession) {
        return new MotionActivitySensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    GyroscopeSensorDao provideGyroscopeSensorDao(DaoSession daoSession) {
        return new GyroscopeSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    LightSensorDao provideLightSensorDao(DaoSession daoSession) {
        return new LightSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    MagneticFieldSensorDao provideMagneticFieldSensorDao(DaoSession daoSession) {
        return new MagneticFieldSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ForegroundSensorDao provideForegroundSensorDao(DaoSession daoSession) {
        return new ForegroundSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    MobileConnectionSensorDao provideMobileConnectionSensorDao(DaoSession daoSession) {
        return new MobileConnectionSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    NetworkTrafficSensorDao provideNetworkTrafficSensorDao(DaoSession daoSession) {
        return new NetworkTrafficSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    CallLogSensorDao provideCallLogSensorDao(DaoSession daoSession) {
        return new CallLogSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    CalendarSensorDao provideCalendarSensorDao(DaoSession daoSession) {
        return new CalendarSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    CalendarReminderSensorDao provideCalendarReminderSensorDao(DaoSession daoSession) {
        return new CalendarReminderSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ConnectionSensorDao provideConnectionSensorDao(DaoSession daoSession) {
        return new ConnectionSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    WifiConnectionSensorDao provideWifiConnectionSensorDao(DaoSession daoSession) {
        return new WifiConnectionSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    PowerStateSensorDao providePowerStateSensorDao(DaoSession daoSession) {
        return new PowerStateSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    PowerLevelSensorDao providePowerLevelSensorDao(DaoSession daoSession) {
        return new PowerLevelSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    LoudnessSensorDao provideLoudnessSensorDao(DaoSession daoSession) {
        return new LoudnessSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    RingtoneSensorDao provideRingtoneSensorDao(DaoSession daoSession) {
        return new RingtoneSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    AccountReaderSensorDao provideAccountReaderSensorDao(DaoSession daoSession) {
        return new AccountReaderSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    BrowserHistorySensorDao provideBrowserHistorySensorDao(DaoSession daoSession) {
        return new BrowserHistorySensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ContactSensorDao provideContactSensorDao(DaoSession daoSession) {
        return new ContactSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ContactEmailSensorDao provideContactEmailSensorDao(DaoSession daoSession) {
        return new ContactEmailSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    ContactNumberSensorDao provideContactNumberSensorDao(DaoSession daoSession) {
        return new ContactNumberSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    RunningProcessesSensorDao provideRunningProcessesSensorDao(DaoSession daoSession) {
        return new RunningProcessesSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    RunningServicesSensorDao provideRunningServicesSensorDao(DaoSession daoSession) {
        return new RunningServicesSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    RunningTasksSensorDao provideRunningTasksSensorDao(DaoSession daoSession) {
        return new RunningTasksSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    TucanSensorDao provideTucanSensorDao(DaoSession daoSession) {
        return new TucanSensorDaoImpl(daoSession);
    }

    @Singleton
    @Provides
    FacebookSensorDao provideFacebookSensorDao(DaoSession daoSession) {
        return new FacebookSensorDaoImpl(daoSession);
    }
}
