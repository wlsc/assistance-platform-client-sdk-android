package de.tudarmstadt.informatik.tk.android.kraken.api;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.android.kraken.Config;
import de.tudarmstadt.informatik.tk.android.kraken.Settings;
import de.tudarmstadt.informatik.tk.android.kraken.model.httpclient.UntrustedOkHttpClient;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Generates Retrofit services
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.06.2015
 */
public class ServiceGenerator {

    private ServiceGenerator() {
    }

    /**
     * Generates retrofit custom API HTTP request services
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> serviceClass) {

        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();

        RestAdapter adapter = new RestAdapter.Builder()
//                .setErrorHandler(new AssistanceErrorHandler())
                .setLogLevel(RestAdapter.LogLevel.FULL) // enabling log traces
                .setLog(new AndroidLog(Config.HTTP_LOGGER_NAME))
                .setConverter(new GsonConverter(gsonBuilder.create()))
                .setEndpoint(Settings.ASSISTANCE_ENDPOINT)
                .setClient(new OkClient(new UntrustedOkHttpClient().getClient()))
                .build();

        return adapter.create(serviceClass);
    }
}
