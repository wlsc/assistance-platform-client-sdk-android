package de.tudarmstadt.informatik.tk.android.kraken.api;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.kraken.api.entities.CalendarEvent;
import de.tudarmstadt.informatik.tk.android.kraken.api.entities.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.communication.IServerCommunicationResponseHandler;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;


/**
 * @author Karsten Planz
 */
public class ApiManager {

    private static ApiManager mInstance;
    private final Context mContext;

    public ApiManager(Context context) {
        mContext = context;
    }

    public static ApiManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiManager(context);
        }
        return mInstance;
    }

    public void getSensordata(ESensorType type, long startDate, long endDate, IServerCommunicationResponseHandler handler) {
//        String className = type.getServerClassName();
//        List<NameValuePair> params = new LinkedList<>();
//        params.add(new BasicNameValuePair("startDate", String.valueOf(startDate)));
//        params.add(new BasicNameValuePair("endDate", String.valueOf(endDate)));
        // params.add(new BasicNameValuePair("sensorClass", className));
//        new ServerCommunication(mContext, handler).getRequest(params, "rest/sensordata/" + className);
    }

    public void getNextCalendarEvents(int limit, Set<String> calendars, IServerCommunicationResponseHandler handler) {
//        List<NameValuePair> params = new LinkedList<>();
//        params.add(new BasicNameValuePair("limit", String.valueOf(limit)));
//        for (String calendar : calendars) {
//            params.add(new BasicNameValuePair("calendars", calendar));
//        }
//        new ServerCommunication(mContext, handler).getRequest(params, "rest/visualization/nextCalendarEvent");
    }

    public void getForegroundEvents(long startDate, long endDate, IServerCommunicationResponseHandler handler) {
//        List<NameValuePair> params = new LinkedList<>();
//        params.add(new BasicNameValuePair("startDate", String.valueOf(startDate)));
//        params.add(new BasicNameValuePair("endDate", String.valueOf(endDate)));
//        new ServerCommunication(mContext, handler).getRequest(params, "rest/visualization/foregroundEvents");
    }

    public static class SensordataHandler<T> implements IServerCommunicationResponseHandler {

        private final Callback mCallback;

        public interface Callback<T> {
            void onData(List<T> data);

            void onError();
        }

        public SensordataHandler(Callback callback) {
            this.mCallback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleData(Bundle data) {

            if (data.containsKey("error") && !data.getBoolean("error") && data.containsKey("response")) {
                String strResponse = data.getString("response");
                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    String type = jsonResponse.optString("type");
                    //Class<? extends IDbSensor> androidClass = ESensorType.getAndroidClass(type);
                    JSONArray jsonData = jsonResponse.getJSONArray("data");
//                    Class<T> classType = (Class<T>) ESensorType.getAndroidClass(type);
                    List<T> sensorData = new LinkedList<>();
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject item = jsonData.getJSONObject(i);
//                        T sensorDataItem = KrakenUtils.getJacksonObjectMapper().convertValue(item, classType);
//                        sensorData.add(sensorDataItem);
                    }
                    mCallback.onData(sensorData);
                } catch (JSONException | ClassCastException e) {
                    e.printStackTrace();
                }
            } else {
                mCallback.onError();
            }
        }
    }

    public static class CalendarEventsHandler implements IServerCommunicationResponseHandler {

        private final Callback mCallback;

        public interface Callback {
            void onData(List<CalendarEvent> data);

            void onError();
        }

        public CalendarEventsHandler(Callback callback) {
            this.mCallback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleData(Bundle data) {

            if (data.containsKey("error") && !data.getBoolean("error") && data.containsKey("response")) {
                String strResponse = data.getString("response");
                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    JSONArray jsonData = jsonResponse.getJSONArray("data");
                    List<CalendarEvent> calendarEvents = new LinkedList<>();
                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONObject item = jsonData.getJSONObject(i);
//                        CalendarEvent sensorDataItem = KrakenUtils.getJacksonObjectMapper().convertValue(item, CalendarEvent.class);
//                        calendarEvents.add(sensorDataItem);
                    }
                    mCallback.onData(calendarEvents);
                } catch (JSONException | ClassCastException e) {
                    e.printStackTrace();
                }
            } else {
                mCallback.onError();
            }
        }
    }

    public static class ForegroundEventsHandler implements IServerCommunicationResponseHandler {

        private final Callback mCallback;

        public interface Callback {
            void onData(List<ForegroundEvent> data);

            void onError(String message);
        }

        public ForegroundEventsHandler(Callback callback) {
            this.mCallback = callback;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleData(Bundle data) {

            if (data.containsKey("error") && !data.getBoolean("error") && data.containsKey("response")) {
                String strResponse = data.getString("response");
                try {
                    JSONObject jsonResponse = new JSONObject(strResponse);
                    boolean bSucc = jsonResponse.getBoolean("succ");
                    if (bSucc) {
                        try {

                            JSONArray jsonData = jsonResponse.getJSONArray("data");
                            List<ForegroundEvent> calendarEvents = new LinkedList<>();
                            for (int i = 0; i < jsonData.length(); i++) {
                                JSONObject item = jsonData.getJSONObject(i);
//                                ForegroundEvent sensorDataItem = KrakenUtils.getJacksonObjectMapper().convertValue(item, ForegroundEvent.class);
//                                calendarEvents.add(sensorDataItem);
                            }
                            mCallback.onData(calendarEvents);
                        } catch (JSONException | ClassCastException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JSONObject jsonError = jsonResponse.getJSONObject("error");
                        String strMsg = jsonError.getString("msg");
                        String strCause = jsonError.getString("cause");
                        throw new Exception(strCause + ": " + strMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onError(e.getMessage());
                }
            } else {
                mCallback.onError(null);
            }
        }
    }

}
