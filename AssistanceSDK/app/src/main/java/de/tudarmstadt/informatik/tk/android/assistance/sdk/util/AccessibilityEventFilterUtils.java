package de.tudarmstadt.informatik.tk.android.assistance.sdk.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

import static android.content.pm.PackageManager.NameNotFoundException;

public class AccessibilityEventFilterUtils {

    private static final String TAG = AccessibilityEventFilterUtils.class.getSimpleName();

    private static final long ONE_SECOND = 1000;

    private static final long MIN_DURATION_APP_CHANGE = 5 * ONE_SECOND;

    private static final String[] BROWSER_PACKAGES = new String[]{
            "com.android.chrome"
    };

    /**
     * Internal states
     */
    private String lastApp = "";
    private String lastClass = "";
    private long lastTimestamp = 0;
    private int keyStrokes = 0;

    private Context mContext;

    private Pattern mUrlPattern;

    public AccessibilityEventFilterUtils(Context context) {
        this.mContext = context;
    }

    /**
     * Receives an AccessibilityEvent and writes out data depending on the internal states.
     *
     * @param event AccessibilityEvent
     * @return ForegroundEvent
     */
    public DbForegroundEvent filter(AccessibilityEvent event) {

        if (event == null || event.getPackageName() == null) {
            return null;
        }

        String packageName = event.getPackageName().toString();
        String className = event.getClassName() == null ? null : event.getClassName().toString();
        String appName = getAppName(packageName);

        DbForegroundEvent foregroundEvent = new DbForegroundEvent();
        foregroundEvent.setPackageName(packageName);
        foregroundEvent.setAppName(appName);
        foregroundEvent.setClassName(className);

        switch (event.getEventType()) {

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                String activityLabel = getActivityLabel(packageName, className);
                foregroundEvent.setActivityLabel(activityLabel);
                foregroundEvent.setKeystrokes(keyStrokes);

                /** new app in foreground */
                if (!lastApp.equals(packageName)) {

                    if ((System.currentTimeMillis() - lastTimestamp) < MIN_DURATION_APP_CHANGE) {
                        //break;
                    }

                    foregroundEvent.setEventType(ForegroundEvent.EVENT_APP);

                    lWindow("App", appName, packageName, className, activityLabel);

                    lastTimestamp = System.currentTimeMillis();

                    /** reset the counter */
                    keyStrokes = 0;
                }
                /** same app but different activity */
                else if (!lastClass.equals(className)) {

                    foregroundEvent.setEventType(ForegroundEvent.EVENT_ACTIVITY);

                    lWindow("Activity", appName, packageName, className, activityLabel);
                } else {
                    // lMisc(event.getEventType(), appName, packageName, className);
                    break;
                }

                /** update current running app */
                lastApp = packageName;
                lastClass = className;

                foregroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

                return foregroundEvent;

            /*
            No longer needed now we have browser history?
            TODO: remove?
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:

                String text = getEventText(event);

                Log.d("kraken", "is url? " + text + " --> " + isUrl(text));
                //lMisc(event.getEventType(), appName, packageName, className);

                boolean isBrowser = Arrays.asList(BROWSER_PACKAGES).contains(packageName);

                // check if the content is a valid url and save it
                if (isBrowser && isUrl(text)) {

                    foregroundEvent.setEventType(ForegroundEventSensor.EVENT_URL);
                    foregroundEvent.setUrl(text);

                    lUrl(text, appName, packageName, className);

                    return foregroundEvent;
                }

                break;
            */

            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:

                /** increment the counter */
                keyStrokes++;

                break;

            default:

                // lMisc(event.getEventType(), appName, packageName, className);

        }

        return null;
    }

    private void lMisc(int type, String appName, String packageName, String className) {
        l("[%s] %s, %s, %s", type, appName, packageName, className);
    }

    private void lWindow(String event, String appName, String packageName, String className, String activityLabel) {
        l("[%s] %s, %s, %s, %s, %s", event, appName, packageName, className, activityLabel, keyStrokes);
    }

    private void lUrl(String url, String appName, String packageName, String className) {
        l("[URL] %s, %s, %s, %s", url, appName, packageName, className);
    }

    private void l(String format, Object... args) {
        String log = String.format(format, args);
        Log.v(TAG, log);
    }

    public String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    public String getAppName(String packageName) {
        PackageManager pm = mContext.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return ai != null ? pm.getApplicationLabel(ai).toString() : null;
    }

    public String getActivityLabel(String packageName, String className) {
        if (packageName == null || className == null) {
            return null;
        }
        ComponentName cn = new ComponentName(packageName, className);
        PackageManager pm = mContext.getPackageManager();
        ActivityInfo ai = null;
        try {
            ai = pm.getActivityInfo(cn, 0);
        } catch (NameNotFoundException e) {
            //e.printStackTrace();
        }
        return ai != null ? ai.loadLabel(pm).toString() : null;
    }

    public boolean isUrl(String text) {
        Matcher matcher = getUrlPattern().matcher(text);
        return matcher.matches();
    }

    private Pattern getUrlPattern() {
        if (mUrlPattern == null) {
            mUrlPattern = Pattern.compile("(?:(?:http|https):\\/\\/)?([-a-zA-Z0-9.]{2,256}\\.[a-z]{2,4})\\b(?:\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?");
        }
        return mUrlPattern;
    }

}
