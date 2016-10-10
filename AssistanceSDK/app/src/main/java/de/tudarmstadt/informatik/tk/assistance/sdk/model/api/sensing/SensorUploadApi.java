package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing;


import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public interface SensorUploadApi {

    @POST(Config.ASSISTANCE_SENSOR_UPLOAD_SERVICE_ENDPOINT)
    Observable<SensorUploadResponseDto> uploadData(@Header("X-AUTH-TOKEN") String userToken,
                                                   @Body SensorUploadRequestDto body);
}