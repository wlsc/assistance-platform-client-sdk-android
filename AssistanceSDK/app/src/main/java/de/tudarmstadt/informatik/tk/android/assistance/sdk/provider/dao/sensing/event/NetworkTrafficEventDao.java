package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NetworkTrafficEventDao extends CommonEventDao<DbNetworkTrafficEvent> {

    List<DbNetworkTrafficEvent> getAllBackground();

    List<DbNetworkTrafficEvent> getAllForeground();

    List<DbNetworkTrafficEvent> getFirstNBackground(int amount);

    List<DbNetworkTrafficEvent> getFirstNForeground(int amount);

}
