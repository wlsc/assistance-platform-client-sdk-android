package de.tudarmstadt.informatik.tk.android.kraken.communication.services;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenConfig;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.09.2015
 */
public interface EventUploadService {

    @POST(KrakenConfig.ASSISTANCE_EVENT_UPLOAD_ENDPOINT)
    void uploadData(@Header("X-AUTH-TOKEN") String userToken,
                    @Body EventUploadRequest body,
                    Callback<Void> callback);

}
