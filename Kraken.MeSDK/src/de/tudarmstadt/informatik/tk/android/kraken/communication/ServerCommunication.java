package de.tudarmstadt.informatik.tk.android.kraken.communication;

import android.content.Context;

import org.json.JSONObject;

import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.communication.endpoint.DeviceEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.device.DeviceRegistrationRequest;
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

    public ServerCommunication(Context ctx, IServerCommunicationResponseHandler handler) {
        this.mContext = ctx;
        this.m_handler = handler;
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
}
