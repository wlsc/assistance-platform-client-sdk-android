package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.device;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbDevice;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface DeviceDao {

    DbDevice getDeviceById(long deviceId);

    long insertDevice(DbDevice device);

    void updateDevice(DbDevice device);

    boolean saveRegistrationTokenToDb(String registrationToken, long userId, long serverDeviceId);
}
