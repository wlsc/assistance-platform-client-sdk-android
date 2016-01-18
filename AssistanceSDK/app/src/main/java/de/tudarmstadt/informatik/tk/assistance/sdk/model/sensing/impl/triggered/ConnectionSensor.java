package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbMobileConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbWifiConnectionSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.AbstractTriggeredSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ConnectionUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.StringUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.10.2015
 */
public class ConnectionSensor extends AbstractTriggeredSensor {

    private static final String TAG = ConnectionReceiver.class.getSimpleName();

    private static ConnectionSensor INSTANCE;

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

    private ConnectionSensor(Context context) {
        super(context);

        if (mReceiver == null) {
            mReceiver = new ConnectionReceiver();
        }
    }

    /**
     * Returns singleton of this class
     *
     * @param context
     * @return
     */
    public static ConnectionSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ConnectionSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        String created = DateUtils.dateToISO8601String(new Date(), Locale.getDefault());
        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();

        /**
         * Connection event
         */
        DbConnectionSensor connectionEvent = new DbConnectionSensor();

        connectionEvent.setIsMobile(isMobileDataAvailable);
        connectionEvent.setIsWifi(isWifiDataAvailable);
        connectionEvent.setCreated(created);
        connectionEvent.setDeviceId(deviceId);

        Log.d(TAG, "Insert entry");

        daoProvider.getConnectionSensorDao().insert(connectionEvent);

        Log.d(TAG, "Finished");

        // only insert data when something is available
        if (StringUtils.isNotNullAndEmpty(mobileCarrierName) ||
                StringUtils.isNotNullAndEmpty(mobileCarrierCode) ||
                StringUtils.isNotNullAndEmpty(mobileNetworkCode)) {

            /**
             * Mobile data information
             */
            DbMobileConnectionSensor mobileConnectionEvent = new DbMobileConnectionSensor();

            mobileConnectionEvent.setCarrierName(mobileCarrierName);
            mobileConnectionEvent.setMobileCountryCode(mobileCarrierCode);
            mobileConnectionEvent.setMobileNetworkCode(mobileNetworkCode);
            mobileConnectionEvent.setCreated(created);
            mobileConnectionEvent.setDeviceId(deviceId);

            Log.d(TAG, "MOBILE: Insert entry");

            daoProvider.getMobileConnectionSensorDao().insert(mobileConnectionEvent);

            Log.d(TAG, "MOBILE: Finished");
        }

        if (StringUtils.isNotNullAndEmpty(ssid) ||
                StringUtils.isNotNullAndEmpty(bssid) ||
                frequency != 0 ||
                linkSpeed != 0 ||
                signalStrength != 0 ||
                networkId != 0) {

            /**
             * WIFI data information
             */
            DbWifiConnectionSensor wifiConnectionEvent = new DbWifiConnectionSensor();

            wifiConnectionEvent.setSsid(ssid);
            wifiConnectionEvent.setBssid(bssid);
            wifiConnectionEvent.setChannel(channel);
            wifiConnectionEvent.setFrequency(frequency);
            wifiConnectionEvent.setLinkSpeed(linkSpeed);
            wifiConnectionEvent.setSignalStrength(signalStrength);
            wifiConnectionEvent.setNetworkId(networkId);
            wifiConnectionEvent.setCreated(created);
            wifiConnectionEvent.setDeviceId(deviceId);

            Log.d(TAG, "WIFI: Insert entry");

            daoProvider.getWifiConnectionSensorDao().insert(wifiConnectionEvent);

            Log.d(TAG, "WIFI: Finished");
        }
    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {
        // empty
    }

    @Override
    public void startSensor() {

        if (!isRunning()) {

            try {

                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

                if (context != null && mReceiver != null) {

                    context.registerReceiver(mReceiver, filter);

                    setRunning(true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Some error: ", e);
            }
        }
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
        return SensorApiType.CONNECTION;
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
        channel = 0;
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
                    try {
                        mobileCarrierCode = networkOperator.substring(0, 3);
                        mobileNetworkCode = networkOperator.substring(3);
                    } catch (Exception e) {
                        Log.d(TAG, "Some error.", e);
                        mobileCarrierCode = null;
                        mobileNetworkCode = null;
                    }
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
