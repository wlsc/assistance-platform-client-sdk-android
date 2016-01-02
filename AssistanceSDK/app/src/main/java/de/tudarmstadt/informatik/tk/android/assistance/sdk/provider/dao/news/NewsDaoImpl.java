package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.news;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNews;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNewsDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ClientFeedbackDto;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ContentDto;

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
    public List<DbNews> getAll(Long userId) {

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
    public ClientFeedbackDto convert(DbNews dbNews) {

        if (dbNews == null) {
            return null;
        }

        ClientFeedbackDto clientFeedbackDto = null;

        try {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();

            ContentDto content = gson.fromJson(parser.parse(dbNews.getContent()), ContentDto.class);
            clientFeedbackDto = new ClientFeedbackDto(
                    dbNews.getDbModule().getPackageName(),
                    content,
                    dbNews.getCreated());

        } catch (JsonSyntaxException e) {
            Log.d(TAG, "json Syntax error");
        } catch (Exception e) {
            Log.d(TAG, "Something happened");
        }

        return clientFeedbackDto;
    }

    @Override
    public DbNews convert(ClientFeedbackDto feedbackDto) {

        if (feedbackDto == null) {
            return null;
        }

        DbNews result = new DbNews();

        try {
            Gson gson = new Gson();

            result.setContent(gson.toJson(feedbackDto.getContent()));
            result.setCreated(feedbackDto.getCreated());

        } catch (JsonSyntaxException e) {
            Log.d(TAG, "json Syntax error");
        } catch (Exception e) {
            Log.d(TAG, "Something happened");
        }

        return result;
    }

    @Override
    public DbNews get(Long id) {

        if (id == null) {
            return null;
        }

        return dao
                .queryBuilder()
                .where(DbNewsDao.Properties.Id.eq(id))
                .limit(1)
                .build()
                .unique();
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
