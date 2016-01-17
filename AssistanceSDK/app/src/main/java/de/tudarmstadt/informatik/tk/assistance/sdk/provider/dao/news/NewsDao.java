package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.news;

import android.support.annotation.Nullable;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNews;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDao;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ClientFeedbackDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface NewsDao extends CommonDao<DbNews> {

    List<DbNews> getAll(Long userId);

    @Nullable
    ClientFeedbackDto convert(DbNews dbNews);

    @Nullable
    DbNews convert(ClientFeedbackDto dbNews);
}
