package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.user;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface UserDao extends CommonDao<DbUser> {

    DbUser getByEmail(String email);

    DbUser getByToken(String token);
}
