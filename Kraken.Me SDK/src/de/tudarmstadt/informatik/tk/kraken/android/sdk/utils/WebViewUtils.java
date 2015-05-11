package de.tudarmstadt.informatik.tk.kraken.android.sdk.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.ApiCache;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.SdkAuthentication;

/**
 * WebViewUtils
 *
 * @author Karsten Planz
 */
public class WebViewUtils {

    public static final String ICONS_DIR = "icons";

    private final Context mContext;
    private final WebView mWebView;
    private final ApiCache mApiCache;
    private final WebViewUtilsCallback mCallback;
    private final String mUrl;
    private Calendar mCurrentDay;

    public interface WebViewUtilsCallback {
        void onError(String message);
    }

    public WebViewUtils(Context context, WebView webView, WebViewUtilsCallback callback, String url) {
        mContext = context;
        mWebView = webView;
        mCallback = callback;
        mUrl = url;
        mApiCache = ApiCache.getInstance(mContext);
    }

    private void setupWebView() {

        if(mCurrentDay == null) {
            mCurrentDay = Calendar.getInstance();
        }

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                initJsApi();
                loadData(mCurrentDay);
            }
        });

        class JsInterface {
            @JavascriptInterface
            public void apiError(String message) {
                message = (message == null)? "" : message;
                Log.d("kraken", "API Error: " + message);
                mCallback.onError(message);
            }
            @JavascriptInterface
            public void cache(String type, String from, String to, String data) {
                mApiCache.put(type, Long.parseLong(from), Long.parseLong(to), data);
            }
        }
        mWebView.addJavascriptInterface(new JsInterface(), "krakenAppInterface");

        mWebView.loadUrl(mUrl);
    }

    class JsonString {

        private String json;

        public JsonString(String json) {
            this.json = json;
        }

        public String toString() {
            return json;
        }
    }

    protected void callJsFunction(String function, Object... args) {
        String argString = "";
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            appendJsArg(sb, args[0]);
            for (int i=1; i<args.length; i++) {
                sb.append(",");
                appendJsArg(sb, args[i]);
            }
            argString = sb.toString();
        }
        mWebView.loadUrl("javascript:" + function + "(" + argString + ")");
    }

    private void appendJsArg(StringBuilder sb, Object arg) {
        if(arg instanceof String) {
            sb.append("'");
            sb.append(arg);
            sb.append("'");
        }
        else if(arg instanceof Integer || arg instanceof Long || arg instanceof Boolean || arg instanceof JsonString) {
            sb.append(arg);
        }
    }

    private void initJsApi() {
        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
        String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);;
        String locale = Locale.getDefault().getLanguage();
        String iconsDir = getIconsDir();
        callJsFunction("KrakenApi.init", kroken, deviceId, locale, iconsDir);
    }

    public String getIconsDir() {
        return "file://" + mContext.getExternalFilesDir(null).getPath()
                + File.separator + ICONS_DIR
                + File.separator;
    }

    private void initAppUsageGraph(long from, long to) {
        if(mWebView == null) {
            return;
        }
        mWebView.loadUrl("javascript:initAppUsageGraph(" + from + "," + to + ")");
    }

    private void initAppUsageGraph(String eventsJson) {
        Log.d("kraken", eventsJson);
        callJsFunction("loadData", new JsonString(eventsJson));
    }

    public void loadData(final Calendar day) {

        Log.d("kraken", "loadData");

        //mCurrentDay = day;

        final long[] startEnd = DateUtils.getDayStartEnd((Calendar) day.clone());

        String cachedForegroundEvents = mApiCache.get("foregroundEvents", startEnd[0], startEnd[1]);

        if (cachedForegroundEvents != null) {
            initAppUsageGraph(cachedForegroundEvents);
        } else {
            initAppUsageGraph(startEnd[0], startEnd[1]);
        }
    }

}
