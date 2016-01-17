package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.device;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbDevice;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface DeviceDao extends CommonDao<DbDevice> {

    @Nullable
    DbDevice getById(long deviceId);

    boolean saveRegistrationTokenToDb(String registrationToken, long userId, long serverDeviceId);
    
}
