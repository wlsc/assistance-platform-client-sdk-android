package de.tudarmstadt.informatik.tk.assistance.sdk.model.httpclient;

import android.content.Context;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.tudarmstadt.informatik.tk.assistance.sdk.util.AppUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * This is an implementation of OkHttp client which trusts all SSL certificates
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28.06.2015
 */
public class UntrustedOkHttpClient {

    private static final long CACHE_SIZE = 1024L * 1024L * 10L;

    private static final long TIMEOUT = 1024L * 20L;

    private static OkHttpClient okHttpClient;

    /**
     * Gives untrusted OkHttp client back: trust ALL certificates!
     *
     * @param context
     * @return
     */
    public OkHttpClient getClient(Context context) {

        try {

            if (okHttpClient == null) {

                // no validation of certification
                UntrustedX509TrustManager untrustedX509TrustManager = new UntrustedX509TrustManager();
                final TrustManager[] trustAllCerts = {untrustedX509TrustManager};

//                Interceptor headerAuthorizationInterceptor = chain -> {
//            Request request = chain.request();
//            Headers headers = request.headers().newBuilder().add("Authorization", mApiKey).build();
//            request = request.newBuilder().headers(headers).build();
//            return chain.proceed(request);
//        };

                // magic check for debug ON/OFF
                final boolean isDebuggable = AppUtils.isDebug(context);

                HttpLoggingInterceptor.Level httpLogLevel = HttpLoggingInterceptor.Level.NONE;
                // setting to output information for http client
                // in debug mode
                if (isDebuggable) {
                    httpLogLevel = HttpLoggingInterceptor.Level.BODY;
                }

                final OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(headerAuthorizationInterceptor);

                final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(httpLogLevel);

                builder.addInterceptor(httpLoggingInterceptor);

                final File baseDir = context.getCacheDir();

                if (baseDir != null) {

                    final File cacheDir = new File(baseDir, "HttpCache");
                    builder.cache(new Cache(cacheDir, CACHE_SIZE));
                }

                builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
                builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
                builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                builder.sslSocketFactory(sslSocketFactory, untrustedX509TrustManager);
                builder.hostnameVerifier((hostname, session) -> true);

                okHttpClient = builder.build();
            }

            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class UntrustedX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
