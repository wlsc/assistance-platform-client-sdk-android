package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.user;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUser;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbUserDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public class UserDaoImpl implements UserDao {

    private static final String TAG = UserDaoImpl.class.getSimpleName();

    private static UserDao INSTANCE;

    private DbUserDao dao;

    private UserDaoImpl(DaoSession daoSession) {

        if (dao == null) {
            dao = daoSession.getDbUserDao();
        }
    }

    public static UserDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new UserDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Returns db user by email
     *
     * @param userEmail
     * @return
     */
    @Override
    public DbUser getUserByEmail(String userEmail) {

        if (userEmail == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbUserDao.Properties.PrimaryEmail.eq(userEmail))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns db user by registered token
     *
     * @param userToken
     * @return
     */
    @Override
    public DbUser getUserByToken(String userToken) {

        if (userToken == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbUserDao.Properties.Token.eq(userToken))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Inserts new user
     *
     * @return
     */
    @Override
    public long insertUser(DbUser user) {

        if (user == null) {
            return -1L;
        }

        return dao.insertOrReplace(user);
    }

    /**
     * Updates user
     *
     * @param user
     */
    @Override
    public void updateUser(DbUser user) {

        if (user == null) {
            return;
        }

        dao.update(user);
    }

    @Override
    public void delete(List<DbUser> dbItems) {

        if (dbItems == null || dbItems.isEmpty()) {
            return;
        }

        dao.deleteInTx(dbItems);
    }

}
