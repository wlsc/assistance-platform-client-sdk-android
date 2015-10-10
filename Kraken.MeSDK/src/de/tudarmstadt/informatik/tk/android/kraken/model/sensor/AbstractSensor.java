package de.tudarmstadt.informatik.tk.android.kraken.model.sensor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.kraken.service.HarvesterService;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;

public abstract class AbstractSensor implements ISensor {

    private static final String TAG = AbstractSensor.class.getSimpleName();

    protected Context context;

    protected DaoSession mDaoSession;

    // for caching
    private Method mInsertMethod = null;
    private Method mUpdateMethod;
    protected AbstractDao<?, Long> mDaoObject = null;
    protected Class<? extends IDbSensor> mSensorClass = null;
    private Property m_propTimestamp;

    private boolean isDisabledByUser = false;
    private boolean isDisabledBySystem = false;
    protected long lastDataFlushTimestamp = -1;
    protected int pushIntervallInMin = 0;
    protected boolean isRunning = false;

    private HashMap<String, Boolean> m_mapNewData = new HashMap<String, Boolean>();

    public AbstractSensor(Context context) {
        setContext(context);
//        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
//        isDisabledByUser = sharedPreferences.getBoolean(getType().toString() + KrakenSdkSettings.PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX, false);
//
//        lastDataFlushTimestamp = sharedPreferences.getLong(getType().toString() + KrakenSdkSettings.PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX, -1);

        if (mDaoSession == null) {
            mDaoSession = DbProvider.getInstance(context).getDaoSession();
        }
    }

    protected void handleDBEntry(IDbSensor dbEntry, boolean bUpdate) {
        handleDBEntry(dbEntry, bUpdate, true, true);
    }

    protected void handleDBEntry(IDbSensor dbEntry, boolean bUpdate, boolean isTimeCreatedNeeded, boolean isSendToHandler) {

        if (isTimeCreatedNeeded) {
            final String now = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());
            dbEntry.setCreated(now);
        }

        // init vars
        if (mDaoSession != null) {

            if (mSensorClass != dbEntry.getClass()) {
                mSensorClass = dbEntry.getClass();
                try {
                    mDaoObject = getDaoEntry(mSensorClass);
                } catch (Exception e) {
                    Log.e(TAG, "Cant get DAO entry! Error: ", e);
                }
                mInsertMethod = null;
                mUpdateMethod = null;
            }

            try {
                if (bUpdate && mUpdateMethod == null)
                    mUpdateMethod = mDaoObject.getClass().getMethod("update", java.lang.Object.class);
                else if (!bUpdate && mInsertMethod == null)
                    mInsertMethod = mDaoObject.getClass().getMethod("insert", java.lang.Object.class);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "No such method found. Error: ", e);
            }
        }

        try {
            if (!bUpdate && mInsertMethod != null & mDaoObject != null) {
                try {
                    mInsertMethod.invoke(mDaoObject, dbEntry);
                } catch (Exception e) {
                    Log.e(TAG, "Could not insert object in database. Error: ", e);
                }
            } else if (bUpdate && mUpdateMethod != null & mDaoObject != null) {
                mUpdateMethod.invoke(mDaoObject, dbEntry);
            }
            String strClassName = dbEntry.getClass().getName();
            m_mapNewData.put(strClassName, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

//        if (isSendToHandler) {
//            saveDbEntry(dbEntry);
//        }

//        RetroServerPushManager.getInstance(context).inform(this);
    }

//    protected void saveDbEntry(IDbSensor dbObject) {
//
//        Bundle data = new Bundle();
//
//        data.putSerializable("sensorType", getType());
//        data.putSerializable("sensorData", dbObject);
//
//        ActivityCommunicator.getInstance().handleData(data);
//    }

    protected AbstractDao<?, Long> getDaoEntry(Class<? extends IDbSensor> sensorClass) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {

        String strSimpleClassNameOfDBObject = sensorClass.getSimpleName();
        return getDaoEntry(strSimpleClassNameOfDBObject);
    }

    protected AbstractDao<?, Long> getDaoEntry(String strClassNameWithoutDaoPostfix) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {

        if (mDaoSession == null) {
            mDaoSession = DbProvider.getInstance(context).getDaoSession();
        }

        if (mDaoSession == null) {
            throw new IllegalAccessError("no valid daoSession available!");
        }

        String daoName = Character.toLowerCase(strClassNameWithoutDaoPostfix.charAt(0))
                + (strClassNameWithoutDaoPostfix.length() > 1 ? strClassNameWithoutDaoPostfix.substring(1) : "") + "Dao";
        Field field = DaoSession.class.getDeclaredField(daoName);
        field.setAccessible(true);
        return (AbstractDao<?, Long>) field.get(mDaoSession);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
        if (context instanceof HarvesterService) {
            setDaoSession(((HarvesterService) context).getDaoSession());
        }
    }

    @Override
    public void setDisabledBySystem(boolean isDisabled) {
        isDisabledBySystem = isDisabled;
    }

    @Override
    public boolean isDisabledBySystem() {
        return isDisabledBySystem;
    }

    @Override
    public void setDisabledByUser(boolean isDisabled) {
        isDisabledByUser = isDisabled;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PreferenceProvider.PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(SensorType.getApiName(getType()) + PreferenceProvider.PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX, isDisabled).apply();
    }

    @Override
    public boolean isDisabledByUser() {
        return isDisabledByUser;
    }

    @Override
    public EPushType getPushType() {
        return EPushType.WLAN_ONLY;
    }

    @Override
    public int getPushIntervalInMin() {
        return pushIntervallInMin;
    }

    @Override
    public void setPushIntervalInMin(int intInterval) {
        pushIntervallInMin = intInterval;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setDaoSession(DaoSession daoSession) {
        this.mDaoSession = daoSession;
    }

    protected abstract void dumpData();
}
