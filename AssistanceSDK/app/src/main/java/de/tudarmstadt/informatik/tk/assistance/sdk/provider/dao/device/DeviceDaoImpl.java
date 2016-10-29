package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.device;

import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbDevice;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbDeviceDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
@Singleton
public final class DeviceDaoImpl extends
        CommonDaoImpl<DbDevice> implements
        DeviceDao {

    private static final String TAG = DeviceDaoImpl.class.getSimpleName();

    @Inject
    public DeviceDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbDeviceDao());
    }

    /**
     * Returns db user device by id
     *
     * @param deviceId
     * @return
     */
    @Nullable
    @Override
    public DbDevice getById(long deviceId) {

        if (deviceId < 0) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.Id.eq(deviceId))
                .limit(1)
                .build()
                .unique();
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

        DbDevice device = dao
                .queryBuilder()
                .where(Properties.UserId.eq(userId))
                .where(Properties.ServerDeviceId.eq(serverDeviceId))
                .build()
                .unique();

        if (device == null) {

            Log.d(TAG, "Not found any device with id: " + serverDeviceId);
            return false;

        } else {

            device.setGcmRegistrationToken(registrationToken);

            update(device);

            Log.d(TAG, "Finished saving GCM token");

            return true;
        }
    }
}