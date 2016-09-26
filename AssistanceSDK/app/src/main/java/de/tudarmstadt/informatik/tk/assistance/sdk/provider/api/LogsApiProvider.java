package de.tudarmstadt.informatik.tk.assistance.sdk.provider.api;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.ApiGenerator;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.logs.LogsApi;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.logs.SensorUploadLogsRequestDto;
import rx.Observable;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public final class LogsApiProvider {

    private static final String TAG = LoginApiProvider.class.getSimpleName();

    private static LogsApiProvider INSTANCE;

    private final LogsApi api;

    private LogsApiProvider(Context context) {
        api = ApiGenerator.getInstance(context).create(LogsApi.class);
    }

    public static LogsApiProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new LogsApiProvider(context);
        }

        return INSTANCE;
    }

    /**
     *
     * @param requestDto
     * @return
     */
    public Observable<Void> uploadSensorLogs(SensorUploadLogsRequestDto requestDto) {
        return api.uploadSensorLogs(requestDto);
    }
}