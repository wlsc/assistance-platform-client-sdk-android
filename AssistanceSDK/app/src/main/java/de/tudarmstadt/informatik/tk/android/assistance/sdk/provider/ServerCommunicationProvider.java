package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device.DeviceRegistrationRequestDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.device.DeviceApi;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;
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

    private static DaoProvider daoProvider;

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

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        return INSTANCE;
    }

    /**
     * Sends GCM registration token to backend
     *
     * @param registrationToken
     */
    public void sendGcmRegistrationToken(final String registrationToken) {

        final String userToken = PreferenceProvider.getInstance(mContext).getUserToken();
        final long serverDeviceId = PreferenceProvider.getInstance(mContext).getServerDeviceId();

        DeviceRegistrationRequestDto deviceRegistrationRequest = new DeviceRegistrationRequestDto();

        deviceRegistrationRequest.setDeviceId(serverDeviceId);
        deviceRegistrationRequest.setRegistrationToken(registrationToken);

        DeviceApi deviceApi = ApiGenerator.getInstance(mContext).create(DeviceApi.class);
        deviceApi.registerDevice(userToken, deviceRegistrationRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    // persist registration
                    final String userToken = PreferenceProvider.getInstance(mContext).getUserToken();

                    DbUser user = daoProvider.getUserDao().getByToken(userToken);

                    if (user == null) {
                        Log.d(TAG, "No such user found! Token: " + userToken);
                        return;
                    } else {

                        final long serverDeviceId = PreferenceProvider.getInstance(mContext).getServerDeviceId();

                        daoProvider.getDeviceDao().saveRegistrationTokenToDb(
                                registrationToken,
                                user.getId(),
                                serverDeviceId);
                    }
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

}
