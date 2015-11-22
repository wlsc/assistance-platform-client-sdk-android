package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.device;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbDevice;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface DeviceDao extends CommonDao<DbDevice> {

    DbDevice getById(long deviceId);

    boolean saveRegistrationTokenToDb(String registrationToken, long userId, long serverDeviceId);
    
}
