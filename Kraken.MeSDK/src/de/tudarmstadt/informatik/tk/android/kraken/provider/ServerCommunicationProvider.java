package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.device.DeviceRegistrationRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.endpoint.DeviceEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.endpoint.EndpointGenerator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Communicates with server backend
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.08.2015
 */
public class ServerCommunicationProvider {

    private static final String TAG = ServerCommunicationProvider.class.getSimpleName();

    private static Context mContext;

    private static ServerCommunicationProvider INSTANCE;

    private static DbProvider dbProvider;

    private ServerCommunicationProvider() {
    }

    /**
     * Gives a singleton of this class back
     *
     * @param context
     * @return
     */
    public static ServerCommunicationProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ServerCommunicationProvider();
        }

        mContext = context;

        if (dbProvider == null) {
            dbProvider = DbProvider.getInstance(context);
        }

        return INSTANCE;
    }

    /**
     * Sends GCM registration token to backend
     *
     * @param registrationToken
     */
    public void sendGcmRegistrationToken(final String registrationToken) {

        final String userToken = PreferenceManager.getInstance(mContext).getUserToken();
        final long serverDeviceId = PreferenceManager.getInstance(mContext).getServerDeviceId();

        DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();

        deviceRegistrationRequest.setDeviceId(serverDeviceId);
        deviceRegistrationRequest.setRegistrationToken(registrationToken);

        DeviceEndpoint deviceEndpoint = EndpointGenerator.create(DeviceEndpoint.class);
        deviceEndpoint.registerDevice(userToken, deviceRegistrationRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    // persist registration
                    dbProvider.saveRegistrationTokenToDb(serverDeviceId, registrationToken);

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

//    public void
}
