package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.endpoint;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.httpclient.UntrustedOkHttpClient;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.AppUtils;
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

    private static EndpointGenerator INSTANCE;

    private final Context context;

    private EndpointGenerator(Context context) {
        this.context = context;
    }

    public static EndpointGenerator getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new EndpointGenerator(context);
        }

        return INSTANCE;
    }

    /**
     * Generates retrofit custom API HTTP request endpoints
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz) {

        // JSON parser
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        GsonConverter jsonConverter = new GsonConverter(gsonBuilder.create());

        // magic check for debug ON/OFF
        boolean isDebuggable = AppUtils.isDebug(context);

        RestAdapter.LogLevel httpLogLevel = RestAdapter.LogLevel.NONE;
        // setting to output information for http client
        // in debug mode
        if (isDebuggable) {
            httpLogLevel = RestAdapter.LogLevel.FULL;
        }

        AndroidLog logger = new AndroidLog(Config.HTTP_LOGGER_NAME);

        // check for custom endpoint url
        String endpointUrl = PreferenceProvider
                .getInstance(context)
                .getCustomEndpointUrl();

        // custom endpoint settings is empty
        if (endpointUrl.isEmpty()) {
            endpointUrl = Config.ASSISTANCE_ENDPOINT;
        }

        // HTTP client setup
        OkHttpClient okHttpClient = new UntrustedOkHttpClient().getClient();

        Cache cache = new Cache(context.getCacheDir(), 1024l * 1024l * 10l);
        okHttpClient.setCache(cache);
        
        OkClient httpClient = new OkClient(okHttpClient);

        // setup actual endpoint adapter
        RestAdapter adapter = new RestAdapter.Builder()
//                .setErrorHandler(new AssistanceErrorHandler())
                .setLogLevel(httpLogLevel) // enabling log traces
                .setLog(logger)
                .setConverter(jsonConverter)
                .setEndpoint(endpointUrl)
                .setClient(httpClient)
                .build();

        return adapter.create(clazz);
    }
}