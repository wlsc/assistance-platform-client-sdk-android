package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.provider.DbProvider;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.ImageUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.sensors.AccessibilityEventFilterUtils;

/**
 * @author Karsten Planz
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class ForegroundEvent extends AbstractTriggeredEvent {

    private static final String TAG = ForegroundEvent.class.getSimpleName();

    public static final int EVENT_APP = 0;
    public static final int EVENT_ACTIVITY = 1;
    public static final int EVENT_SCREEN_ON = 2;
    public static final int EVENT_SCREEN_OFF = 3;
    public static final int EVENT_URL = 4;
    public static final int EVENT_ASSISTANCE_START = 5;
    public static final int EVENT_ASSISTANCE_STOP = 6;

    public final static Integer[] SYSTEM_EVENTS = new Integer[]{
            ForegroundEvent.EVENT_SCREEN_OFF,
            ForegroundEvent.EVENT_ASSISTANCE_STOP
    };

    public static final String ICONS_DIR = "icons";

    private static Map<String, String> mIconColorCache = new HashMap<>();

    private AccessibilityEventFilterUtils mEventFilter;
    private ScreenReceiver mReceiver;
    private static boolean mStarted;

    public ForegroundEvent(Context context) {
        super(context);
    }

    @Override
    public void startSensor() {

        mStarted = true;

        if (mReceiver == null) {
            mReceiver = new ScreenReceiver();
        }

        mEventFilter = new AccessibilityEventFilterUtils(context);

        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(mReceiver, filter);

        DbForegroundEvent dbForegroundEvent = new DbForegroundEvent();

        dbForegroundEvent.setEventType(EVENT_ASSISTANCE_START);
        dbForegroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        dbProvider.insertEventEntry(dbForegroundEvent, getType());
    }

    @Override
    public void stopSensor() {

        if (mStarted) {
            try {
                if (context != null && mReceiver != null) {
                    context.unregisterReceiver(mReceiver);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot unregister receiver", e);
            } finally {

                mReceiver = null;
                mStarted = false;

                if (dbProvider == null) {
                    dbProvider = DbProvider.getInstance(context);
                }

                DbForegroundEvent dbForegroundEvent = new DbForegroundEvent();

                dbForegroundEvent.setEventType(EVENT_ASSISTANCE_STOP);
                dbForegroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

                dbProvider.insertEventEntry(dbForegroundEvent, getType());
            }
        }
    }

    public void onEvent(AccessibilityEvent event) {

        if (dbProvider == null) {
            dbProvider = DbProvider.getInstance(context);
        }

        if (mStarted) {

            DbForegroundEvent foregroundEvent = mEventFilter.filter(event);

            if (foregroundEvent != null) {

                String color = storeIcon(foregroundEvent.getPackageName());
                foregroundEvent.setColor(color);

                dbProvider.insertEventEntry(foregroundEvent, getType());
            } else {
                Log.d(TAG, "Cannot save event: event filter gave NULL back");
            }
        } else {
            Log.d(TAG, "Event received, but sensor was NOT started! Unregistering receiver...");
            try {
                if (context != null && mReceiver != null) {
                    context.unregisterReceiver(mReceiver);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot unregister receiver", e);
            } finally {
                mReceiver = null;
            }
        }
    }

    private String storeIcon(String packageName) {

        Bitmap icon = getAppIcon(packageName);
        String color = getIconColor(packageName, icon);

        File path = new File(context.getExternalFilesDir(null).getPath()
                + File.separator + ICONS_DIR);
        path.mkdirs();
        File file = new File(path, packageName + ".png");

        if (!file.exists()) {
            storeIconFile(file, icon);
        }
        return color;
    }

    private void storeIconFile(File file, Bitmap icon) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (IOException e) {
            Log.d(TAG, "Cannot find file", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.d(TAG, "Cannot close file output stream!", e);
            }
        }
    }

    private Bitmap getAppIcon(String packageName) {

        PackageManager pm = context.getPackageManager();
        try {
            Drawable icon = pm.getApplicationIcon(packageName);
            return ImageUtils.drawableToBitmap(icon);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "Cannot get app icon", e);
            return null;
        }
    }

    private String getIconColor(String packageName, Bitmap icon) {
        if (mIconColorCache.containsKey(packageName)) {
            return mIconColorCache.get(packageName);
        } else {
            String color = ImageUtils.getMainColor(icon);
            mIconColorCache.put(packageName, color);
            return color;
        }
    }

    @Override
    public int getType() {
        return SensorType.FOREGROUND;
    }

    /**
     * Screen register
     */
    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "action: " + intent.getAction());

            DbForegroundEvent foregroundEvent = new DbForegroundEvent();

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                foregroundEvent.setEventType(EVENT_SCREEN_OFF);
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                foregroundEvent.setEventType(EVENT_SCREEN_ON);
            }

            foregroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

            dbProvider.insertEventEntry(foregroundEvent, getType());
        }
    }

    @Override
    public EPushType getPushType() {
        return EPushType.PERIODIC;
    }

    @Override
    protected void dumpData() {

    }

    @Override
    public void reset() {

    }
}
