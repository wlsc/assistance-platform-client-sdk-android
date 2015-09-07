package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.abstract_sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.ActivityCommunicator;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.MessageType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.EPushType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.SensorData;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbUpdatableSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.services.KrakenService;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.DatabaseManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.DateUtils;

public abstract class AbstractSensor implements ISensor {

    private static final String TAG = AbstractSensor.class.getSimpleName();

    protected Context context;

    protected DaoSession m_daoSession;

    // for caching
    private Method m_insertMethod = null;
    private Method m_updateMethod;
    protected AbstractDao<?, Long> m_daoObject = null;
    protected Class<? extends IDbSensor> m_sensorClass = null;
    private Property m_propTimestamp;

    private boolean m_isDisabledByUser = false;
    private boolean m_isDisabledBySystem = false;
    protected long m_longLastDataFlushTimestamp = -1;
    protected int m_intPushIntervallInMin = 0;
    protected boolean m_bIsRunning = false;

    private HashMap<String, Boolean> m_mapNewData = new HashMap<String, Boolean>();

    public AbstractSensor(Context context) {
        setContext(context);
        SharedPreferences sharedPreferences = this.context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
        m_isDisabledByUser = sharedPreferences.getBoolean(getSensorType().toString() + KrakenSdkSettings.PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX, false);

        m_longLastDataFlushTimestamp = sharedPreferences.getLong(getSensorType().toString() + KrakenSdkSettings.PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX, -1);

    }

