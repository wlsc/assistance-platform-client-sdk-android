package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.assistance.model.client.feedback.content.ClientFeedbackDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ActivatedModulesResponse;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ModuleApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ModuleResponseDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.module.ToggleModuleRequestDto;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 02.01.2016
 */
public final class ModuleApiProvider {

    private static final String TAG = ModuleApiProvider.class.getSimpleName();

    private static final long FEEDBACK_POLLING_RATE = 10;
    private static final TimeUnit FEEDBACK_TIME_UNIT = TimeUnit.SECONDS;
    public static final int FEEDBACK_RETRY_NUMBER = 3;

    private static ModuleApiProvider INSTANCE;

    private final ModuleApi api;

    private ModuleApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(ModuleApi.class);
    }

    public static ModuleApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ModuleApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Returns subscription to available modules
     *
     * @param userToken
     * @return
     */
    public Observable<List<ModuleResponseDto>> getAvailableModules(final String userToken) {
        return api.getAvailableModules(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription to active modules
     *
     * @param userToken
     * @return
     */
    public Observable<Set<String>> getActiveModulesRequest(final String userToken) {
        return api.getActiveModules(userToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription to available activated modules (combined request)
     *
     * @param userToken
     * @return
     */
    public Observable<ActivatedModulesResponse> getActivatedModules(final String userToken) {
        return Observable.combineLatest(
                getAvailableModules(userToken),
                getActiveModulesRequest(userToken),
                ActivatedModulesResponse::new);
    }

    /**
     * Returns subscription to module activation
     *
     * @param userToken
     * @param request
     * @return
     */
    public Observable<Void> activateModule(final String userToken,
                                           final ToggleModuleRequestDto request) {
        return api.activateModule(userToken, request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription to module deactivation
     *
     * @param userToken
     * @param request
     * @return
     */
    public Observable<Void> deactivateModule(final String userToken,
                                             final ToggleModuleRequestDto request) {
        return api.deactivateModule(userToken, request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription to module feedback channel
     *
     * @param userToken
     * @param deviceId
     * @return
     */
    public Observable<List<ClientFeedbackDto>> moduleFeedback(final String userToken,
                                                              final Long deviceId) {
        return api.getModuleFeedback(userToken, deviceId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns subscription for periodic to module feedback channel
     *
     * @param userToken
     * @param deviceId
     * @return
     */
    public Observable<List<ClientFeedbackDto>> moduleFeedbackPeriodic(final String userToken,
                                                                      final Long deviceId) {
        Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

        return Observable.interval(FEEDBACK_POLLING_RATE, FEEDBACK_TIME_UNIT)
                .flatMap(f ->
                        api.getModuleFeedback(userToken, deviceId)
                                .retry(FEEDBACK_RETRY_NUMBER)
                                .subscribeOn(scheduler))
                .observeOn(AndroidSchedulers.mainThread());
    }
}