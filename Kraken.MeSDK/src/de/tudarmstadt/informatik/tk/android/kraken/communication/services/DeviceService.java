package de.tudarmstadt.informatik.tk.android.kraken.communication.services;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.models.api.device.DeviceListResponse;
import de.tudarmstadt.informatik.tk.android.kraken.models.api.device.DeviceRegistrationRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * API endpoints for managing user's devices
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public interface DeviceService {

    @POST(KrakenSdkSettings.DEVICE_REGISTRATION_ENDPOINT)
    void registerDevice(@Header("X-AUTH-TOKEN") String userToken,
                        @Body DeviceRegistrationRequest body,
                        Callback<Void> callback);

    @GET(KrakenSdkSettings.DEVICE_LIST_ENDPOINT)
    void getDeviceList(@Header("X-AUTH-TOKEN") String userToken,
                       Callback<List<DeviceListResponse>> callback);

}
