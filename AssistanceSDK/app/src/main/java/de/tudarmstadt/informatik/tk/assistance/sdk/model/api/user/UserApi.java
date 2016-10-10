package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.profile.ProfileResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.profile.UpdateProfileRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.registration.RegistrationRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.registration.RegistrationResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.resetpassword.ResetPasswordRequestDto;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;


/**
 * User service API endpoint
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28.06.2015
 */
public interface UserApi {

    @POST(Config.ASSISTANCE_USER_REGISTER_ENDPOINT)
    Observable<RegistrationResponseDto> registerUser(@Body RegistrationRequestDto body);

    @POST(Config.ASSISTANCE_USER_PASSWORD_ENDPOINT)
    Observable<Void> resetUserPassword(@Body ResetPasswordRequestDto body);

    @GET(Config.ASSISTANCE_USER_PROFILE_SHORT_ENDPOINT)
    Observable<ProfileResponseDto> getUserProfileShort(@Header("X-AUTH-TOKEN") String userToken);

    @GET(Config.ASSISTANCE_USER_PROFILE_FULL_ENDPOINT)
    Observable<ProfileResponseDto> getUserProfileFull(@Header("X-AUTH-TOKEN") String userToken);

    @PUT(Config.ASSISTANCE_USER_PROFILE_UPDATE_ENDPOINT)
    Observable<Void> updateUserProfile(@Header("X-AUTH-TOKEN") String userToken,
                                       @Body UpdateProfileRequestDto body);
}