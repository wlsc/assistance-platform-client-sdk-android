package de.tudarmstadt.informatik.tk.assistance.sdk.model.api;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.httpclient.UntrustedOkHttpClient;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.AppUtils;
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
public final class ApiGenerator {

    public static final long CACHE_SIZE = 1024l * 1024l * 10l;

    private static ApiGenerator INSTANCE;

    private final Context context;

    private static RestAdapter restAdapter;

    private ApiGenerator(Context context) {
        this.context = context;
    }

    public static ApiGenerator getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ApiGenerator(context);
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

        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);
        okHttpClient.setCache(cache);

        OkClient httpClient = new OkClient(okHttpClient);

        // setup actual endpoint adapter
        RestAdapter adapter = getRestAdapter(
                jsonConverter,
                httpLogLevel,
                logger,
                endpointUrl,
                httpClient);

        return adapter.create(clazz);
    }

    /**
     * Returns normal REST retrofit adapter
     *
     * @param jsonConverter
     * @param httpLogLevel
     * @param logger
     * @param endpointUrl
     * @param httpClient
     * @return
     */
    private RestAdapter getRestAdapter(GsonConverter jsonConverter, RestAdapter.LogLevel httpLogLevel, AndroidLog logger, String endpointUrl, OkClient httpClient) {

        if (restAdapter == null) {
            restAdapter = new RestAdapter.Builder()
//                .setErrorHandler(new AssistanceErrorHandler())
                    .setLogLevel(httpLogLevel) // enabling log traces
                    .setLog(logger)
                    .setConverter(jsonConverter)
                    .setEndpoint(endpointUrl)
                    .setClient(httpClient)
                    .build();
        }

        return restAdapter;
    }
}