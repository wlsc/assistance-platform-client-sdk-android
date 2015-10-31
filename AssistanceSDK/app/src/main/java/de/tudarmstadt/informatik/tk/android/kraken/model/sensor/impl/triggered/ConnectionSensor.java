package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbMobileConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbWifiConnectionEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;
import de.tudarmstadt.informatik.tk.android.kraken.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.android.kraken.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.10.2015
 */
public class ConnectionSensor extends AbstractTriggeredEvent {

    private static final String TAG = ConnectionReceiver.class.getSimpleName();

    private static ConnectionReceiver mReceiver;

    private boolean isMobileDataAvailable;
    private boolean isWifiDataAvailable;

    /**
     * WIFI information
     */
    private String ssid;
    private String bssid;
    private int channel;
    private int frequency;
    private int linkSpeed;
    private int signalStrength;
    private int networkId;

    /**
     * MOBILE information
     */
    private String mobileCarrierName;
    private String mobileCarrierCode;
    private String mobileNetworkCode;

    public ConnectionSensor(Context context) {
        super(context);

        if (mReceiver == null) {
            mReceiver = new ConnectionReceiver();
        }
    }

    @Override
    public void dumpData() {

        String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());

        /**
         * Connection event
         */
        DbConnectionEvent connectionEvent = new DbConnectionEvent();

        connectionEvent.setIsMobile(isMobileDataAvailable);
        connectionEvent.setIsWifi(isWifiDataAvailable);
        connectionEvent.setCreated(created);

        dbProvider.getConnectionEventDao().insert(connectionEvent);

        /**
         * Mobile data information
         */
        DbMobileConnectionEvent mobileConnectionEvent = new DbMobileConnectionEvent();

        mobileConnectionEvent.setCarrierName(mobileCarrierName);
        mobileConnectionEvent.setMobileCarrierCode(mobileCarrierCode);
        mobileConnectionEvent.setMobileNetworkCode(mobileNetworkCode);
        mobileConnectionEvent.setCreated(created);

        dbProvider.getMobileConnectionEventDao().insert(mobileConnectionEvent);

        /**
         * WIFI data information
         */
        DbWifiConnectionEvent wifiConnectionEvent = new DbWifiConnectionEvent();

        wifiConnectionEvent.setSsid(ssid);
        wifiConnectionEvent.setBssid(bssid);
        wifiConnectionEvent.setChannel(channel);
        wifiConnectionEvent.setFrequency(frequency);
        wifiConnectionEvent.setLinkSpeed(linkSpeed);
        wifiConnectionEvent.setSignalStrength(signalStrength);
        wifiConnectionEvent.setNetworkId(networkId);
        wifiConnectionEvent.setCreated(created);

        dbProvider.getWifiConnectionEventDao().insert(wifiConnectionEvent);
    }

    @Override
    public void startSensor() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        context.registerReceiver(mReceiver, filter);

        setRunning(true);
    }

    @Override
    public void stopSensor() {

        if (isRunning()) {

            try {

                if (context != null && mReceiver != null) {
                    context.unregisterReceiver(mReceiver);
                }

            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot unregister receiver!", e);
            } finally {
                setRunning(false);
                mReceiver = null;
            }
        }
    }

    @Override
    public int getType() {
        return DtoType.CONNECTION;
    }

    @Override
    public void reset() {

        isMobileDataAvailable = false;
        isWifiDataAvailable = false;

        mobileCarrierName = null;
        mobileCarrierCode = null;
        mobileNetworkCode = null;

        ssid = null;
        bssid = null;
        channel = -0;
        frequency = 0;
        linkSpeed = 0;
        signalStrength = 0;
        networkId = 0;
    }

    /**
     * Connection event receiver
     */
    private class ConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Received connection event");

            /**
             * MOBILE CONNECTION
             */
            if (ConnectionUtils.isConnectedMobile(context)) {

                isMobileDataAvailable = true;

                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                mobileCarrierName = manager.getNetworkOperatorName();

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
            if (ConnectionUtils.isConnectedWifi(context)) {

                isWifiDataAvailable = true;

                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();

                ssid = info.getSSID();
                bssid = info.getBSSID();

                // TODO: find a way to get a channel
                channel = -1;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    frequency = info.getFrequency();
                }

                linkSpeed = info.getLinkSpeed();
                signalStrength = info.getRssi();
                networkId = info.getNetworkId();

            } else {
                isWifiDataAvailable = false;
            }

            dumpData();
        }
    }
}
