package de.tudarmstadt.informatik.tk.kraken.android.sdk.api;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiResponse;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ForegroundEvent;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.MessageType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.SdkAuthentication;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.KrakenUtils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.JacksonConverter;

import static de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiResponse.ArrayApiResponse;

/**
 * RetroApiManager
 *
 * @author Karsten Planz
 */
public class RetroApiManager {

    private static RetroApiManager mInstance;

    private Context mContext;
    private KrakenApi mKrakenApi;

    public RetroApiManager(Context context) {
        mContext = context;

        ExecutorService executor = Executors.newCachedThreadPool();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(KrakenSdkSettings.SERVER_URL)
                        //.setEndpoint("http://localhost")
                .setConverter(new JacksonConverter())
                .setExecutors(executor, executor)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build();
        mKrakenApi = restAdapter.create(KrakenApi.class);
    }

    public static RetroApiManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RetroApiManager(context);
        }
        return mInstance;
    }

    public void postData(List<ApiMessage.DataWrapper> dataWrappers,
                         final Callback<ApiResponse.BundleApiResponse> cb) {
        final ApiMessage bundle = new ApiMessage();
        bundle.type = MessageType.BUNDLE;
        bundle.kroken = SdkAuthentication.getInstance(mContext).getKroken();
        bundle.deviceID = KrakenUtils.getDeviceId(mContext);
        List<ApiMessage> data = new LinkedList<>();
        for (ApiMessage.DataWrapper wrapper : dataWrappers) {
            if (wrapper == null || wrapper.objs == null || wrapper.objs.isEmpty()) {
                continue;
            }
            ApiMessage message = new ApiMessage();
            message.type = MessageType.PERSONAL_DATA;
            message.payload = wrapper;
            data.add(message);
        }
        if (!data.isEmpty()) {
            bundle.data = data;
            mKrakenApi.postData(bundle, cb);
        }
    }

    public List<ForegroundEvent> getForegroundEvents(long startDate, long endDate)
            throws RetrofitError, KrakenError {

        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
        String deviceId = KrakenUtils.getDeviceId(mContext);
        ArrayApiResponse<ForegroundEvent> response = mKrakenApi.getForegroundEvents(startDate,
                endDate, kroken, deviceId);
        if (response == null) {
            throw KrakenError.fromApiError(null);
        }
        if (!response.succ) {
            throw KrakenError.fromApiError(response.error);
        }
        return response.data;
    }

    public static class KrakenError extends RuntimeException {

        public KrakenError(String message) {
            super(message);
        }

        public static KrakenError fromApiError(ApiResponse.ApiError error) {
            if (error == null) {
                return new KrakenError("Unkown Kraken API error");
            }
            StringBuilder sb = new StringBuilder();
            if (error.code != null) {
                sb.append("[");
                sb.append(error.code);
                sb.append("] ");
            }
            if (error.cause != null) {
                sb.append(error.cause);
                if (error.msg != null) {
                    sb.append(": ");
                }
            }
            if (error.msg != null) {
                sb.append(error.msg);
            }
            return new KrakenError(sb.toString());
        }
    }
}
