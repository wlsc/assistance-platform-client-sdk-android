package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.view.accessibility.AccessibilityEvent;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbForegroundSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbNetworkTrafficSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.NetworkTrafficSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.AccessibilityEventFilterUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * This is a triggered sensor which collect the data of apps which are running in the foreground.
 * The sensor collect data if a AccessibilityEvent is happen.
 * Created by Stefan Hacker on 12.11.14.
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public final class ForegroundTrafficSensor extends AbstractTriggeredSensor {

    static final String TAG = ForegroundTrafficSensor.class.getSimpleName();

    private static ForegroundTrafficSensor INSTANCE;

    private Mode operationMode;

    private int UPDATE_INTERVAL_IN_SEC = 5;

    static String EVENT_SCREEN_OFF = "0";
    static String EVENT_SCREEN_ON = "1";
    public static String EVENT_START_ASSISTANCE = "2";

    private ScreenReceiver mReceiver;
    private AccessibilityEventFilterUtils mEventFilter;

    private ActivityManager mActivityManager;
    private PackageManager mPackageManager;

    private DbForegroundSensor oldEvent;

    private static ScheduledThreadPoolExecutor taskExecutor;

    public enum Mode {
        NORMAL,
        PERIODIC
    }

    /**
     * Constructor of a new Foreground Traffic Sensor
     *
     * @param context
     * @param mode)
     */
    private ForegroundTrafficSensor(Context context, Mode mode) {
        super(context);

        if (context != null) {

            this.operationMode = mode;
            mReceiver = new ScreenReceiver();
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mPackageManager = context.getPackageManager();
            mEventFilter = new AccessibilityEventFilterUtils(context);
        }
    }

    /**
     * Represents normal mode of this sensor
     *
     * @param context
     * @return
     */
    public static ForegroundTrafficSensor getInstance(Context context) {
        return getInstance(context, Mode.NORMAL);
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @param mode
     * @return
     */
    public static ForegroundTrafficSensor getInstance(Context context, Mode mode) {

        if (INSTANCE == null) {
            INSTANCE = new ForegroundTrafficSensor(context, mode);
        }

        return INSTANCE;
    }

    /**
     * start Sensor and trigger if an intent action happen
     */
    @Override
    public void startSensor() {

        Log.d(TAG, "Starting...");

        if (!isRunning()) {

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);

            try {

                if (mReceiver == null) {
                    mReceiver = new ScreenReceiver();
                }

                if (context != null) {

                    context.registerReceiver(mReceiver, filter);

                    setRunning(true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Some error: ", e);
                setRunning(false);
                mReceiver = null;
            }
        }
    }

    /**
     * Stop the sensor if sensing service is stopping
     */
    @Override
    public void stopSensor() {

        Log.d(TAG, "Stopping...");

        if (isRunning()) {
            try {
                if (mReceiver != null && context != null) {
                    context.unregisterReceiver(mReceiver);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot unregister receiver", e);
            } finally {
                setRunning(false);
                mReceiver = null;
                if (taskExecutor != null) {
                    taskExecutor.shutdown();
                    taskExecutor = null;
                }
            }
        }
    }

    @Override
    public void dumpData() {

    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

        if (operationMode == Mode.PERIODIC) {

            Log.d(TAG, "onUpdate interval");
            Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

            int newUpdateIntervalInSec = collectionInterval.intValue();

            Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

            UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
        }
    }

    @Override
    public void reset() {

    }

    /**
     * set own sensor type for push manager
     *
     * @return sensor type of sensor
     */
    @Override
    public int getType() {
        return SensorApiType.FOREGROUND_TRAFFIC;
    }

    /**
     * set push type of sensor for push manager
     *
     * @return push type of sensor
     */
    @Override
    public EPushType getPushType() {
        return EPushType.WLAN_ONLY;
    }

    /**
     * The function is called if a app in foreground starts.
     *
     * @param event include information about the app which will execute in foreground
     */
    //fired at AccessibilityEvent
    public void onEvent(AccessibilityEvent event) {

        Log.d(TAG, "onEvent invoked: " + event.getEventType());

        if (isRunning()) {

            Log.d(TAG, "OK: Sensor is running");

            DbForegroundSensor foregroundEvent = mEventFilter.filter(event);

            if (foregroundEvent != null) {

                /**
                 * Normal mode
                 */
                if (operationMode == Mode.NORMAL) {

                    //  null at start of sensors
                    if (oldEvent == null) {
                        oldEvent = foregroundEvent;
                    }

                    //  old active app different to new, new app will start
                    if (!oldEvent.getPackageName().equals(foregroundEvent.getPackageName())) {

                        storeData(oldEvent);
                    }

                    storeData(foregroundEvent);

                    oldEvent = foregroundEvent;
                }

                /**
                 * Special periodic mode
                 */
                if (operationMode == Mode.PERIODIC) {

                    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                        Log.d(TAG, operationMode.toString() +
                                ": TYPE_WINDOW_STATE_CHANGED event.");

                        // events NOT same
                        if (oldEvent == null || !oldEvent.getPackageName().equals(foregroundEvent.getPackageName())) {

                            Log.d(TAG, operationMode.toString() + ": package name was changed");

                            oldEvent = foregroundEvent;

                            if (taskExecutor != null) {
                                taskExecutor.shutdown();
                            }

                            Log.d(TAG, operationMode.toString() + ": task executor was shutted down");
                            taskExecutor = new ScheduledThreadPoolExecutor(1);
                            taskExecutor.scheduleAtFixedRate(
                                    () -> {
                                        Log.d(TAG, "Called taskExecutor at " + UPDATE_INTERVAL_IN_SEC);
                                        storeData(oldEvent);
                                    },
                                    0L,
                                    UPDATE_INTERVAL_IN_SEC * 1_000,
                                    TimeUnit.MILLISECONDS
                            );

                        } else {
                            Log.d(TAG, operationMode.toString() + ": package names are same!");
                        }

                    } else {
                        Log.d(TAG, operationMode.toString() +
                                ": NOT TYPE_WINDOW_STATE_CHANGED event! Was: " + event.getEventType());
                    }
                }
            }
        } else {
            Log.d(TAG, "FAIL: Sensor is NOT running");
        }
    }

    /**
     * will store the traffic information and the actual position in database
     *
     * @param event includes information about the app which was execute in foreground
     *              <p>
     *              Receiver of an intent send by sendBroadcast().
     */
    private void storeData(DbForegroundSensor event) {

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        NetworkTrafficSensorDao networkTrafficSensorDao = daoProvider.getNetworkTrafficSensorDao();

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        DbNetworkTrafficSensor networkTrafficEvent = new DbNetworkTrafficSensor();

        networkTrafficEvent.setAppName(event.getPackageName());

        List<ApplicationInfo> packages = mPackageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        //find app at installed apps to get uid for TrafficStats
        for (ApplicationInfo packageInfo : packages) {

            if (packageInfo.packageName.equals(event.getPackageName())) {

                networkTrafficEvent.setTxBytes(TrafficStats.getUidTxPackets(packageInfo.uid));
                networkTrafficEvent.setRxBytes(TrafficStats.getUidRxPackets(packageInfo.uid));

                double lastLatitude = PreferenceProvider.getInstance(context).getLastLatitude();
                double lastLongitude = PreferenceProvider.getInstance(context).getLastLongitude();

                if (lastLatitude != 0) {
                    networkTrafficEvent.setLatitude(lastLatitude);
                }

                if (lastLongitude != 0) {
                    networkTrafficEvent.setLongitude(lastLongitude);
                }

                networkTrafficEvent.setBackground(Boolean.FALSE);
                networkTrafficEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
                networkTrafficEvent.setDeviceId(deviceId);

                Log.d(TAG, "Insert entry");

                networkTrafficSensorDao.insert(networkTrafficEvent);

                Log.d(TAG, "Finished");

                //found a match, don't need to search anymore
                break;
            }
        }
    }

    /**
     * Receiver of an intent send by sendBroadcast().
     */
    private class ScreenReceiver extends BroadcastReceiver {

        ScreenReceiver() {
        }

        /**
         * do something only if intent action is screen off or on
         *
         * @param context
         * @param intent  the intent being received
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            if (daoProvider == null) {
                daoProvider = DaoProvider.getInstance(context);
            }

            long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

            DbNetworkTrafficSensor networkTrafficEvent = new DbNetworkTrafficSensor();

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                networkTrafficEvent.setAppName(EVENT_SCREEN_OFF);
            }

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                networkTrafficEvent.setAppName(EVENT_SCREEN_ON);
            }

            double lastLatitude = PreferenceProvider.getInstance(context).getLastLatitude();
            double lastLongitude = PreferenceProvider.getInstance(context).getLastLongitude();

            if (lastLatitude != 0) {
                networkTrafficEvent.setLatitude(lastLatitude);
            }

            if (lastLongitude != 0) {
                networkTrafficEvent.setLongitude(lastLongitude);
            }

            networkTrafficEvent.setBackground(Boolean.FALSE);
            networkTrafficEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));
            networkTrafficEvent.setDeviceId(deviceId);

            Log.d(TAG, "Insert entry");

            daoProvider.getNetworkTrafficSensorDao().insert(networkTrafficEvent);

            Log.d(TAG, "Finished");
        }
    }
}