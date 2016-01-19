package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.UserApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.profile.ProfileResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.profile.UpdateProfileRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.registration.RegistrationRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.registration.RegistrationResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.user.resetpassword.ResetPasswordRequestDto;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 19.01.2016
 */
public class UserApiProvider {

    private static final String TAG = UserApiProvider.class.getSimpleName();

    private static UserApiProvider INSTANCE;

    private final UserApi api;

    private UserApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(UserApi.class);
    }

    public static UserApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new UserApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Returns subscription for user registration request
     *
     * @param requestDto
     * @return
     */
    public Observable<RegistrationResponseDto> registerUser(RegistrationRequestDto requestDto) {
        return api.registerUser(requestDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for user password reset request
     *
     * @param requestDto
     * @return
     */
    public Observable<Void> resetUserPassword(ResetPasswordRequestDto requestDto) {
        return api.resetUserPassword(requestDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for get user profile SHORT request
     *
     * @param userToken
     * @return
     */
    public Observable<ProfileResponseDto> getUserProfileShort(String userToken) {
        return api.getUserProfileShort(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for get user profile FULL request
     *
     * @param userToken
     * @return
     */
    public Observable<ProfileResponseDto> getUserProfileFull(String userToken) {
        return api.getUserProfileFull(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for update user profile request
     *
     * @param userToken
     * @return
     */
    public Observable<Void> updateUserProfile(String userToken,
                                              UpdateProfileRequestDto updateProfileRequestDto) {
        return api.updateUserProfile(userToken, updateProfileRequestDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}