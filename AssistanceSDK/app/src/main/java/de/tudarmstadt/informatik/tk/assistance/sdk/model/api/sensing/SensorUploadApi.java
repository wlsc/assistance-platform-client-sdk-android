package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public interface SensorUploadApi {

    @POST(Config.ASSISTANCE_EVENT_UPLOAD_SERVICE_ENDPOINT)
    void uploadData(@Header("X-AUTH-TOKEN") String userToken,
                    @Body SensorUploadDto body,
                    Callback<Void> callback);

}
