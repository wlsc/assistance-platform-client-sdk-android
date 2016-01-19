package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorUploadDto;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.01.2016
 */
public class SensorUploadApiProvider {

    private static final String TAG = SensorUploadApiProvider.class.getSimpleName();

    private static SensorUploadApiProvider INSTANCE;

    private final SensorUploadApi api;

    private SensorUploadApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(SensorUploadApi.class);
    }

    public static SensorUploadApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new SensorUploadApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     * Returns subscription for sensor upload request
     *
     * @param userToken
     * @param sensorUploadDto
     * @return
     */
    public Observable<Void> uploadData(String userToken, SensorUploadDto sensorUploadDto) {
        return api.uploadData(userToken, sensorUploadDto)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}