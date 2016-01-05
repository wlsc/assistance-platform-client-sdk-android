package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event.contact;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbContactNumberSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public interface ContactNumberEventDao extends CommonEventDao<DbContactNumberSensor> {

    List<DbContactNumberSensor> getAll(Long contactId);

}
