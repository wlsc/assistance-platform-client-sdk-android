package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.os.Handler;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.LogsSensorUpload;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.logs.SensorUploadLogsRequestDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.ApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.api.LogsApiProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.RxUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 23.01.2016
 */
public class LogsUploadService extends GcmTaskService {

    private static final String TAG = LogsUploadService.class.getSimpleName();

    private DaoProvider daoProvider;

    private Subscription logsSubscription;
    private static List<LogsSensorUpload> sensorUploadLogs;

    @Override
    public int onRunTask(TaskParams taskParams) {

        Log.d(TAG, "Task uploader has started");

        // check Airplane Mode enabled
        if (ConnectionUtils.isAirplaneModeEnabled(this)) {
            Log.d(TAG, "Airplane Mode enabled. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        // device is not online
        if (!ConnectionUtils.isOnline(this)) {
            Log.d(TAG, "Device is not online. Upload request ignored");
            return GcmNetworkManager.RESULT_FAILURE;
        }

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(getApplicationContext());
        }

        Handler handler = new Handler(getMainLooper());
        handler.post(() -> prepareData());

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    /**
     * So, it prepares the data
     */
    private void prepareData() {

        String useToken = PreferenceProvider.getInstance(getApplicationContext()).getUserToken();
        DbUser user = daoProvider.getUserDao().getByToken(useToken);

        if (user == null) {
            return;
        }

        long deviceId = PreferenceProvider.getInstance(getApplicationContext()).getCurrentDeviceId();

        sensorUploadLogs = daoProvider.getSensorUploadLogsDao().getAll();

        if (sensorUploadLogs.isEmpty()) {
            Log.d(TAG, "Not sensor logs to upload");
            return;
        }

        List<SensorUploadLogsRequestDto.Data> allLogs = new ArrayList<>(sensorUploadLogs.size());

        for (LogsSensorUpload entry : sensorUploadLogs) {

            SensorUploadLogsRequestDto.Data log = new SensorUploadLogsRequestDto.Data(
                    entry.getStartTime(),
                    entry.getResponseTime(),
                    entry.getProcessingTime(),
                    entry.getBodySize(),
                    entry.getEventsNumber(),
                    entry.getNetworkType()
            );

            allLogs.add(log);
        }

        SensorUploadLogsRequestDto requestDto = new SensorUploadLogsRequestDto(
                user.getId(),
                deviceId,
                allLogs
        );

        LogsApiProvider logsAPi = ApiProvider.getInstance(this).getLogsApiProvider();
        logsSubscription = logsAPi.uploadSensorLogs(requestDto)
                .subscribe(new LogsUploadSubscriber());
    }

    private class LogsUploadSubscriber extends Subscriber<Void> {

        @Override
        public void onCompleted() {

            // remove transmitted logs
            if (sensorUploadLogs != null && !sensorUploadLogs.isEmpty()) {
                daoProvider.getSensorUploadLogsDao().delete(sensorUploadLogs);
            }

            RxUtils.unsubscribe(logsSubscription);
        }

        @Override
        public void onError(Throwable e) {
            RxUtils.unsubscribe(logsSubscription);
        }

        @Override
        public void onNext(Void aVoid) {
            RxUtils.unsubscribe(logsSubscription);
        }
    }
}