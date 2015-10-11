package de.tudarmstadt.informatik.tk.android.kraken.model.api.endpoint;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.android.kraken.Config;
import de.tudarmstadt.informatik.tk.android.kraken.model.httpclient.UntrustedOkHttpClient;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Generates Retrofit endpoints
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.06.2015
 */
public class EndpointGenerator {

    private EndpointGenerator() {
    }

    /**
     * Generates retrofit custom API HTTP request endpoints
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T create(Class<T> clazz) {

        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();

        RestAdapter adapter = new RestAdapter.Builder()
//                .setErrorHandler(new AssistanceErrorHandler())
                .setLogLevel(RestAdapter.LogLevel.FULL) // enabling log traces
                .setLog(new AndroidLog(Config.HTTP_LOGGER_NAME))
                .setConverter(new GsonConverter(gsonBuilder.create()))
                .setEndpoint(Config.ASSISTANCE_ENDPOINT)
                .setClient(new OkClient(new UntrustedOkHttpClient().getClient()))
                .build();

        return adapter.create(clazz);
    }
}
