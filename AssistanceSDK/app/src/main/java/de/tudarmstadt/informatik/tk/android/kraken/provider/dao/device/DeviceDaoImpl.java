package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.device;

import android.util.Log;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDevice;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbDeviceDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class DeviceDaoImpl implements DeviceDao {

    private static final String TAG = DeviceDaoImpl.class.getSimpleName();

    private static DeviceDao INSTANCE;

    private DbDeviceDao deviceDao;

    private DeviceDaoImpl(DaoSession daoSession) {

        if (deviceDao == null) {
            deviceDao = daoSession.getDbDeviceDao();
        }
    }

    public static DeviceDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new DeviceDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Returns db user device by id
     *
     * @param deviceId
     * @return
     */
    @Override
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
     * Inserts new device
     *
     * @param device
     * @return
     */
    @Override
    public long insertDevice(DbDevice device) {

        if (device == null) {
            return -1L;
        }

        return deviceDao.insertOrReplace(device);
    }

    /**
     * Updates device
     *
     * @param device
     */
    @Override
    public void updateDevice(DbDevice device) {

        if (device == null) {
            return;
        }

        deviceDao.update(device);
    }

    /**
     * Saves device GCM registration id to db
     *
     * @param registrationToken
     */
    @Override
    public boolean saveRegistrationTokenToDb(String registrationToken, long userId, long serverDeviceId) {

        Log.d(TAG, "Saving GCM registration token to DB...");

        if (registrationToken == null) {
            Log.e(TAG, "GCM registration token IS null!");
            return false;
        }

        DbDevice device = deviceDao
                .queryBuilder()
                .where(DbDeviceDao.Properties.UserId.eq(userId))
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

}
