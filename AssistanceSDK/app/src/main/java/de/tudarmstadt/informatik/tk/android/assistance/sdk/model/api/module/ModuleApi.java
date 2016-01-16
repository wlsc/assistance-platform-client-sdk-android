package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.module;

import java.util.List;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ClientFeedbackDto;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Module service API endpoint
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.06.2015
 */
public interface ModuleApi {

    @GET(Config.ASSISTANCE_MODULE_LIST_ENDPOINT)
    Observable<List<ModuleResponseDto>> getAvailableModules(
            @Header("X-AUTH-TOKEN") String userToken
    );

    @GET(Config.ASSISTANCE_MODULE_ACTIVE_ENDPOINT)
    Observable<Set<String>> getActiveModules(
            @Header("X-AUTH-TOKEN") String userToken
    );

    @POST(Config.ASSISTANCE_MODULE_ACTIVATE_ENDPOINT)
    Observable<Void> activateModule(@Header("X-AUTH-TOKEN") String userToken,
                                    @Body ToggleModuleRequestDto body);

    @POST(Config.ASSISTANCE_MODULE_DEACTIVATE_ENDPOINT)
    Observable<Void> deactivateModule(@Header("X-AUTH-TOKEN") String userToken,
                                      @Body ToggleModuleRequestDto body);

    @GET(Config.ASSISTANCE_MODULE_FEEDBACK_ENDPOINT)
    Observable<List<ClientFeedbackDto>> getModuleFeedback(@Header("X-AUTH-TOKEN") String userToken,
                                                          @Path("deviceId") Long deviceId);
}