package de.tudarmstadt.informatik.tk.android.kraken.communication.services;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.models.api.http.device.DeviceRegistrationRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public interface DeviceService {

    @POST(KrakenSdkSettings.DEVICE_REGISTRATION_ENDPOINT)
    void registerDevice(@Header("X-AUTH-TOKEN") String userToken,
                        @Body DeviceRegistrationRequest body,
                        Callback<Void> callback);

}
