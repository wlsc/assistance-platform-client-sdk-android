package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.news;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNews;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNewsDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NewsDaoImpl extends
        CommonDaoImpl<DbNews> implements
        NewsDao {

    private static final String TAG = NewsDaoImpl.class.getSimpleName();

    private static NewsDao INSTANCE;

    private NewsDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbNewsDao());
    }

    public static NewsDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new NewsDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    /**
     * Returns list of DB cached user assistance entries
     *
     * @param userId
     * @return
     */
    @Override
    public List<DbNews> get(Long userId) {

        if (userId == null) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .where(DbNewsDao.Properties.UserId.eq(userId))
                .build()
                .list();
    }

    @Override
    public List<DbNews> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbNewsDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
