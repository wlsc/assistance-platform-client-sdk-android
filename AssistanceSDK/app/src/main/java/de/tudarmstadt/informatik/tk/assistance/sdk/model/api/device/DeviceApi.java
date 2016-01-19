package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.device;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

/**
 * API endpoints for managing user's devices
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 13.09.2015
 */
public interface DeviceApi {

    @POST(Config.DEVICE_REGISTRATION_ENDPOINT)
    Observable<Void> registerDevice(@Header("X-AUTH-TOKEN") String userToken,
                                    @Body DeviceRegistrationRequestDto body);

    @GET(Config.DEVICE_LIST_ENDPOINT)
    Observable<List<DeviceListResponseDto>> getDeviceList(
            @Header("X-AUTH-TOKEN") String userToken);

    @POST(Config.DEVICE_SET_USER_DEFINED_NAME_ENDPOINT)
    Observable<Void> setUserDefinedName(@Header("X-AUTH-TOKEN") String userToken,
                                        @Body DeviceUserDefinedNameRequestDto body);
}