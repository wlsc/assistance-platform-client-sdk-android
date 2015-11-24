package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.event;

import java.util.Collections;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessEventDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensing.event.LoudnessEventDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class LoudnessEventDaoImpl extends
        CommonEventDaoImpl<DbLoudnessEvent> implements
        LoudnessEventDao {

    private static final String TAG = LoudnessEventDaoImpl.class.getSimpleName();

    private static LoudnessEventDao INSTANCE;

    private LoudnessEventDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbLoudnessEventDao());
    }

    public static LoudnessEventDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new LoudnessEventDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Override
    public SensorDto convertObject(DbLoudnessEvent sensor) {

        if (sensor == null) {
            return null;
        }

        LoudnessEventDto result = new LoudnessEventDto();

        result.setLoudness(sensor.getLoudness());
        result.setCreated(sensor.getCreated());
        result.setType(DtoType.LOUDNESS);
        result.setTypeStr(DtoType.getApiName(DtoType.LOUDNESS));

        return result;
    }

    @Override
    public List<DbLoudnessEvent> getLastN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .orderDesc(DbLoudnessEventDao.Properties.Id)
                .limit(amount)
                .build()
                .list();
    }
}
