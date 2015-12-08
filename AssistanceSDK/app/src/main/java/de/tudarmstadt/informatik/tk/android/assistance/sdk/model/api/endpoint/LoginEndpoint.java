package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.endpoint;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.login.LoginResponseDto;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
public interface LoginEndpoint {

    @POST(Config.ASSISTANCE_USER_LOGIN_ENDPOINT)
    void loginUser(@Body LoginRequestDto body,
                   Callback<LoginResponseDto> callback);

}
