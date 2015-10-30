package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.user;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbUser;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface UserDao {

    DbUser getUserByEmail(String userEmail);

    DbUser getUserByToken(String userToken);

    long insertUser(DbUser user);

    void updateUser(DbUser user);
}
