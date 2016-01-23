package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing;


import com.squareup.okhttp.Response;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public interface SensorUploadApi {

    @POST(Config.ASSISTANCE_EVENT_UPLOAD_SERVICE_ENDPOINT)
    Observable<Response> uploadData(@Header("X-AUTH-TOKEN") String userToken,
                                    @Body SensorUploadRequestDto body);
}