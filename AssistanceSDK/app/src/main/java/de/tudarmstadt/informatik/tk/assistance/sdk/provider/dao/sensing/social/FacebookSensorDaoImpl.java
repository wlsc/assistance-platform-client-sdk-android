package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.social;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashSet;
import java.util.Set;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbFacebookSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor.social.FacebookSensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.CommonEventDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 17.01.2016
 */
public class FacebookSensorDaoImpl extends
        CommonEventDaoImpl<DbFacebookSensor> implements
        FacebookSensorDao {

    private static final String TAG = FacebookSensorDaoImpl.class.getSimpleName();

    private static FacebookSensorDao INSTANCE;

    private FacebookSensorDaoImpl(DaoSession daoSession) {
        super(daoSession.getDbFacebookSensorDao());
    }

    public static FacebookSensorDao getInstance(DaoSession mDaoSession) {

        if (INSTANCE == null) {
            INSTANCE = new FacebookSensorDaoImpl(mDaoSession);
        }

        return INSTANCE;
    }

    @Nullable
    @Override
    public SensorDto convertObject(DbFacebookSensor sensor) {

        if (sensor == null) {
            return null;
        }

        Set<String> perms = new HashSet<>();

        if (sensor.getPermissions() != null) {
            Gson gson = new Gson();
            perms = gson.fromJson(sensor.getPermissions(), new TypeToken<Set<String>>() {
            }.getType());
        }

        FacebookSensorDto result = new FacebookSensorDto(
                sensor.getOauthToken(),
                perms,
                sensor.getCreated()
        );

        return result;
    }
}