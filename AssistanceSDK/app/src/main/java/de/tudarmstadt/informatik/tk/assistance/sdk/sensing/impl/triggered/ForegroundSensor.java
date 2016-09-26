package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.view.accessibility.AccessibilityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.AccessibilityEventFilterUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ImageUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Karsten Planz
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public final class ForegroundSensor extends AbstractTriggeredSensor {

    private static final String TAG = ForegroundSensor.class.getSimpleName();

    private static ForegroundSensor INSTANCE;

    public static final int EVENT_APP = 0;
    public static final int EVENT_ACTIVITY = 1;
    public static final int EVENT_SCREEN_ON = 2;
    public static final int EVENT_SCREEN_OFF = 3;
    public static final int EVENT_URL = 4;
    public static final int EVENT_ASSISTANCE_START = 5;
    public static final int EVENT_ASSISTANCE_STOP = 6;

    public static final Integer[] SYSTEM_EVENTS = {
            EVENT_SCREEN_OFF,
            EVENT_ASSISTANCE_STOP
    };

    public static final String ICONS_DIR = "icons";

    private static Map<String, String> mIconColorCache = new HashMap<>();

    private AccessibilityEventFilterUtils mEventFilter;
    private ScreenReceiver mReceiver;

    private ForegroundSensor(Context context) {
        super(context);
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static ForegroundSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void startSensor() {

        try {

            setRunning(true);

            if (mReceiver == null) {
                mReceiver = new ScreenReceiver();
            }

            mEventFilter = new AccessibilityEventFilterUtils(context);

            // register receiver that handles screen on and screen off logic
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);

            if (context != null) {
                context.registerReceiver(mReceiver, filter);
            }

        } catch (Exception e) {
            Log.e(TAG, "Some exception happened: " + e);
            setRunning(false);
        } finally {

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbForegroundSensor dbForegroundEvent = new DbForegroundSensor();

            dbForegroundEvent.setEventType(EVENT_ASSISTANCE_START);
            dbForegroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
            dbForegroundEvent.setDeviceId(deviceId);

            Log.d(TAG, "Insert entry");

            daoProvider.getForegroundSensorDao().insert(dbForegroundEvent);

            Log.d(TAG, "Finished");
        }
    }

    @Override
    public void stopSensor() {

        try {
            if (context != null && mReceiver != null) {
                context.unregisterReceiver(mReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Cannot unregister receiver", e);
        } finally {

            mReceiver = null;
            setRunning(false);

            if (daoProvider == null) {
                daoProvider = DaoProvider.getInstance(context);
            }

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbForegroundSensor dbForegroundEvent = new DbForegroundSensor();

            dbForegroundEvent.setEventType(EVENT_ASSISTANCE_STOP);
            dbForegroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
            dbForegroundEvent.setDeviceId(deviceId);

            Log.d(TAG, "Insert entry");

            daoProvider.getForegroundSensorDao().insert(dbForegroundEvent);

            Log.d(TAG, "Finished");
        }
    }

    public void onEvent(AccessibilityEvent event) {

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        if (isRunning()) {

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbForegroundSensor foregroundEvent = mEventFilter.filter(event);

            if (foregroundEvent != null) {

                String color = storeIcon(foregroundEvent.getPackageName());

                if (color != null) {
                    foregroundEvent.setColor(color);
                }

                foregroundEvent.setDeviceId(deviceId);

                Log.d(TAG, "Insert entry");

                daoProvider.getForegroundSensorDao().insert(foregroundEvent);

                Log.d(TAG, "Finished");

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

        try {

            File externalFilesDir = context.getExternalFilesDir(null);

            File path = null;

            if (externalFilesDir != null) {
                path = new File(externalFilesDir.getPath()
                        + File.separator + ICONS_DIR);
            }

            if (path != null) {

                path.mkdirs();

                File file = new File(path, packageName + ".png");

                if (!file.exists()) {
                    storeIconFile(file, icon);
                }
            }

        } catch (NullPointerException npe) {
            Log.e(TAG, "NPE in storeIcon");
        } catch (Exception e) {
            Log.e(TAG, "Some error: " + e);
        }

        return color;
    }

    private void storeIconFile(File file, Bitmap icon) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            icon.compress(CompressFormat.PNG, 100, stream);
        } catch (IOException e) {
            Log.e(TAG, "Cannot find file", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Cannot close file output stream!", e);
            }
        }
    }

    private Bitmap getAppIcon(String packageName) {

        PackageManager pm = context.getPackageManager();
        try {
            Drawable icon = pm.getApplicationIcon(packageName);
            return ImageUtils.drawableToBitmap(icon);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Cannot get app icon", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Some error: ", e);
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
        return SensorApiType.FOREGROUND;
    }

    /**
     * Screen register
     */
    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "action: " + intent.getAction());

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbForegroundSensor foregroundEvent = new DbForegroundSensor();

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                foregroundEvent.setEventType(EVENT_SCREEN_OFF);
            } else {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    foregroundEvent.setEventType(EVENT_SCREEN_ON);
                }
            }

            foregroundEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
            foregroundEvent.setDeviceId(deviceId);

            Log.d(TAG, "Insert entry");

            daoProvider.getForegroundSensorDao().insert(foregroundEvent);

            Log.d(TAG, "Finished");
        }
    }

    @Override
    public EPushType getPushType() {
        return EPushType.PERIODIC;
    }

    @Override
    public void dumpData() {
    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {
        // empty
    }

    @Override
    public void reset() {

    }
}
