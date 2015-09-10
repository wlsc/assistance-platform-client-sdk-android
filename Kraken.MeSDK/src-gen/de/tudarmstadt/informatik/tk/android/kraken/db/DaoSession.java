package de.tudarmstadt.informatik.tk.android.kraken.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import de.tudarmstadt.informatik.tk.android.kraken.db.User;
import de.tudarmstadt.informatik.tk.android.kraken.db.UserSocialProfile;
import de.tudarmstadt.informatik.tk.android.kraken.db.Login;
import de.tudarmstadt.informatik.tk.android.kraken.db.Device;
import de.tudarmstadt.informatik.tk.android.kraken.db.Module;
import de.tudarmstadt.informatik.tk.android.kraken.db.ModuleCapability;
import de.tudarmstadt.informatik.tk.android.kraken.db.ModuleInstallation;
import de.tudarmstadt.informatik.tk.android.kraken.db.PositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.GyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.MagneticFieldSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.MotionActivityEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.ConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.WifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.MobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.LoudnessEvent;

import de.tudarmstadt.informatik.tk.android.kraken.db.UserDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.UserSocialProfileDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.LoginDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DeviceDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.ModuleDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.ModuleCapabilityDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.ModuleInstallationDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.PositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.GyroscopeSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.AccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.MagneticFieldSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.MotionActivityEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.ConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.WifiConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.MobileConnectionEventDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.LoudnessEventDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig userSocialProfileDaoConfig;
    private final DaoConfig loginDaoConfig;
    private final DaoConfig deviceDaoConfig;
    private final DaoConfig moduleDaoConfig;
    private final DaoConfig moduleCapabilityDaoConfig;
    private final DaoConfig moduleInstallationDaoConfig;
    private final DaoConfig positionSensorDaoConfig;
    private final DaoConfig gyroscopeSensorDaoConfig;
    private final DaoConfig accelerometerSensorDaoConfig;
    private final DaoConfig magneticFieldSensorDaoConfig;
    private final DaoConfig motionActivityEventDaoConfig;
    private final DaoConfig connectionEventDaoConfig;
    private final DaoConfig wifiConnectionEventDaoConfig;
    private final DaoConfig mobileConnectionEventDaoConfig;
    private final DaoConfig loudnessEventDaoConfig;

    private final UserDao userDao;
    private final UserSocialProfileDao userSocialProfileDao;
    private final LoginDao loginDao;
    private final DeviceDao deviceDao;
    private final ModuleDao moduleDao;
    private final ModuleCapabilityDao moduleCapabilityDao;
    private final ModuleInstallationDao moduleInstallationDao;
    private final PositionSensorDao positionSensorDao;
    private final GyroscopeSensorDao gyroscopeSensorDao;
    private final AccelerometerSensorDao accelerometerSensorDao;
    private final MagneticFieldSensorDao magneticFieldSensorDao;
    private final MotionActivityEventDao motionActivityEventDao;
    private final ConnectionEventDao connectionEventDao;
    private final WifiConnectionEventDao wifiConnectionEventDao;
    private final MobileConnectionEventDao mobileConnectionEventDao;
    private final LoudnessEventDao loudnessEventDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        userSocialProfileDaoConfig = daoConfigMap.get(UserSocialProfileDao.class).clone();
        userSocialProfileDaoConfig.initIdentityScope(type);

        loginDaoConfig = daoConfigMap.get(LoginDao.class).clone();
        loginDaoConfig.initIdentityScope(type);

        deviceDaoConfig = daoConfigMap.get(DeviceDao.class).clone();
        deviceDaoConfig.initIdentityScope(type);

        moduleDaoConfig = daoConfigMap.get(ModuleDao.class).clone();
        moduleDaoConfig.initIdentityScope(type);

        moduleCapabilityDaoConfig = daoConfigMap.get(ModuleCapabilityDao.class).clone();
        moduleCapabilityDaoConfig.initIdentityScope(type);

        moduleInstallationDaoConfig = daoConfigMap.get(ModuleInstallationDao.class).clone();
        moduleInstallationDaoConfig.initIdentityScope(type);

        positionSensorDaoConfig = daoConfigMap.get(PositionSensorDao.class).clone();
        positionSensorDaoConfig.initIdentityScope(type);

        gyroscopeSensorDaoConfig = daoConfigMap.get(GyroscopeSensorDao.class).clone();
        gyroscopeSensorDaoConfig.initIdentityScope(type);

        accelerometerSensorDaoConfig = daoConfigMap.get(AccelerometerSensorDao.class).clone();
        accelerometerSensorDaoConfig.initIdentityScope(type);

        magneticFieldSensorDaoConfig = daoConfigMap.get(MagneticFieldSensorDao.class).clone();
        magneticFieldSensorDaoConfig.initIdentityScope(type);

        motionActivityEventDaoConfig = daoConfigMap.get(MotionActivityEventDao.class).clone();
        motionActivityEventDaoConfig.initIdentityScope(type);

        connectionEventDaoConfig = daoConfigMap.get(ConnectionEventDao.class).clone();
        connectionEventDaoConfig.initIdentityScope(type);

        wifiConnectionEventDaoConfig = daoConfigMap.get(WifiConnectionEventDao.class).clone();
        wifiConnectionEventDaoConfig.initIdentityScope(type);

        mobileConnectionEventDaoConfig = daoConfigMap.get(MobileConnectionEventDao.class).clone();
        mobileConnectionEventDaoConfig.initIdentityScope(type);

        loudnessEventDaoConfig = daoConfigMap.get(LoudnessEventDao.class).clone();
        loudnessEventDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        userSocialProfileDao = new UserSocialProfileDao(userSocialProfileDaoConfig, this);
        loginDao = new LoginDao(loginDaoConfig, this);
        deviceDao = new DeviceDao(deviceDaoConfig, this);
        moduleDao = new ModuleDao(moduleDaoConfig, this);
        moduleCapabilityDao = new ModuleCapabilityDao(moduleCapabilityDaoConfig, this);
        moduleInstallationDao = new ModuleInstallationDao(moduleInstallationDaoConfig, this);
        positionSensorDao = new PositionSensorDao(positionSensorDaoConfig, this);
        gyroscopeSensorDao = new GyroscopeSensorDao(gyroscopeSensorDaoConfig, this);
        accelerometerSensorDao = new AccelerometerSensorDao(accelerometerSensorDaoConfig, this);
        magneticFieldSensorDao = new MagneticFieldSensorDao(magneticFieldSensorDaoConfig, this);
        motionActivityEventDao = new MotionActivityEventDao(motionActivityEventDaoConfig, this);
        connectionEventDao = new ConnectionEventDao(connectionEventDaoConfig, this);
        wifiConnectionEventDao = new WifiConnectionEventDao(wifiConnectionEventDaoConfig, this);
        mobileConnectionEventDao = new MobileConnectionEventDao(mobileConnectionEventDaoConfig, this);
        loudnessEventDao = new LoudnessEventDao(loudnessEventDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(UserSocialProfile.class, userSocialProfileDao);
        registerDao(Login.class, loginDao);
        registerDao(Device.class, deviceDao);
        registerDao(Module.class, moduleDao);
        registerDao(ModuleCapability.class, moduleCapabilityDao);
        registerDao(ModuleInstallation.class, moduleInstallationDao);
        registerDao(PositionSensor.class, positionSensorDao);
        registerDao(GyroscopeSensor.class, gyroscopeSensorDao);
        registerDao(AccelerometerSensor.class, accelerometerSensorDao);
        registerDao(MagneticFieldSensor.class, magneticFieldSensorDao);
        registerDao(MotionActivityEvent.class, motionActivityEventDao);
        registerDao(ConnectionEvent.class, connectionEventDao);
        registerDao(WifiConnectionEvent.class, wifiConnectionEventDao);
        registerDao(MobileConnectionEvent.class, mobileConnectionEventDao);
        registerDao(LoudnessEvent.class, loudnessEventDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        userSocialProfileDaoConfig.getIdentityScope().clear();
        loginDaoConfig.getIdentityScope().clear();
        deviceDaoConfig.getIdentityScope().clear();
        moduleDaoConfig.getIdentityScope().clear();
        moduleCapabilityDaoConfig.getIdentityScope().clear();
        moduleInstallationDaoConfig.getIdentityScope().clear();
        positionSensorDaoConfig.getIdentityScope().clear();
        gyroscopeSensorDaoConfig.getIdentityScope().clear();
        accelerometerSensorDaoConfig.getIdentityScope().clear();
        magneticFieldSensorDaoConfig.getIdentityScope().clear();
        motionActivityEventDaoConfig.getIdentityScope().clear();
        connectionEventDaoConfig.getIdentityScope().clear();
        wifiConnectionEventDaoConfig.getIdentityScope().clear();
        mobileConnectionEventDaoConfig.getIdentityScope().clear();
        loudnessEventDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserSocialProfileDao getUserSocialProfileDao() {
        return userSocialProfileDao;
    }

    public LoginDao getLoginDao() {
        return loginDao;
    }

    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public ModuleDao getModuleDao() {
        return moduleDao;
    }

    public ModuleCapabilityDao getModuleCapabilityDao() {
        return moduleCapabilityDao;
    }

    public ModuleInstallationDao getModuleInstallationDao() {
        return moduleInstallationDao;
    }

    public PositionSensorDao getPositionSensorDao() {
        return positionSensorDao;
    }

    public GyroscopeSensorDao getGyroscopeSensorDao() {
        return gyroscopeSensorDao;
    }

    public AccelerometerSensorDao getAccelerometerSensorDao() {
        return accelerometerSensorDao;
    }

    public MagneticFieldSensorDao getMagneticFieldSensorDao() {
        return magneticFieldSensorDao;
    }

    public MotionActivityEventDao getMotionActivityEventDao() {
        return motionActivityEventDao;
    }

    public ConnectionEventDao getConnectionEventDao() {
        return connectionEventDao;
    }

    public WifiConnectionEventDao getWifiConnectionEventDao() {
        return wifiConnectionEventDao;
    }

    public MobileConnectionEventDao getMobileConnectionEventDao() {
        return mobileConnectionEventDao;
    }

    public LoudnessEventDao getLoudnessEventDao() {
        return loudnessEventDao;
    }

}
