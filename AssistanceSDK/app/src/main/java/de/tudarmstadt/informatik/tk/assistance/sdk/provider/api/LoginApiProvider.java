package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.login.LoginResponseDto;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 19.01.2016
 */
public final class LoginApiProvider {

    private static final String TAG = LoginApiProvider.class.getSimpleName();

    private static LoginApiProvider INSTANCE;

    private final LoginApi api;

    private LoginApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(LoginApi.class);
    }

    public static LoginApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new LoginApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Returns subscription for user login request
     *
     * @param loginRequest
     * @return
     */
    public Observable<LoginResponseDto> loginUser(LoginRequestDto loginRequest) {
        return api.loginUser(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}