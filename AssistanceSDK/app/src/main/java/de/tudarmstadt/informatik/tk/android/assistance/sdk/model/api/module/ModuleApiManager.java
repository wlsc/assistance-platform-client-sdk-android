package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.module;

import android.content.Context;

import java.util.List;
import java.util.Set;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.ApiGenerator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.01.2016
 */
public class ModuleApiManager {

    private static final String TAG = ModuleApiManager.class.getSimpleName();

    private static ModuleApiManager INSTANCE;

    private final Context mContext;

    private final ModuleApi api;

    private ModuleApiManager(Context context) {
        mContext = context;

        api = ApiGenerator.getInstance(context).create(ModuleApi.class);
    }

    public static ModuleApiManager getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleApiManager(context);
        }

        return INSTANCE;
    }

    public Observable<List<ModuleResponseDto>> getAvailableModules(String userToken) {
        return api.getAvailableModules(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Set<String>> getActiveModulesRequest(final String userToken) {
        return api.getActiveModules(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ActivatedModulesResponse> getActivatedModules(String userToken) {
        return Observable.combineLatest(
                getAvailableModules(userToken),
                getActiveModulesRequest(userToken),
                ActivatedModulesResponse::new);
    }
}