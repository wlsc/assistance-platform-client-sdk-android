package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.user;

import android.support.annotation.Nullable;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUserDao.Properties;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 29.10.2015
 */
public final class UserDaoImpl extends
        CommonDaoImpl<DbUser> implements
        UserDao {

    private static final String TAG = UserDaoImpl.class.getSimpleName();

    private static UserDao INSTANCE;

    private UserDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbUserDao());
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
     * @param email
     * @return
     */
    @Nullable
    @Override
    public DbUser getByEmail(String email) {

        if (email == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.PrimaryEmail.eq(email))
                .limit(1)
                .build()
                .unique();
    }

    /**
     * Returns db user by registered token
     *
     * @param token
     * @return
     */
    @Nullable
    @Override
    public DbUser getByToken(String token) {

        if (token == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(Properties.Token.eq(token))
                .limit(1)
                .build()
                .unique();
    }
}