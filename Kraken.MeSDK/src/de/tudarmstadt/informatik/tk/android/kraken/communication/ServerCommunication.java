package de.tudarmstadt.informatik.tk.android.kraken.communication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.communication.endpoint.DeviceEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.device.DeviceRegistrationRequest;
import de.tudarmstadt.informatik.tk.android.kraken.util.KrakenUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ServerCommunication {

    private static final String TAG = ServerCommunication.class.getSimpleName();

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;

    private Context mContext;
    private int m_method;
    private String m_endpoint;
    private JSONObject m_jsonObject;
    private IServerCommunicationResponseHandler m_handler;
    private List<NameValuePair> m_getParams;

    public ServerCommunication(Context ctx, IServerCommunicationResponseHandler handler) {
        this.mContext = ctx;
        this.m_handler = handler;
    }

    public void getRequest(List<NameValuePair> params, String endpoint) {
        m_method = METHOD_GET;
        m_endpoint = endpoint;
        m_getParams = params;
        runThread();
    }

    public void postRequest(final String registrationToken) {


        final String userToken = PreferenceManager.getInstance(mContext).getUserToken();
        final long serverDeviceId = PreferenceManager.getInstance(mContext).getServerDeviceId();

        DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();

        deviceRegistrationRequest.setDeviceId(serverDeviceId);
        deviceRegistrationRequest.setRegistrationToken(registrationToken);

        DeviceEndpoint deviceEndpoint = ServiceGenerator.createService(DeviceEndpoint.class);
        deviceEndpoint.registerDevice(userToken, deviceRegistrationRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    saveRegistrationTokenToDb(serverDeviceId, registrationToken);

                } else {
                    // TODO: handle response null
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: handle error case
            }
        });


    }

    /**
     * Saves device GCM registration id to db
     *
     * @param deviceId
     * @param registrationToken
     */
    private void saveRegistrationTokenToDb(long deviceId, String registrationToken) {


    }

    private void runThread() {
        Thread thread = new Thread(new CommunicationThread());
        thread.setName("ServerCommunicationThread");
        thread.start();
    }

    private class CommunicationThread implements Runnable {

        @Override
        public void run() {

            // Log.i("jsonRequest", m_jsonObject.toString());

            AbstractHttpEntity entity;
            try {

                HttpUriRequest request;
                if (m_method == METHOD_GET) {
                    String url = KrakenSdkSettings.SERVER_URL + "/" + m_endpoint;
                    List<NameValuePair> params = m_getParams;
//                    params.add(new BasicNameValuePair("kroken", SdkAuthentication.getInstance(mContext).getKroken()));
                    params.add(new BasicNameValuePair("deviceId", KrakenUtils.getDeviceId(mContext)));
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    request = new HttpGet(url + "?" + paramString);
                    Log.d("kraken", url + "?" + paramString);

                } else if (m_method == METHOD_POST) {
                    HttpPost requestPost = new HttpPost(KrakenSdkSettings.SERVER_URL + "/api");
//                    authenticate(m_jsonObject);
//                    addDeviceId(m_jsonObject);

                    String json = m_jsonObject.toString();

                    Log.i("jsonRequest", json);
                    /*
                    entity = new AbstractHttpEntity() {

                        public boolean isRepeatable() {
                            return false;
                        }

                        public long getContentLength() {
                            return -1;
                        }

                        public boolean isStreaming() {
                            return false;
                        }

                        public InputStream getContent() throws IOException {
                            // Should be implemented as well but is irrelevant for this case
                            throw new UnsupportedOperationException();
                        }

                        public void writeTo(final OutputStream outstream) throws IOException {
                            ObjectMapper mapper = KrakenService.getJacksonObjectMapper();
                            mapper.writeValue(outstream, m_jsonObject);
                        }

                    };
                    */

                    entity = new StringEntity(json, "UTF-8");

                    entity.setContentType("application/json;charset=UTF-8");// text/plain;charset=UTF-8
                    entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                    requestPost.setEntity(entity);
                    request = requestPost;
                } else {
                    throw new InvalidParameterException("No valid method specified.");
                }

                DefaultHttpClient httpClient = new DefaultHttpClient();

                HttpResponse response = httpClient.execute(request);

                HttpEntity responseEntity = response.getEntity();

                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()), 65728);
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("jsonResponse", sb.toString());

                if (m_handler != null) {
                    Bundle data = new Bundle();
                    data.putBoolean("error", false);
                    // data.putString("request", m_jsonObject.toString());
                    data.putString("response", sb.toString());
                    m_handler.handleData(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (m_handler != null) {
                    Bundle data = new Bundle();
                    data.putBoolean("error", true);
                    m_handler.handleData(data);
                }
            }
        }

    }

}
