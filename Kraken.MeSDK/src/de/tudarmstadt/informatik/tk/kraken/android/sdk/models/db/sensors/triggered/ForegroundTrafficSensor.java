package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.triggered;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.EPushType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.abstract_sensors.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.sensors.AccessibilityEventFilter;

/**
 * This is a triggered sensor which collect the data of apps which are running in the foreground.
 * The sensor collect data if a AccessibilityEvent is happen.
 * Created by Stefan Hacker on 12.11.14.
 */
public class ForegroundTrafficSensor extends AbstractTriggeredSensor {

    private static String EVENT_SCREEN_OFF = "0";
    private static String EVENT_SCREEN_ON = "1";
    public static String EVENT_START_KRAKEN = "2";

    public ForegroundTrafficSensor(Context context) {
        super(context);
    }

    @Override
    public void startSensor() {

    }

    @Override
    public void stopSensor() {

    }

    @Override
    public ESensorType getSensorType() {
        return null;
    }

    @Override
    public void reset() {

    }

    /**
     * Receiver of an intent send by sendBroadcast().
     */
    private class Receiver extends BroadcastReceiver {

        /**
         * do something only if intent action is screen off or on
         *
         * @param context global information about Kraken.Me app in which the reciver is running
         * @param intent  the intent beeing received
         */
        @Override
        public void onReceive(Context context, Intent intent) {
//            SensorNetworkTraffic sensorNetworkTraffic = new SensorNetworkTraffic();
//            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//                sensorNetworkTraffic.setAppName(EVENT_SCREEN_OFF);
//            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//                sensorNetworkTraffic.setAppName(EVENT_SCREEN_ON);
//            }
//            sensorNetworkTraffic.setLatitude(TrafficLocation.getInstance().getLatitude());
//            sensorNetworkTraffic.setLongitude(TrafficLocation.getInstance().getLongitude());
//            sensorNetworkTraffic.setBackground(false);
//            handleDBEntry(sensorNetworkTraffic);
//        }

        }

        private boolean m_bSensorStarted = false;
        private Receiver m_receiver;
        private AccessibilityEventFilter mEventFilter;

        private ActivityManager m_ActivityManager;
        private PackageManager m_PackageManager;

//    private SensorForegroundEvent oldEvent;

        /**
         * Constructor of a new Foreground Traffic Sensor
         *
         * @param context global information about Kraken.Me app
         */
//    public ForegroundTrafficSensor(Context context) {
//        super(context);
//        m_receiver = new Receiver();
//        m_ActivityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
//        m_PackageManager = this.context.getPackageManager();
//        mEventFilter = new AccessibilityEventFilter(this.context);
//    }

        /**
         * start Sensor and trigger if an intent action happen
         */
//    @Override
        public void startSensor() {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            context.registerReceiver(m_receiver, filter);
            m_bSensorStarted = true;
        }

        /**
         * Stop the sensor if kraken service is stopping
         */
//    @Override
        public void stopSensor() {
            if (m_bSensorStarted) {
                try {
                    if (m_receiver != null) {
                        context.unregisterReceiver(m_receiver);
                        m_receiver = null;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                m_bSensorStarted = false;
            }
        }

        /**
         * set own sensor type for push manager
         *
         * @return sensor type of sensor
         */
//    @Override
        public ESensorType getSensorType() {
            return ESensorType.SENSOR_NETWORK_TRAFFIC;
        }

        /**
         * set push type of sensor for push manager
         *
         * @return push type of sensor
         */
//    @Override
        public EPushType getPushType() {
            return EPushType.WLAN_ONLY;
        }


        /**
         * The function is called if a app in foreground starts.
         * @param event include information about the app which will execute in foreground
         */
        //fired at AccessibilityEvent
//    public void onEvent(AccessibilityEvent event) {
//        if (m_bSensorStarted) {
//            SensorForegroundEvent foregroundEvent = mEventFilter.filter(event);
//            if (foregroundEvent != null) {
//                //null at start of sensosr
//                if(oldEvent == null)
//                    oldEvent= foregroundEvent;
//                //old active app different to new, new app will start
//                if(!oldEvent.getPackageName().equals(foregroundEvent.getPackageName())) {
//                    storeData(oldEvent);
//                }
//                storeData(foregroundEvent);
//                oldEvent = foregroundEvent;
//            }
//        }
//    }

        /**
         * will store the traffic information and the actual position in database
         * @param event includes information about the app which was execute in foreground
         */
//    private void storeData(SensorForegroundEvent event){
//        SensorNetworkTraffic sensorNetworkTrafficTraffic = new SensorNetworkTraffic();
//        sensorNetworkTrafficTraffic.setAppName(event.getPackageName());
//        List<ApplicationInfo> packages = m_PackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//        //find app at installed apps to get uid for TrafficStats
//        for (ApplicationInfo packageInfo : packages) {
//            if (packageInfo.packageName.equals(event.getPackageName())) {
//                sensorNetworkTrafficTraffic.setTxBytes(TrafficStats.getUidTxPackets(packageInfo.uid));
//                sensorNetworkTrafficTraffic.setRxBytes(TrafficStats.getUidRxPackets(packageInfo.uid));
//                sensorNetworkTrafficTraffic.setLatitude(TrafficLocation.getInstance().getLatitude());
//                sensorNetworkTrafficTraffic.setLongitude(TrafficLocation.getInstance().getLongitude());
//                sensorNetworkTrafficTraffic.setBackground(false);
//
//                handleDBEntry(sensorNetworkTrafficTraffic);
//                break; //found a match, don't need to search anymore
//            }
//        }
    }
}