    protected void handleDBEntry(IDbSensor dbEntry) {
        handleDBEntry(dbEntry, false, true, true);
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
        if (m_daoSession != null) {

            if (m_sensorClass != dbEntry.getClass()) {
                m_sensorClass = dbEntry.getClass();
                try {
                    m_daoObject = getDaoObject(m_sensorClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                m_insertMethod = null;
                m_updateMethod = null;
            }

            try {
                if (bUpdate && m_updateMethod == null)
                    m_updateMethod = m_daoObject.getClass().getMethod("update", java.lang.Object.class);
                else if (!bUpdate && m_insertMethod == null)
                    m_insertMethod = m_daoObject.getClass().getMethod("insert", java.lang.Object.class);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "No such method found. Error: ", e);
            }
        }

        try {
            if (!bUpdate && m_insertMethod != null & m_daoObject != null) {
                try {
                    m_insertMethod.invoke(m_daoObject, dbEntry);
                } catch (Exception e) {
                    Log.e(TAG, "Could not insert object in database. Error: ", e);
                }
            } else if (bUpdate && m_updateMethod != null & m_daoObject != null) {
                m_updateMethod.invoke(m_daoObject, dbEntry);
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

        if (isSendToHandler) {
            sendDatabaseObject(dbEntry);
        }

        RetroServerPushManager.getInstance(context).inform(this);
    }

    protected void sendDatabaseObject(IDbSensor dbObject) {
        Bundle data = new Bundle();
        data.putSerializable("sensorType", getSensorType());
        data.putSerializable("sensorData", dbObject);
        ActivityCommunicator.getInstance().handleData(data);
    }

    protected AbstractDao<?, Long> getDaoObject(Class<? extends IDbSensor> sensorClass) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {

        String strSimpleClassNameOfDBObject = sensorClass.getSimpleName();
        return getDaoObject(strSimpleClassNameOfDBObject);
    }

    @SuppressWarnings("unchecked")
    protected AbstractDao<?, Long> getDaoObject(String strClassNameWithoutDaoPostfix) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {

        if (m_daoSession == null) {
            m_daoSession = DatabaseManager.getInstance(context).getDaoSession();
        }

        if (m_daoSession == null) {
            throw new IllegalAccessError("no valid daoSession available!");
        }

        String daoName = Character.toLowerCase(strClassNameWithoutDaoPostfix.charAt(0))
                + (strClassNameWithoutDaoPostfix.length() > 1 ? strClassNameWithoutDaoPostfix.substring(1) : "") + "Dao";
        Field field = DaoSession.class.getDeclaredField(daoName);
        field.setAccessible(true);
        return (AbstractDao<?, Long>) field.get(m_daoSession);
    }

    // @Override
    // public void setDaoSession(DaoSession session) {
    // m_daoObject = null;
    // m_insertMethod = null;
    // m_daoSession = session;
    // }

    @Override
    public void setContext(Context context) {
        this.context = context;
        if (context instanceof KrakenService)
            setDaoSession(((KrakenService) context).getDaoSession());
    }

    @Override
    public void setDisabledBySystem(boolean bDisabled) {
        m_isDisabledBySystem = bDisabled;
    }

    @Override
    public boolean isDisabledBySystem() {
        return m_isDisabledBySystem;
    }

    @Override
    public void setDisabledByUser(boolean bDisabled) {
        m_isDisabledByUser = bDisabled;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(getSensorType().toString() + KrakenSdkSettings.PREFERENCES_SENSOR_DISABLED_BY_USER_POSTFIX, bDisabled).commit();
    }

    @Override
    public boolean isDisabledByUser() {
        return m_isDisabledByUser;
    }

    @Override
    public SensorData flushData(DaoSession daoSession) throws JSONException {
        String strClassName = getSensorType().getFullqualifiedDatabaseClassName();
        return flushData(daoSession, strClassName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SensorData flushData(DaoSession daoSession, String strFullQualifiedBeanClassName) throws JSONException {

        long longTimestamp = Calendar.getInstance().getTimeInMillis();

        try {

            // caching is important, because reflections are not really
            // performant
            if (m_sensorClass == null || !m_sensorClass.getName().equals(strFullQualifiedBeanClassName)) {
                // getting database bean
                m_sensorClass = (Class<? extends IDbSensor>) Class.forName(strFullQualifiedBeanClassName);
                // get the *Dao object for doing a query
                m_daoObject = getDaoObject(m_sensorClass);
                m_propTimestamp = null;
            }

            QueryBuilder<? extends IDbSensor> qb = (QueryBuilder<? extends IDbSensor>) m_daoObject.queryBuilder();

            Query<? extends IDbSensor> query;
            try {
                query = getDbQuery(m_sensorClass, longTimestamp, qb);
            } catch (Exception e) {
                m_propTimestamp = null;
                query = getDbQuery(m_sensorClass, longTimestamp, qb);
            }

            // set timestamp of last query
            m_longLastDataFlushTimestamp = longTimestamp;
            //SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
            //sharedPreferences.edit()
            //		.putLong(strFullQualifiedBeanClassName + KrakenSdkSettings.PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX, m_longLastDataFlushTimestamp).commit();

            // handle every entry and convert it to Json
            List<? extends IDbSensor> list = query.list();
            if (list.size() > 0) {
                // sensor data
                SensorData sensorData = new SensorData();
                sensorData.setSensor(this);
                sensorData.setFullQualifiedBeanClassName(strFullQualifiedBeanClassName);

                List<IDbSensor> liSensorEntities = new LinkedList<IDbSensor>();
                JSONArray jsonArray = new JSONArray();
//				ObjectMapper mapper = KrakenService.getJacksonObjectMapper();
//				for (IDbSensor sensor : list) {
//					liSensorEntities.add(sensor);
//					JSONObject jsonObj = mapper.convertValue(sensor, JSONObject.class);
//					jsonObj.remove("id");
//					jsonArray.put(jsonObj);
//				}
                sensorData.setSensorEntities(list);

                // prepare sensor name: replace "SensorName" to "AndroidName":
                // "SensorLight" becomes "AndroidLight";
                // this is needed for generic implementation on server side.
                String strClassNameForServer = getSensorType().getServerClassName();
                // TODO: remove ugly workaround
                if (strFullQualifiedBeanClassName.equals("de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorCalendarEventReminder")) {
                    strClassNameForServer = "AndroidCalendarEventReminder";
                }

                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("class", strClassNameForServer);
                jsonPayload.put("objs", jsonArray);

                // header
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(MessageType.KEY_MESSAGE_TYPE, MessageType.PERSONAL_DATA);
                jsonObject.put("payload", jsonPayload);

                sensorData.setJsonData(jsonObject);

                return sensorData;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ApiMessage.DataWrapper flushDataRetro() {
        String strClassName = getSensorType().getFullqualifiedDatabaseClassName();
        return flushDataRetro(strClassName);
    }

    @Override
    public ApiMessage.DataWrapper flushDataRetro(String strFullQualifiedBeanClassName) {

        KrakenService service = KrakenService.getInstance();
        Boolean bDataAvailable = m_mapNewData.get(strFullQualifiedBeanClassName);
        if (service == null || bDataAvailable == null || !bDataAvailable)
            return null;

        long longTimestamp = Calendar.getInstance().getTimeInMillis();

        try {
            // caching is important, because reflections are not really
            // performant
            if (m_sensorClass == null || !m_sensorClass.getName().equals(strFullQualifiedBeanClassName)) {
                // getting database bean
                m_sensorClass = (Class<? extends IDbSensor>) Class.forName(strFullQualifiedBeanClassName);
                // get the *Dao object for doing a query
                m_daoObject = getDaoObject(m_sensorClass);
                m_propTimestamp = null;
            }

            QueryBuilder<? extends IDbSensor> qb = (QueryBuilder<? extends IDbSensor>) m_daoObject.queryBuilder();

            Query<? extends IDbSensor> query;
            try {
                query = getDbQuery(m_sensorClass, longTimestamp, qb);
            } catch (Exception e) {
                m_propTimestamp = null;
                query = getDbQuery(m_sensorClass, longTimestamp, qb);
            }

            // set timestamp of last query
            m_longLastDataFlushTimestamp = longTimestamp;
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit()
                    .putLong(strFullQualifiedBeanClassName + KrakenSdkSettings.PREFERENCES_SENSOR_LAST_PUSHED_TIMESTAMP_POSTFIX, m_longLastDataFlushTimestamp).commit();

            // handle every entry and convert it to Json
            List<? extends IDbSensor> list = query.list();
            if (list.size() > 0) {

                LinkedList<IDbSensor> sensorData = new LinkedList<>();
                for (IDbSensor sensor : list) {
                    sensorData.add(sensor);
                    //TODO: remove id? why?
                }
                ApiMessage.DataWrapper wrapper = new ApiMessage.DataWrapper();
                wrapper.objs = sensorData;
                wrapper.databaseClassName = strFullQualifiedBeanClassName;

                // prepare sensor name: replace "SensorName" to "AndroidName":
                // "SensorLight" becomes "AndroidLight";
                // this is needed for generic implementation on server side.
                String strClassNameForServer = getSensorType().getServerClassName();
                // TODO: remove ugly workaround
                if (strFullQualifiedBeanClassName.equals("de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorCalendarEventReminder")) {
                    strClassNameForServer = "AndroidCalendarEventReminder";
                }
                wrapper.className = strClassNameForServer;

                m_mapNewData.put(strFullQualifiedBeanClassName, false);
                return wrapper;
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Query<? extends IDbSensor> getDbQuery(Class<? extends IDbSensor> sensorClass, long longTimestamp, QueryBuilder<? extends IDbSensor> qb)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        if (m_propTimestamp == null)
            m_propTimestamp = getPropertiesArgument(sensorClass, "Timestamp");
        Query<? extends IDbSensor> query = qb.where(m_propTimestamp.gt(m_longLastDataFlushTimestamp), m_propTimestamp.le(longTimestamp)).build();
        return query;
    }

    /**
     * This method gets the Property field of the corresponding *Dao class of
     * the database bean. This is needed for filter by an Property in DB queues.
     *
     * @param sensorClass
     * @param strArgumentName
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    protected Property getPropertiesArgument(Class<? extends IDbSensor> sensorClass, String strArgumentName) throws ClassNotFoundException,
            NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        Class<?> cl = Class.forName(sensorClass.getName() + "Dao$Properties");
        Field field = cl.getField(strArgumentName);
        return (Property) field.get(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void removeDataFromDb(List<? extends IDbSensor> liSensorData, String strFullqualifiedSensorClassName) {

        try {
            if (strFullqualifiedSensorClassName == null)
                strFullqualifiedSensorClassName = getSensorType().getFullqualifiedDatabaseClassName();

            // caching is important, because reflections are not really
            // performant
            if (m_sensorClass == null || !m_sensorClass.getName().equals(strFullqualifiedSensorClassName)) {
                // getting database bean
                m_sensorClass = (Class<? extends IDbSensor>) Class.forName(strFullqualifiedSensorClassName);
                // get the *Dao object for doing a query
                m_daoObject = getDaoObject(m_sensorClass);
            }

            // We assume that every entry in this list is of the same type!
            if (liSensorData != null && liSensorData.size() > 0) {
                // UpdatableSensors won't be simply deleted from database. May
                // be we have to update the flags!
                if (liSensorData.get(0) instanceof IDbUpdatableSensor) {
                    List<IDbUpdatableSensor> liDelete = new LinkedList<IDbUpdatableSensor>();
                    List<IDbUpdatableSensor> liUpdate = new LinkedList<IDbUpdatableSensor>();
                    for (IDbSensor data : liSensorData) {
                        IDbUpdatableSensor event = (IDbUpdatableSensor) data;
                        if (event.getIsDeleted())
                            liDelete.add(event);
                        else {
                            event.setIsNew(false);
                            event.setIsUpdated(false);
                            liUpdate.add(event);
                        }
                    }
                    m_daoObject.updateInTx((Iterable) liUpdate);
                    m_daoObject.deleteInTx((Iterable) liDelete);
                } else {
                    m_daoObject.deleteInTx((Iterable) liSensorData);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public EPushType getPushType() {
        return EPushType.WLAN_ONLY;
    }

    public void configure(JSONObject json) {
        // stop sensor if necessary
        boolean bWasRunning = isRunning();
        if (bWasRunning)
            stopSensor();

        configure(json, this.getClass());

        // restart sensor if it was running
        if (bWasRunning)
            startSensor();
    }

    private void configure(JSONObject json, Class<?> configurationClass) {
        try {
            // get the Configuration-enum with all available configurations
            String strClassName = configurationClass.getName() + ("$Configuration");

            Class<?> classConfiguration = Class.forName(strClassName);

            // these are all available configuration parameters. check every of
            // them!
            Field[] fields = classConfiguration.getFields();
            for (Field configurationField : fields) {

                // the current parameter name
                String strConfigurationName = configurationField.getName();

                // check if there's a configuration parameter in json
                if (json.has(strConfigurationName)) {
                    try {

                        // this is the REAL parameter (not only the field which
                        // is declared in the enum
                        Field configurationAttribute = configurationClass.getDeclaredField(strConfigurationName);

                        try {
                            // set the parameter accessible and set the value
                            configurationAttribute.setAccessible(true);

                            if ("int".equals(configurationAttribute.getType().toString()))
                                configurationAttribute.set(this, json.getInt(strConfigurationName));
                            else if ("String".equals(configurationAttribute.getType().toString()))
                                configurationAttribute.set(this, json.getString(strConfigurationName));

                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (ClassNotFoundException e) {

        }

        if (configurationClass != AbstractSensor.class) {
            Class<?> superclass = configurationClass.getSuperclass();
            if (superclass != null && superclass != Object.class)
                configure(json, superclass);
        }
    }

    @Override
    public int getPushIntervalInMin() {
        return m_intPushIntervallInMin;
    }

    @Override
    public void setPushIntervalInMin(int intInterval) {
        m_intPushIntervallInMin = intInterval;
    }

    @Override
    public boolean isRunning() {
        return m_bIsRunning;
    }

    @Override
    public void setDaoSession(DaoSession daoSession) {
        this.m_daoSession = daoSession;
    }
}
