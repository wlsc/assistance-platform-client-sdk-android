package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module;

import java.util.List;
import java.util.Set;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ClientFeedbackDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
            @Header("X-AUTH-TOKEN") String userToken);

    @GET(Config.ASSISTANCE_MODULE_ACTIVE_ENDPOINT)
    Observable<Set<String>> getActiveModules(@Header("X-AUTH-TOKEN") String userToken);

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