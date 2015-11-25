package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.AccessibilityEventFilterUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * This is a triggered sensor which collect the data of apps which are running in the foreground.
 * The sensor collect data if a AccessibilityEvent is happen.
 * Created by Stefan Hacker on 12.11.14.
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 27.10.2015
 */
public class ForegroundTrafficEvent extends AbstractTriggeredEvent {

    private static final String TAG = ForegroundTrafficEvent.class.getSimpleName();

    private static String EVENT_SCREEN_OFF = "0";
    private static String EVENT_SCREEN_ON = "1";
    public static String EVENT_START_ASSISTANCE = "2";

    private ScreenReceiver mReceiver;
    private AccessibilityEventFilterUtils mEventFilter;

    private ActivityManager mActivityManager;
    private PackageManager mPackageManager;

    private DbForegroundEvent oldEvent;

    /**
     * Constructor of a new Foreground Traffic Sensor
     *
     * @param context
     */
    public ForegroundTrafficEvent(Context context) {
        super(context);

        if (context != null) {

            mReceiver = new ScreenReceiver();
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mPackageManager = context.getPackageManager();
            mEventFilter = new AccessibilityEventFilterUtils(context);
        }
    }

    /**
     * start Sensor and trigger if an intent action happen
     */
    @Override
    public void startSensor() {

        if (!isRunning()) {

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);

            try {

                if (context != null && mReceiver != null) {

                    context.registerReceiver(mReceiver, filter);

                    setRunning(true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Some error: ", e);
                setRunning(false);
            }
        }
    }

    /**
     * Stop the sensor if sensing service is stopping
     */
    @Override
    public void stopSensor() {

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
            }
        }
    }

    @Override
    public void dumpData() {

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
        return DtoType.NETWORK_TRAFFIC;
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

        if (isRunning()) {

            DbForegroundEvent foregroundEvent = mEventFilter.filter(event);

            if (foregroundEvent != null) {

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
        }
    }

    /**
     * will store the traffic information and the actual position in database
     *
     * @param event includes information about the app which was execute in foreground
     *              <p/>
     *              Receiver of an intent send by sendBroadcast().
     */
    private void storeData(DbForegroundEvent event) {

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(context);
        }

        DbNetworkTrafficEvent networkTrafficEvent = new DbNetworkTrafficEvent();

        networkTrafficEvent.setAppName(event.getPackageName());

        List<ApplicationInfo> packages = mPackageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        //find app at installed apps to get uid for TrafficStats
        for (ApplicationInfo packageInfo : packages) {

            if (packageInfo.packageName.equals(event.getPackageName())) {

                Double lastLatitude = PreferenceProvider.getInstance(context).getLastLatitude();
                Double lastLongitude = PreferenceProvider.getInstance(context).getLastLongitude();

                networkTrafficEvent.setTxBytes(TrafficStats.getUidTxPackets(packageInfo.uid));
                networkTrafficEvent.setRxBytes(TrafficStats.getUidRxPackets(packageInfo.uid));

                if (lastLatitude != 0) {
                    networkTrafficEvent.setLatitude(lastLatitude);
                }

                if (lastLongitude != 0) {
                    networkTrafficEvent.setLongitude(lastLongitude);
                }

                networkTrafficEvent.setBackground(false);
                networkTrafficEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

                Log.d(TAG, "Insert entry");

                daoProvider.getNetworkTrafficEventDao().insert(networkTrafficEvent);

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

            DbNetworkTrafficEvent networkTrafficEvent = new DbNetworkTrafficEvent();

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                networkTrafficEvent.setAppName(EVENT_SCREEN_OFF);
            }

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                networkTrafficEvent.setAppName(EVENT_SCREEN_ON);
            }

            Double lastLatitude = PreferenceProvider.getInstance(context).getLastLatitude();
            Double lastLongitude = PreferenceProvider.getInstance(context).getLastLongitude();

            if (lastLatitude != 0) {
                networkTrafficEvent.setLatitude(lastLatitude);
            }

            if (lastLongitude != 0) {
                networkTrafficEvent.setLongitude(lastLongitude);
            }

            networkTrafficEvent.setBackground(false);
            networkTrafficEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

            Log.d(TAG, "Insert entry");

            daoProvider.getNetworkTrafficEventDao().insert(networkTrafficEvent);

            Log.d(TAG, "Finished");
        }
    }
}