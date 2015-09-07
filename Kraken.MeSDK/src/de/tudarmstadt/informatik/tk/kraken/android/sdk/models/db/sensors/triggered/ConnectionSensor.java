package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.triggered;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.abstract_sensors.AbstractTriggeredSensor;

public class ConnectionSensor extends AbstractTriggeredSensor {

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo mobWifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//			SensorConnection sensorConnection = new SensorConnection();
//			if (activeNetInfo != null)
//				sensorConnection.setActiveNetwork(activeNetInfo.getType());
//
//			if (mobNetInfo != null)
//			{
//				sensorConnection.setMobileState(mobNetInfo.getState().ordinal());
//				sensorConnection.setMobileIsAvailable(mobNetInfo.isAvailable());
//			}
//
//			if (mobWifiInfo != null)
//			{
//				sensorConnection.setWlanState(mobWifiInfo.getState().ordinal());
//				sensorConnection.setWlanIsAvailable(mobWifiInfo.isAvailable());
//				//ServerPushManager.getInstance(context).setWlanConnected(mobWifiInfo.getState() == NetworkInfo.State.CONNECTED);
//				RetroServerPushManager.getInstance(context).setWlanConnected(mobWifiInfo.getState() == NetworkInfo.State.CONNECTED);
//			}
//
//			handleDatabaseObject(sensorConnection);
        }

    }

    private boolean m_bSensorStarted = false;
    private Receiver m_receiver;

    public ConnectionSensor(Context context) {
        super(context);
        m_receiver = new Receiver();
    }

    public void startSensor() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(m_receiver, filter);
        m_bSensorStarted = true;
    }

    public void stopSensor() {
        if (m_bSensorStarted) {
            //ServerPushManager.getInstance(context).setWlanConnected(false);
            RetroServerPushManager.getInstance(context).setWlanConnected(false);
            // TODO: find out why this exception is thrown
            try {
                context.unregisterReceiver(m_receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            m_bSensorStarted = false;
        }
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_CONNECTION;
    }
}
