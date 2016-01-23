package de.tudarmstadt.informatik.tk.assistance.sdk.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUserSocialProfile;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbDevice;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapability;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleAllowedCapabilities;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNews;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUpload;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMagneticFieldSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbWifiConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLoudnessSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningTasksSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbBrowserHistorySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactEmailSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerLevelSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbFacebookSensor;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUserDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUserSocialProfileDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbDeviceDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbModuleAllowedCapabilitiesDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNewsDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUploadDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbGyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMotionActivitySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbWifiConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLoudnessSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbLightSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRingtoneSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningProcessesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningTasksSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbBrowserHistorySensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCallLogSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbCalendarReminderSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactNumberSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbContactEmailSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerStateSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbPowerLevelSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbTucanSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbFacebookSensorDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dbUserDaoConfig;
    private final DaoConfig dbUserSocialProfileDaoConfig;
    private final DaoConfig dbDeviceDaoConfig;
    private final DaoConfig dbModuleDaoConfig;
    private final DaoConfig dbModuleCapabilityDaoConfig;
    private final DaoConfig dbModuleAllowedCapabilitiesDaoConfig;
    private final DaoConfig dbNewsDaoConfig;
    private final DaoConfig logsSensorUploadDaoConfig;
    private final DaoConfig dbPositionSensorDaoConfig;
    private final DaoConfig dbGyroscopeSensorDaoConfig;
    private final DaoConfig dbAccelerometerSensorDaoConfig;
    private final DaoConfig dbMagneticFieldSensorDaoConfig;
    private final DaoConfig dbMotionActivitySensorDaoConfig;
    private final DaoConfig dbConnectionSensorDaoConfig;
    private final DaoConfig dbWifiConnectionSensorDaoConfig;
    private final DaoConfig dbMobileConnectionSensorDaoConfig;
    private final DaoConfig dbLoudnessSensorDaoConfig;
    private final DaoConfig dbForegroundSensorDaoConfig;
    private final DaoConfig dbLightSensorDaoConfig;
    private final DaoConfig dbRingtoneSensorDaoConfig;
    private final DaoConfig dbRunningProcessesSensorDaoConfig;
    private final DaoConfig dbRunningServicesSensorDaoConfig;
    private final DaoConfig dbRunningTasksSensorDaoConfig;
    private final DaoConfig dbAccountReaderSensorDaoConfig;
    private final DaoConfig dbNetworkTrafficSensorDaoConfig;
    private final DaoConfig dbBrowserHistorySensorDaoConfig;
    private final DaoConfig dbCallLogSensorDaoConfig;
    private final DaoConfig dbCalendarSensorDaoConfig;
    private final DaoConfig dbCalendarReminderSensorDaoConfig;
    private final DaoConfig dbContactSensorDaoConfig;
    private final DaoConfig dbContactNumberSensorDaoConfig;
    private final DaoConfig dbContactEmailSensorDaoConfig;
    private final DaoConfig dbPowerStateSensorDaoConfig;
    private final DaoConfig dbPowerLevelSensorDaoConfig;
    private final DaoConfig dbTucanSensorDaoConfig;
    private final DaoConfig dbFacebookSensorDaoConfig;

    private final DbUserDao dbUserDao;
    private final DbUserSocialProfileDao dbUserSocialProfileDao;
    private final DbDeviceDao dbDeviceDao;
    private final DbModuleDao dbModuleDao;
    private final DbModuleCapabilityDao dbModuleCapabilityDao;
    private final DbModuleAllowedCapabilitiesDao dbModuleAllowedCapabilitiesDao;
    private final DbNewsDao dbNewsDao;
    private final LogsSensorUploadDao logsSensorUploadDao;
    private final DbPositionSensorDao dbPositionSensorDao;
    private final DbGyroscopeSensorDao dbGyroscopeSensorDao;
    private final DbAccelerometerSensorDao dbAccelerometerSensorDao;
    private final DbMagneticFieldSensorDao dbMagneticFieldSensorDao;
    private final DbMotionActivitySensorDao dbMotionActivitySensorDao;
    private final DbConnectionSensorDao dbConnectionSensorDao;
    private final DbWifiConnectionSensorDao dbWifiConnectionSensorDao;
    private final DbMobileConnectionSensorDao dbMobileConnectionSensorDao;
    private final DbLoudnessSensorDao dbLoudnessSensorDao;
    private final DbForegroundSensorDao dbForegroundSensorDao;
    private final DbLightSensorDao dbLightSensorDao;
    private final DbRingtoneSensorDao dbRingtoneSensorDao;
    private final DbRunningProcessesSensorDao dbRunningProcessesSensorDao;
    private final DbRunningServicesSensorDao dbRunningServicesSensorDao;
    private final DbRunningTasksSensorDao dbRunningTasksSensorDao;
    private final DbAccountReaderSensorDao dbAccountReaderSensorDao;
    private final DbNetworkTrafficSensorDao dbNetworkTrafficSensorDao;
    private final DbBrowserHistorySensorDao dbBrowserHistorySensorDao;
    private final DbCallLogSensorDao dbCallLogSensorDao;
    private final DbCalendarSensorDao dbCalendarSensorDao;
    private final DbCalendarReminderSensorDao dbCalendarReminderSensorDao;
    private final DbContactSensorDao dbContactSensorDao;
    private final DbContactNumberSensorDao dbContactNumberSensorDao;
    private final DbContactEmailSensorDao dbContactEmailSensorDao;
    private final DbPowerStateSensorDao dbPowerStateSensorDao;
    private final DbPowerLevelSensorDao dbPowerLevelSensorDao;
    private final DbTucanSensorDao dbTucanSensorDao;
    private final DbFacebookSensorDao dbFacebookSensorDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dbUserDaoConfig = daoConfigMap.get(DbUserDao.class).clone();
        dbUserDaoConfig.initIdentityScope(type);

        dbUserSocialProfileDaoConfig = daoConfigMap.get(DbUserSocialProfileDao.class).clone();
        dbUserSocialProfileDaoConfig.initIdentityScope(type);

        dbDeviceDaoConfig = daoConfigMap.get(DbDeviceDao.class).clone();
        dbDeviceDaoConfig.initIdentityScope(type);

        dbModuleDaoConfig = daoConfigMap.get(DbModuleDao.class).clone();
        dbModuleDaoConfig.initIdentityScope(type);

        dbModuleCapabilityDaoConfig = daoConfigMap.get(DbModuleCapabilityDao.class).clone();
        dbModuleCapabilityDaoConfig.initIdentityScope(type);

        dbModuleAllowedCapabilitiesDaoConfig = daoConfigMap.get(DbModuleAllowedCapabilitiesDao.class).clone();
        dbModuleAllowedCapabilitiesDaoConfig.initIdentityScope(type);

        dbNewsDaoConfig = daoConfigMap.get(DbNewsDao.class).clone();
        dbNewsDaoConfig.initIdentityScope(type);

        logsSensorUploadDaoConfig = daoConfigMap.get(LogsSensorUploadDao.class).clone();
        logsSensorUploadDaoConfig.initIdentityScope(type);

        dbPositionSensorDaoConfig = daoConfigMap.get(DbPositionSensorDao.class).clone();
        dbPositionSensorDaoConfig.initIdentityScope(type);

        dbGyroscopeSensorDaoConfig = daoConfigMap.get(DbGyroscopeSensorDao.class).clone();
        dbGyroscopeSensorDaoConfig.initIdentityScope(type);

        dbAccelerometerSensorDaoConfig = daoConfigMap.get(DbAccelerometerSensorDao.class).clone();
        dbAccelerometerSensorDaoConfig.initIdentityScope(type);

        dbMagneticFieldSensorDaoConfig = daoConfigMap.get(DbMagneticFieldSensorDao.class).clone();
        dbMagneticFieldSensorDaoConfig.initIdentityScope(type);

        dbMotionActivitySensorDaoConfig = daoConfigMap.get(DbMotionActivitySensorDao.class).clone();
        dbMotionActivitySensorDaoConfig.initIdentityScope(type);

        dbConnectionSensorDaoConfig = daoConfigMap.get(DbConnectionSensorDao.class).clone();
        dbConnectionSensorDaoConfig.initIdentityScope(type);

        dbWifiConnectionSensorDaoConfig = daoConfigMap.get(DbWifiConnectionSensorDao.class).clone();
        dbWifiConnectionSensorDaoConfig.initIdentityScope(type);

        dbMobileConnectionSensorDaoConfig = daoConfigMap.get(DbMobileConnectionSensorDao.class).clone();
        dbMobileConnectionSensorDaoConfig.initIdentityScope(type);

        dbLoudnessSensorDaoConfig = daoConfigMap.get(DbLoudnessSensorDao.class).clone();
        dbLoudnessSensorDaoConfig.initIdentityScope(type);

        dbForegroundSensorDaoConfig = daoConfigMap.get(DbForegroundSensorDao.class).clone();
        dbForegroundSensorDaoConfig.initIdentityScope(type);

        dbLightSensorDaoConfig = daoConfigMap.get(DbLightSensorDao.class).clone();
        dbLightSensorDaoConfig.initIdentityScope(type);

        dbRingtoneSensorDaoConfig = daoConfigMap.get(DbRingtoneSensorDao.class).clone();
        dbRingtoneSensorDaoConfig.initIdentityScope(type);

        dbRunningProcessesSensorDaoConfig = daoConfigMap.get(DbRunningProcessesSensorDao.class).clone();
        dbRunningProcessesSensorDaoConfig.initIdentityScope(type);

        dbRunningServicesSensorDaoConfig = daoConfigMap.get(DbRunningServicesSensorDao.class).clone();
        dbRunningServicesSensorDaoConfig.initIdentityScope(type);

        dbRunningTasksSensorDaoConfig = daoConfigMap.get(DbRunningTasksSensorDao.class).clone();
        dbRunningTasksSensorDaoConfig.initIdentityScope(type);

        dbAccountReaderSensorDaoConfig = daoConfigMap.get(DbAccountReaderSensorDao.class).clone();
        dbAccountReaderSensorDaoConfig.initIdentityScope(type);

        dbNetworkTrafficSensorDaoConfig = daoConfigMap.get(DbNetworkTrafficSensorDao.class).clone();
        dbNetworkTrafficSensorDaoConfig.initIdentityScope(type);

        dbBrowserHistorySensorDaoConfig = daoConfigMap.get(DbBrowserHistorySensorDao.class).clone();
        dbBrowserHistorySensorDaoConfig.initIdentityScope(type);

        dbCallLogSensorDaoConfig = daoConfigMap.get(DbCallLogSensorDao.class).clone();
        dbCallLogSensorDaoConfig.initIdentityScope(type);

        dbCalendarSensorDaoConfig = daoConfigMap.get(DbCalendarSensorDao.class).clone();
        dbCalendarSensorDaoConfig.initIdentityScope(type);

        dbCalendarReminderSensorDaoConfig = daoConfigMap.get(DbCalendarReminderSensorDao.class).clone();
        dbCalendarReminderSensorDaoConfig.initIdentityScope(type);

        dbContactSensorDaoConfig = daoConfigMap.get(DbContactSensorDao.class).clone();
        dbContactSensorDaoConfig.initIdentityScope(type);

        dbContactNumberSensorDaoConfig = daoConfigMap.get(DbContactNumberSensorDao.class).clone();
        dbContactNumberSensorDaoConfig.initIdentityScope(type);

        dbContactEmailSensorDaoConfig = daoConfigMap.get(DbContactEmailSensorDao.class).clone();
        dbContactEmailSensorDaoConfig.initIdentityScope(type);

        dbPowerStateSensorDaoConfig = daoConfigMap.get(DbPowerStateSensorDao.class).clone();
        dbPowerStateSensorDaoConfig.initIdentityScope(type);

        dbPowerLevelSensorDaoConfig = daoConfigMap.get(DbPowerLevelSensorDao.class).clone();
        dbPowerLevelSensorDaoConfig.initIdentityScope(type);

        dbTucanSensorDaoConfig = daoConfigMap.get(DbTucanSensorDao.class).clone();
        dbTucanSensorDaoConfig.initIdentityScope(type);

        dbFacebookSensorDaoConfig = daoConfigMap.get(DbFacebookSensorDao.class).clone();
        dbFacebookSensorDaoConfig.initIdentityScope(type);

        dbUserDao = new DbUserDao(dbUserDaoConfig, this);
        dbUserSocialProfileDao = new DbUserSocialProfileDao(dbUserSocialProfileDaoConfig, this);
        dbDeviceDao = new DbDeviceDao(dbDeviceDaoConfig, this);
        dbModuleDao = new DbModuleDao(dbModuleDaoConfig, this);
        dbModuleCapabilityDao = new DbModuleCapabilityDao(dbModuleCapabilityDaoConfig, this);
        dbModuleAllowedCapabilitiesDao = new DbModuleAllowedCapabilitiesDao(dbModuleAllowedCapabilitiesDaoConfig, this);
        dbNewsDao = new DbNewsDao(dbNewsDaoConfig, this);
        logsSensorUploadDao = new LogsSensorUploadDao(logsSensorUploadDaoConfig, this);
        dbPositionSensorDao = new DbPositionSensorDao(dbPositionSensorDaoConfig, this);
        dbGyroscopeSensorDao = new DbGyroscopeSensorDao(dbGyroscopeSensorDaoConfig, this);
        dbAccelerometerSensorDao = new DbAccelerometerSensorDao(dbAccelerometerSensorDaoConfig, this);
        dbMagneticFieldSensorDao = new DbMagneticFieldSensorDao(dbMagneticFieldSensorDaoConfig, this);
        dbMotionActivitySensorDao = new DbMotionActivitySensorDao(dbMotionActivitySensorDaoConfig, this);
        dbConnectionSensorDao = new DbConnectionSensorDao(dbConnectionSensorDaoConfig, this);
        dbWifiConnectionSensorDao = new DbWifiConnectionSensorDao(dbWifiConnectionSensorDaoConfig, this);
        dbMobileConnectionSensorDao = new DbMobileConnectionSensorDao(dbMobileConnectionSensorDaoConfig, this);
        dbLoudnessSensorDao = new DbLoudnessSensorDao(dbLoudnessSensorDaoConfig, this);
        dbForegroundSensorDao = new DbForegroundSensorDao(dbForegroundSensorDaoConfig, this);
        dbLightSensorDao = new DbLightSensorDao(dbLightSensorDaoConfig, this);
        dbRingtoneSensorDao = new DbRingtoneSensorDao(dbRingtoneSensorDaoConfig, this);
        dbRunningProcessesSensorDao = new DbRunningProcessesSensorDao(dbRunningProcessesSensorDaoConfig, this);
        dbRunningServicesSensorDao = new DbRunningServicesSensorDao(dbRunningServicesSensorDaoConfig, this);
        dbRunningTasksSensorDao = new DbRunningTasksSensorDao(dbRunningTasksSensorDaoConfig, this);
        dbAccountReaderSensorDao = new DbAccountReaderSensorDao(dbAccountReaderSensorDaoConfig, this);
        dbNetworkTrafficSensorDao = new DbNetworkTrafficSensorDao(dbNetworkTrafficSensorDaoConfig, this);
        dbBrowserHistorySensorDao = new DbBrowserHistorySensorDao(dbBrowserHistorySensorDaoConfig, this);
        dbCallLogSensorDao = new DbCallLogSensorDao(dbCallLogSensorDaoConfig, this);
        dbCalendarSensorDao = new DbCalendarSensorDao(dbCalendarSensorDaoConfig, this);
        dbCalendarReminderSensorDao = new DbCalendarReminderSensorDao(dbCalendarReminderSensorDaoConfig, this);
        dbContactSensorDao = new DbContactSensorDao(dbContactSensorDaoConfig, this);
        dbContactNumberSensorDao = new DbContactNumberSensorDao(dbContactNumberSensorDaoConfig, this);
        dbContactEmailSensorDao = new DbContactEmailSensorDao(dbContactEmailSensorDaoConfig, this);
        dbPowerStateSensorDao = new DbPowerStateSensorDao(dbPowerStateSensorDaoConfig, this);
        dbPowerLevelSensorDao = new DbPowerLevelSensorDao(dbPowerLevelSensorDaoConfig, this);
        dbTucanSensorDao = new DbTucanSensorDao(dbTucanSensorDaoConfig, this);
        dbFacebookSensorDao = new DbFacebookSensorDao(dbFacebookSensorDaoConfig, this);

        registerDao(DbUser.class, dbUserDao);
        registerDao(DbUserSocialProfile.class, dbUserSocialProfileDao);
        registerDao(DbDevice.class, dbDeviceDao);
        registerDao(DbModule.class, dbModuleDao);
        registerDao(DbModuleCapability.class, dbModuleCapabilityDao);
        registerDao(DbModuleAllowedCapabilities.class, dbModuleAllowedCapabilitiesDao);
        registerDao(DbNews.class, dbNewsDao);
        registerDao(LogsSensorUpload.class, logsSensorUploadDao);
        registerDao(DbPositionSensor.class, dbPositionSensorDao);
        registerDao(DbGyroscopeSensor.class, dbGyroscopeSensorDao);
        registerDao(DbAccelerometerSensor.class, dbAccelerometerSensorDao);
        registerDao(DbMagneticFieldSensor.class, dbMagneticFieldSensorDao);
        registerDao(DbMotionActivitySensor.class, dbMotionActivitySensorDao);
        registerDao(DbConnectionSensor.class, dbConnectionSensorDao);
        registerDao(DbWifiConnectionSensor.class, dbWifiConnectionSensorDao);
        registerDao(DbMobileConnectionSensor.class, dbMobileConnectionSensorDao);
        registerDao(DbLoudnessSensor.class, dbLoudnessSensorDao);
        registerDao(DbForegroundSensor.class, dbForegroundSensorDao);
        registerDao(DbLightSensor.class, dbLightSensorDao);
        registerDao(DbRingtoneSensor.class, dbRingtoneSensorDao);
        registerDao(DbRunningProcessesSensor.class, dbRunningProcessesSensorDao);
        registerDao(DbRunningServicesSensor.class, dbRunningServicesSensorDao);
        registerDao(DbRunningTasksSensor.class, dbRunningTasksSensorDao);
        registerDao(DbAccountReaderSensor.class, dbAccountReaderSensorDao);
        registerDao(DbNetworkTrafficSensor.class, dbNetworkTrafficSensorDao);
        registerDao(DbBrowserHistorySensor.class, dbBrowserHistorySensorDao);
        registerDao(DbCallLogSensor.class, dbCallLogSensorDao);
        registerDao(DbCalendarSensor.class, dbCalendarSensorDao);
        registerDao(DbCalendarReminderSensor.class, dbCalendarReminderSensorDao);
        registerDao(DbContactSensor.class, dbContactSensorDao);
        registerDao(DbContactNumberSensor.class, dbContactNumberSensorDao);
        registerDao(DbContactEmailSensor.class, dbContactEmailSensorDao);
        registerDao(DbPowerStateSensor.class, dbPowerStateSensorDao);
        registerDao(DbPowerLevelSensor.class, dbPowerLevelSensorDao);
        registerDao(DbTucanSensor.class, dbTucanSensorDao);
        registerDao(DbFacebookSensor.class, dbFacebookSensorDao);
    }
    
    public void clear() {
        dbUserDaoConfig.getIdentityScope().clear();
        dbUserSocialProfileDaoConfig.getIdentityScope().clear();
        dbDeviceDaoConfig.getIdentityScope().clear();
        dbModuleDaoConfig.getIdentityScope().clear();
        dbModuleCapabilityDaoConfig.getIdentityScope().clear();
        dbModuleAllowedCapabilitiesDaoConfig.getIdentityScope().clear();
        dbNewsDaoConfig.getIdentityScope().clear();
        logsSensorUploadDaoConfig.getIdentityScope().clear();
        dbPositionSensorDaoConfig.getIdentityScope().clear();
        dbGyroscopeSensorDaoConfig.getIdentityScope().clear();
        dbAccelerometerSensorDaoConfig.getIdentityScope().clear();
        dbMagneticFieldSensorDaoConfig.getIdentityScope().clear();
        dbMotionActivitySensorDaoConfig.getIdentityScope().clear();
        dbConnectionSensorDaoConfig.getIdentityScope().clear();
        dbWifiConnectionSensorDaoConfig.getIdentityScope().clear();
        dbMobileConnectionSensorDaoConfig.getIdentityScope().clear();
        dbLoudnessSensorDaoConfig.getIdentityScope().clear();
        dbForegroundSensorDaoConfig.getIdentityScope().clear();
        dbLightSensorDaoConfig.getIdentityScope().clear();
        dbRingtoneSensorDaoConfig.getIdentityScope().clear();
        dbRunningProcessesSensorDaoConfig.getIdentityScope().clear();
        dbRunningServicesSensorDaoConfig.getIdentityScope().clear();
        dbRunningTasksSensorDaoConfig.getIdentityScope().clear();
        dbAccountReaderSensorDaoConfig.getIdentityScope().clear();
        dbNetworkTrafficSensorDaoConfig.getIdentityScope().clear();
        dbBrowserHistorySensorDaoConfig.getIdentityScope().clear();
        dbCallLogSensorDaoConfig.getIdentityScope().clear();
        dbCalendarSensorDaoConfig.getIdentityScope().clear();
        dbCalendarReminderSensorDaoConfig.getIdentityScope().clear();
        dbContactSensorDaoConfig.getIdentityScope().clear();
        dbContactNumberSensorDaoConfig.getIdentityScope().clear();
        dbContactEmailSensorDaoConfig.getIdentityScope().clear();
        dbPowerStateSensorDaoConfig.getIdentityScope().clear();
        dbPowerLevelSensorDaoConfig.getIdentityScope().clear();
        dbTucanSensorDaoConfig.getIdentityScope().clear();
        dbFacebookSensorDaoConfig.getIdentityScope().clear();
    }

    public DbUserDao getDbUserDao() {
        return dbUserDao;
    }

    public DbUserSocialProfileDao getDbUserSocialProfileDao() {
        return dbUserSocialProfileDao;
    }

    public DbDeviceDao getDbDeviceDao() {
        return dbDeviceDao;
    }

    public DbModuleDao getDbModuleDao() {
        return dbModuleDao;
    }

    public DbModuleCapabilityDao getDbModuleCapabilityDao() {
        return dbModuleCapabilityDao;
    }

    public DbModuleAllowedCapabilitiesDao getDbModuleAllowedCapabilitiesDao() {
        return dbModuleAllowedCapabilitiesDao;
    }

    public DbNewsDao getDbNewsDao() {
        return dbNewsDao;
    }

    public LogsSensorUploadDao getLogsSensorUploadDao() {
        return logsSensorUploadDao;
    }

    public DbPositionSensorDao getDbPositionSensorDao() {
        return dbPositionSensorDao;
    }

    public DbGyroscopeSensorDao getDbGyroscopeSensorDao() {
        return dbGyroscopeSensorDao;
    }

    public DbAccelerometerSensorDao getDbAccelerometerSensorDao() {
        return dbAccelerometerSensorDao;
    }

    public DbMagneticFieldSensorDao getDbMagneticFieldSensorDao() {
        return dbMagneticFieldSensorDao;
    }

    public DbMotionActivitySensorDao getDbMotionActivitySensorDao() {
        return dbMotionActivitySensorDao;
    }

    public DbConnectionSensorDao getDbConnectionSensorDao() {
        return dbConnectionSensorDao;
    }

    public DbWifiConnectionSensorDao getDbWifiConnectionSensorDao() {
        return dbWifiConnectionSensorDao;
    }

    public DbMobileConnectionSensorDao getDbMobileConnectionSensorDao() {
        return dbMobileConnectionSensorDao;
    }

    public DbLoudnessSensorDao getDbLoudnessSensorDao() {
        return dbLoudnessSensorDao;
    }

    public DbForegroundSensorDao getDbForegroundSensorDao() {
        return dbForegroundSensorDao;
    }

    public DbLightSensorDao getDbLightSensorDao() {
        return dbLightSensorDao;
    }

    public DbRingtoneSensorDao getDbRingtoneSensorDao() {
        return dbRingtoneSensorDao;
    }

    public DbRunningProcessesSensorDao getDbRunningProcessesSensorDao() {
        return dbRunningProcessesSensorDao;
    }

    public DbRunningServicesSensorDao getDbRunningServicesSensorDao() {
        return dbRunningServicesSensorDao;
    }

    public DbRunningTasksSensorDao getDbRunningTasksSensorDao() {
        return dbRunningTasksSensorDao;
    }

    public DbAccountReaderSensorDao getDbAccountReaderSensorDao() {
        return dbAccountReaderSensorDao;
    }

    public DbNetworkTrafficSensorDao getDbNetworkTrafficSensorDao() {
        return dbNetworkTrafficSensorDao;
    }

    public DbBrowserHistorySensorDao getDbBrowserHistorySensorDao() {
        return dbBrowserHistorySensorDao;
    }

    public DbCallLogSensorDao getDbCallLogSensorDao() {
        return dbCallLogSensorDao;
    }

    public DbCalendarSensorDao getDbCalendarSensorDao() {
        return dbCalendarSensorDao;
    }

    public DbCalendarReminderSensorDao getDbCalendarReminderSensorDao() {
        return dbCalendarReminderSensorDao;
    }

    public DbContactSensorDao getDbContactSensorDao() {
        return dbContactSensorDao;
    }

    public DbContactNumberSensorDao getDbContactNumberSensorDao() {
        return dbContactNumberSensorDao;
    }

    public DbContactEmailSensorDao getDbContactEmailSensorDao() {
        return dbContactEmailSensorDao;
    }

    public DbPowerStateSensorDao getDbPowerStateSensorDao() {
        return dbPowerStateSensorDao;
    }

    public DbPowerLevelSensorDao getDbPowerLevelSensorDao() {
        return dbPowerLevelSensorDao;
    }

    public DbTucanSensorDao getDbTucanSensorDao() {
        return dbTucanSensorDao;
    }

    public DbFacebookSensorDao getDbFacebookSensorDao() {
        return dbFacebookSensorDao;
    }

}
