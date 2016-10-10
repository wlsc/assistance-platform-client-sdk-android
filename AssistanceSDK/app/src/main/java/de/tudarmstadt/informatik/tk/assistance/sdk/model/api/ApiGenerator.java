package de.tudarmstadt.informatik.tk.assistance.sdk.model.api;

import android.content.Context;

import com.google.gson.GsonBuilder;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.httpclient.UntrustedOkHttpClient;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Generates Retrofit endpoints
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.06.2015
 */
public final class ApiGenerator {

    private static ApiGenerator INSTANCE;

    private final Context context;

    private Retrofit retrofit;

    private ApiGenerator(Context context) {
        this.context = context.getApplicationContext();
    }

    public static ApiGenerator getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ApiGenerator(context);
        }

        return INSTANCE;
    }

    /**
     * Returns retrofit adapter
     *
     * @return
     */
    public Retrofit getRetrofit() {

        if (retrofit == null) {

            // JSON parser
            final GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
            final GsonConverterFactory jsonConverterFactory = GsonConverterFactory.create(gsonBuilder.create());

            // check for custom endpoint url
            String endpointUrl = PreferenceProvider
                    .getInstance(context)
                    .getCustomEndpointUrl();

            // custom endpoint settings is empty
            if (endpointUrl.isEmpty()) {
                endpointUrl = Config.ASSISTANCE_ENDPOINT;
            }

            // HTTP client setup
            final OkHttpClient okHttpClient = new UntrustedOkHttpClient().getClient(context);

            retrofit = new Retrofit.Builder()
                    .baseUrl(endpointUrl)
                    .client(okHttpClient)
                    .addConverterFactory(jsonConverterFactory)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

    /**
     * Generates retrofit custom API HTTP request endpoints
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }
}