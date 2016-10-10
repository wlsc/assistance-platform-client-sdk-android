package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.logs;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public interface LogsApi {

    @POST(Config.ASSISTANCE_LOGS_SENSOR_UPLOAD_SERVICE_ENDPOINT)
    Observable<Void> uploadSensorLogs(@Body SensorUploadLogsRequestDto body);
}