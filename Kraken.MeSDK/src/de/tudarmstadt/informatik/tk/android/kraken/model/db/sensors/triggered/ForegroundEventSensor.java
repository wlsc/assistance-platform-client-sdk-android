package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered;

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
import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.informatik.tk.android.kraken.communication.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.android.kraken.utils.ImageUtils;
import de.tudarmstadt.informatik.tk.android.kraken.utils.sensors.AccessibilityEventFilter;

/**
 * @author Karsten Planz
 */
public class ForegroundEventSensor extends AbstractTriggeredSensor {

    public static final int EVENT_APP = 0;
    public static final int EVENT_ACTIVITY = 1;
    public static final int EVENT_SCREEN_ON = 2;
    public static final int EVENT_SCREEN_OFF = 3;
    public static final int EVENT_URL = 4;
    public static final int EVENT_KRAKEN_START = 5;
    public static final int EVENT_KRAKEN_STOP = 6;

    public final static Integer[] SYSTEM_EVENTS = new Integer[]{
            ForegroundEventSensor.EVENT_SCREEN_OFF,
            ForegroundEventSensor.EVENT_KRAKEN_STOP
    };

    public static final String ICONS_DIR = "icons";

    private static Map<String, String> mIconColorCache = new HashMap<>();

    private AccessibilityEventFilter mEventFilter;
    private ScreenReceiver mReceiver = null;
    private boolean mStarted = false;

    public ForegroundEventSensor(Context context) {
        super(context);
        mReceiver = new ScreenReceiver();
    }

    @Override
    public void startSensor() {

        mStarted = true;

        mEventFilter = new AccessibilityEventFilter(context);

        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(mReceiver, filter);

//        SensorForegroundEvent event = new SensorForegroundEvent();
//        event.setEventType(EVENT_KRAKEN_START);
//        event.setTimestamp(System.currentTimeMillis());
//        insertEvent(event);
    }

    @Override
    public void stopSensor() {

        if (mStarted) {
            // TODO: find out why this exception is thrown
            try {
                if (mReceiver != null) {
                    context.unregisterReceiver(mReceiver);
                    mReceiver = null;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            mStarted = false;

//            SensorForegroundEvent event = new SensorForegroundEvent();
//            event.setEventType(EVENT_KRAKEN_STOP);
//            event.setTimestamp(System.currentTimeMillis());
//            insertEvent(event);
        }
    }

    public void onEvent(AccessibilityEvent event) {
//        if(mStarted) {
//            SensorForegroundEvent foregroundEvent = mEventFilter.filter(event);
//            if (foregroundEvent != null) {
//                String color = storeIcon(foregroundEvent.getPackageName());
//                foregroundEvent.setColor(color);
//                insertEvent(foregroundEvent);
//            }
//        }
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
            e.printStackTrace();
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getAppIcon(String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            Drawable icon = pm.getApplicationIcon(packageName);
            return ImageUtils.drawableToBitmap(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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

//    private void insertEvent(SensorForegroundEvent foregroundEvent) {
//        handleDBEntry(foregroundEvent);
//    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_FOREGROUND_EVENT;
    }


    private class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("kraken", "action = " + intent.getAction());

//            SensorForegroundEvent foregroundEvent = new SensorForegroundEvent();
//            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                foregroundEvent.setEventType(EVENT_SCREEN_OFF);
//            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                foregroundEvent.setEventType(EVENT_SCREEN_ON);
//            }
//            insertEvent(foregroundEvent);
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
