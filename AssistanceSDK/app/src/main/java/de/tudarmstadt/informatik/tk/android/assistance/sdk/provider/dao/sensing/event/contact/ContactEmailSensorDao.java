package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.contact;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactEmailSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public interface ContactEmailSensorDao extends CommonEventDao<DbContactEmailSensor> {

    List<DbContactEmailSensor> getAll(Long contactId);
}