package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.user;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface UserDao {

    DbUser getUserByEmail(String userEmail);

    DbUser getUserByToken(String userToken);

    long insertUser(DbUser user);

    void updateUser(DbUser user);

    void delete(List<DbUser> dbItems);
}
