package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.services.KrakenService;

public class ServerPushManager {

    private int intPeriodicPushDelayInMin = 0;
    private int intPeriodicPushPeriodInMin = 1;

    private static ServerPushManager m_messageHandler;
    private HashSet<ISensor> m_setImmediate = new HashSet<ISensor>();
    private HashSet<ISensor> m_setPeriodic = new HashSet<ISensor>();
    private HashSet<ISensor> m_setWlan = new HashSet<ISensor>();
    private List<SensorData> m_liCachedFlushManuallyData = new LinkedList<SensorData>();
    private List<SensorData> m_liCachedFlushManuallyOnlyWlanData = new LinkedList<SensorData>();
    private long m_longLastPeriodicPushTimestamp = -1;
    private boolean m_bIsWlanConnected = false;

    private static Future<?> m_future;
    protected ScheduledExecutorService m_scheduledTaskExecutor;
    private static Context m_context;

    public static ServerPushManager getInstance(Context ctx) {
        if (m_messageHandler == null) {
            m_messageHandler = new ServerPushManager();
        }

        if (m_future == null)
            m_messageHandler.startPeriodicPush();

        ServerPushManager.m_context = ctx.getApplicationContext();
        return m_messageHandler;
    }

    private ServerPushManager() {
        m_scheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setWlanConnected(boolean bConnected) {
        if (bConnected) {
            m_bIsWlanConnected = true;
            stopPeriodicPush();
            flushData(EPushType.ALL);
        } else {
            m_bIsWlanConnected = false;
            startPeriodicPush();
        }
    }

    private void startPeriodicPush() {
        if (m_future == null) {
            m_future = m_scheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    flushData(EPushType.PERIODIC);
                }
            }, intPeriodicPushDelayInMin, intPeriodicPushPeriodInMin, TimeUnit.MINUTES);
        }
    }

    public static void stopPeriodicPush() {
        if (m_future != null) {
            m_future.cancel(true);
            m_future = null;
        }
    }

    public void inform(ISensor sensor) {
        if (m_setImmediate.contains(sensor)) {
            flushData(sensor);
        } else if (m_bIsWlanConnected) {
            flushData(EPushType.ALL);
        }
    }

    @SuppressWarnings("incomplete-switch")
    public void setPushType(ISensor sensor, EPushType type) {
        switch (type) {
            case IMMEDIATE:
                m_setPeriodic.remove(sensor);
                m_setWlan.remove(sensor);
                m_setImmediate.add(sensor);
                break;
            case PERIODIC:
                m_setImmediate.remove(sensor);
                m_setWlan.remove(sensor);
                m_setPeriodic.add(sensor);
                break;
            case WLAN_ONLY:
                m_setImmediate.remove(sensor);
                m_setPeriodic.remove(sensor);
                m_setWlan.add(sensor);
                break;
        }
    }

    private void flushData(ISensor sensor) {
        KrakenService service = KrakenService.getInstance();
        if (service == null)
            return;

        Authentication auth = Authentication.getInstance(m_context);
        if (auth == null)
            return;

        JSONArray jsonArray = new JSONArray();
        try {
            List<SensorData> liSensorData = new LinkedList<SensorData>();
            SensorData sensorData = sensor.flushData(service.getDaoSession());
            liSensorData.add(sensorData);
            jsonArray.put(sensorData.getJsonData());
            sendSensorData(jsonArray, liSensorData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void flushAll() {
        flushData(EPushType.ALL);
    }

    private void flushData(EPushType type) {
        KrakenService service = KrakenService.getInstance();
        if (service == null)
            return;

        Authentication auth = Authentication.getInstance(m_context);
        if (auth == null)
            return;

        JSONArray jsonArray = new JSONArray();
        List<SensorData> liSensorData = new LinkedList<SensorData>();
        buildSensorsDataArray(service.getDaoSession(), type, jsonArray, liSensorData);
        sendSensorData(jsonArray, liSensorData);
    }

    private void sendSensorData(JSONArray jsonArray, List<SensorData> liSensorData) {
        if (jsonArray == null || jsonArray.length() == 0)
            return;

        // add cached data!
        addCachedDataToArray(jsonArray, liSensorData);

        //splitAndSend(liSensorData);

//        postJsonArray(jsonArray, liSensorData);

        /*

        int maxLength = 10;

        Log.d("kraken", "length: " + jsonArray.length());
        try {
            Log.d("kraken", "first elem: " + jsonArray.getJSONObject(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(jsonArray.length() > maxLength) {

            Log.d("kraken", "splitting...");

            JSONArray jsonTemp = new JSONArray();
            List<SensorData> listTemp = new LinkedList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    SensorData sd = liSensorData.get(i);
                    jsonTemp.put(jo);
                    listTemp.add(sd);
                    if((i+1) % maxLength == 0) {
                        Log.d("kraken", i + " posting part...");
                        postJsonArray(jsonTemp, listTemp);
                        jsonTemp = new JSONArray();
                        listTemp = new LinkedList<>();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(jsonTemp.length() > 0) {
                Log.d("kraken", "posting the rest...");
                postJsonArray(jsonTemp, listTemp);
            }
        }
        else {
            postJsonArray(jsonArray, liSensorData);
        }
        */

        // new ServerCommunication(context).execute(jsonObject);
    }

//    private void splitAndSend(List<SensorData> liSensorData) {
//        int maxElems = 50;
//        for (SensorData data : liSensorData) {
//            List<? extends IDbSensor> sensorEntities = data.getSensorEntities();
//            final int N = sensorEntities.size();
//            Log.d("kraken", "splitAndSend: N=" + N);
//            for (int i = 0; i < N; i += maxElems) {
//                Log.d("kraken", "splitAndSend: split! " + i + "," + Math.min(N, i + maxElems));
//                List<? extends IDbSensor> part = sensorEntities.subList(i, Math.min(N, i + maxElems));
//
//                JSONObject json = null;
//                try {
//                    json = getJson(part, data.getSensor().getSensorType());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                data.setSensorEntities(part);
//
//                ServerPushManagerResponseHandler responseHandler = new ServerPushManagerResponseHandler(Collections.singletonList(data), m_context);
//                ServerCommunication com = new ServerCommunication(m_context, responseHandler);
//                com.postRequest(json);
//            }
//        }
//    }

//    private JSONObject getJson(List<? extends IDbSensor> data, ESensorType sensorType) throws JSONException {
//        JSONArray jsonArray = new JSONArray();
//        ObjectMapper mapper = KrakenService.getJacksonObjectMapper();
//        for (IDbSensor sensor : data) {
//            JSONObject jsonObj = mapper.convertValue(sensor, JSONObject.class);
//            jsonObj.remove("id");
//            jsonArray.put(jsonObj);
//        }
//        String strClassNameForServer = sensorType.getServerClassName();
//
//        JSONObject jsonPayload = new JSONObject();
//        jsonPayload.put("class", strClassNameForServer);
//        jsonPayload.put("objs", jsonArray);
//
//        // header
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(MessageType.KEY_MESSAGE_TYPE, MessageType.PERSONAL_DATA);
//        jsonObject.put("payload", jsonPayload);
//        return jsonObject;
//    }

//    private void postJsonArray(JSONArray jsonArray, List<SensorData> liSensorData) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(MessageType.KEY_MESSAGE_TYPE, MessageType.BUNDLE);
//            jsonObject.put(MessageType.KEY_DATA, jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ServerPushManagerResponseHandler responseHandler = new ServerPushManagerResponseHandler(liSensorData, m_context);
//        ServerCommunication com = new ServerCommunication(m_context, responseHandler);
//        com.postRequest(jsonObject);
//    }

    private void buildSensorsDataArray(DaoSession daoSession, EPushType type, JSONArray returnArray, List<SensorData> liSensorData) {
        switch (type) {
            case IMMEDIATE:
                addSensorsToArray(daoSession, m_setImmediate, returnArray, liSensorData);
            case PERIODIC:
                addPeriodicPushingSensorsToArray(daoSession, m_setPeriodic, returnArray, liSensorData);
            case WLAN_ONLY:
                addSensorsToArray(daoSession, m_setWlan, returnArray, liSensorData);
            case ALL: {
                addSensorsToArray(daoSession, m_setImmediate, returnArray, liSensorData);
                addSensorsToArray(daoSession, m_setPeriodic, returnArray, liSensorData);
                addSensorsToArray(daoSession, m_setWlan, returnArray, liSensorData);
            }
        }
    }

    private void addSensorsToArray(DaoSession daoSession, HashSet<ISensor> set, JSONArray jsonArray, List<SensorData> liSensorData) {
        for (ISensor sensor : set) {
            try {
                SensorData data = sensor.flushData(daoSession);
                if (data != null) {
                    liSensorData.add(data);
                    JSONObject insert = data.getJsonData();
                    jsonArray.put(insert);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addPeriodicPushingSensorsToArray(DaoSession daoSession, HashSet<ISensor> set, JSONArray jsonArray, List<SensorData> liSensorData) {
        long longCurrentTimestamp = Calendar.getInstance().getTimeInMillis();

        for (ISensor sensor : set) {
            try {
                int intPushIntervall = sensor.getPushIntervalInMin();
                long longPushIntervall = intPushIntervall * 60 * 1000;

                if (m_longLastPeriodicPushTimestamp + longPushIntervall < longCurrentTimestamp) {
                    SensorData data = sensor.flushData(daoSession);
                    if (data != null) {
                        liSensorData.add(data);
                        JSONObject insert = data.getJsonData();
                        jsonArray.put(insert);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        m_longLastPeriodicPushTimestamp = longCurrentTimestamp;
    }

    private void addCachedDataToArray(JSONArray jsonArray, List<SensorData> liSensorData) {

        for (SensorData data : m_liCachedFlushManuallyData) {
            jsonArray.put(data.getJsonData());
            liSensorData.add(data);
        }
        m_liCachedFlushManuallyData.clear();

        if (m_bIsWlanConnected) {
            for (SensorData data : m_liCachedFlushManuallyOnlyWlanData) {
                jsonArray.put(data.getJsonData());
                liSensorData.add(data);
            }
            m_liCachedFlushManuallyOnlyWlanData.clear();
        }
    }

    public void flushManually(SensorData... sensorData) {
        if (sensorData == null)
            return;

        JSONArray jsonArray = new JSONArray();
        List<SensorData> liSensorData = new LinkedList<SensorData>();

        for (SensorData data : sensorData) {
            if (data == null)
                continue;
            liSensorData.add(data);
            JSONObject insert = data.getJsonData();
            jsonArray.put(insert);
        }
        sendSensorData(jsonArray, liSensorData);
    }

    public void addToCache(List<SensorData> liSensorData) {
        for (SensorData data : liSensorData) {
            EPushType type = data.getSensor().getPushType();
            if (type.equals(EPushType.MANUALLY_IMMEDIATE)) {
                m_liCachedFlushManuallyData.add(data);
            } else if (type.equals(EPushType.MANUALLY_WLAN_ONLY)) {
                m_liCachedFlushManuallyOnlyWlanData.add(data);
            }
        }
    }
}
