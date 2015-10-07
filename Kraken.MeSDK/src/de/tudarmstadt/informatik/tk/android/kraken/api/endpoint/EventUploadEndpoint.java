package de.tudarmstadt.informatik.tk.android.kraken.api.endpoint;

import de.tudarmstadt.informatik.tk.android.kraken.Config;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public interface EventUploadEndpoint {

    @POST(Config.ASSISTANCE_EVENT_UPLOAD_ENDPOINT)
    void uploadData(@Header("X-AUTH-TOKEN") String userToken,
                    @Body EventUploadRequest body,
                    Callback<Void> callback);

}
