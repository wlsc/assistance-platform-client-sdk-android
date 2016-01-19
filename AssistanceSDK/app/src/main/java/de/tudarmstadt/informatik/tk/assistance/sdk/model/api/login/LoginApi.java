package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.12.2015
 */
public interface LoginApi {

    @POST(Config.ASSISTANCE_USER_LOGIN_ENDPOINT)
    Observable<LoginResponseDto> loginUser(@Body LoginRequestDto body);
}