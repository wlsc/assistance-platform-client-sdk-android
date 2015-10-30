package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.news;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNews;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbNewsDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public class NewsDaoImpl implements NewsDao {

    private static final String TAG = NewsDaoImpl.class.getSimpleName();

    private static NewsDao INSTANCE;

    private DbNewsDao newsDao;

    private NewsDaoImpl(DaoSession daoSession) {

        if (newsDao == null) {
            newsDao = daoSession.getDbNewsDao();
        }
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
    public List<DbNews> getNews(Long userId) {

        if (userId == null) {
            return Collections.EMPTY_LIST;
        }

        return newsDao
                .queryBuilder()
                .where(DbNewsDao.Properties.UserId.eq(userId))
                .build()
                .list();
    }
}
