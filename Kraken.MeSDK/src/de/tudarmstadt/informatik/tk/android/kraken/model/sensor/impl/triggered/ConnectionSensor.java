package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.enums.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;

public class ConnectionSensor extends AbstractTriggeredEvent {

    private static final String TAG = ConnectionReceiver.class.getSimpleName();

    private boolean isMobileDataAvailable;
    private boolean isWifiDataAvailable;

    /**
     * WIFI
     */
    private String ssid;
    private String bssid;
    private Integer channel;
    private Integer frequency;
    private Integer linkSpeed;
    private Integer signalStrength;
    private Integer networkId;

    /**
     * MOBILE
     */
    private String carrierName;
    private String mobileCarrierCode;
    private String mobileNetworkCode;

    private class ConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Received connection event");

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo mobWifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//            if (activeNetInfo != null) {
//                connectionEvent.setActiveNetwork(activeNetInfo.getType());
//            }

            /**
             * MOBILE CONNECTION
             */
            if (mobNetInfo != null && mobNetInfo.isConnected()) {

                isMobileDataAvailable = true;

                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                carrierName = manager.getNetworkOperatorName();

                String networkOperator = manager.getNetworkOperator();

                if (networkOperator != null) {
                    mobileCarrierCode = networkOperator.substring(0, 3);
                    mobileNetworkCode = networkOperator.substring(3);
                }


            } else {
                isMobileDataAvailable = false;
            }

            /**
             * WIFI CONNECTION
             */
            if (mobWifiInfo != null && mobWifiInfo.isConnected()) {

                isWifiDataAvailable = true;

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();

                ssid = info.getSSID();

            } else {
                isWifiDataAvailable = false;
            }

            dumpData();
//			handleDBEntry(connectionEvent);
        }

    }

    private boolean m_bSensorStarted = false;
    private ConnectionReceiver connectionReceiver;

    public ConnectionSensor(Context context) {
        super(context);
        connectionReceiver = new ConnectionReceiver();
    }

    @Override
    protected void dumpData() {

        Log.d(TAG, "Dumping connection event data...");

        DbConnectionEvent connectionEvent = new DbConnectionEvent();

        connectionEvent.setIsMobile(isMobileDataAvailable);
        connectionEvent.setIsWifi(isWifiDataAvailable);
        connectionEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));


        Log.d(TAG, "Finished.");
    }

    @Override
    public void startSensor() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(connectionReceiver, filter);
        m_bSensorStarted = true;
    }

    @Override
    public void stopSensor() {

        if (m_bSensorStarted) {
//            RetroServerPushManager.getInstance(context).setWLANConnected(false);
            // TODO: find out why this exception is thrown
            try {
                context.unregisterReceiver(connectionReceiver);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot unregister receiver!", e);
            }
            m_bSensorStarted = false;
        }
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.CONNECTION_EVENT;
    }

    @Override
    public void reset() {
        isMobileDataAvailable = false;
        isWifiDataAvailable = false;
    }
}
