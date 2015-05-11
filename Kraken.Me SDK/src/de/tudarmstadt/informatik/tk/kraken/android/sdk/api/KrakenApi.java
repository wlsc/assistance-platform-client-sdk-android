package de.tudarmstadt.informatik.tk.kraken.android.sdk.api;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiResponse;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ForegroundEvent;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import static de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiResponse
        .ArrayApiResponse;

/**
 * KrakenApi
 *
 * @author Karsten Planz
 */
public interface KrakenApi {

    @POST("/api")
    void postData(@Body ApiMessage message, Callback<ApiResponse.BundleApiResponse> cb);

    @GET("/rest/visualization/foregroundEvents")
    ArrayApiResponse<ForegroundEvent> getForegroundEvents(@Query("startDate") long startDate,
                                                          @Query("endDate") long endDate,
                                                          @Query("kroken") String kroken,
                                                          @Query("deviceId") String deviceId);
}
