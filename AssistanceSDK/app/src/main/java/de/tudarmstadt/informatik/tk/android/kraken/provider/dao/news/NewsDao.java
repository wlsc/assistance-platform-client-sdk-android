package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.news;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbNews;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NewsDao {

    List<DbNews> getNews(Long userId);
    
}
