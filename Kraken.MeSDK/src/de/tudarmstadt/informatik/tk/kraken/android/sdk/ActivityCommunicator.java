package de.tudarmstadt.informatik.tk.kraken.android.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;

public class ActivityCommunicator {

    private static ActivityCommunicator m_instance;
    private static Messenger m_messenger;
    private static Context m_context;

    private ActivityCommunicator() {
        m_instance = this;
    }

    public static ActivityCommunicator getInstance() {
        if (m_instance == null)
            new ActivityCommunicator();
        return m_instance;
    }

    public static void setMessenger(Messenger messenger, Context context) {
        if (m_instance == null)
            new ActivityCommunicator();
        m_messenger = messenger;
        m_context = context;
    }

    public boolean handleData(Bundle data) {
        if (m_messenger == null) {
            // System.out.println("would send, but there's no activity messenger registered");
            return false;
        }

        if (m_context == null) {
            // System.out.println("would send, but there's no context registered");
            return false;
        }

        Bundle dataOut = new Bundle();
        IDbSensor sensor = (IDbSensor) data.getSerializable("sensorData");
        switch ((ESensorType) data.getSerializable("sensorType")) {
            case SENSOR_ACCELEROMETER:
//			SensorAccelerometer sensorAccelerometer = (SensorAccelerometer) sensor;
//			float f = sensorAccelerometer.getAccelerationX() * sensorAccelerometer.getAccelerationY() * sensorAccelerometer.getAccelerationZ();
//			f = ((float) (int) (f * 100)) / 100;
//			dataOut.putString("msg", "Accelerometer: " + f);
                break;
            case SENSOR_ACTIVITY:
                dataOut.putString("msg", "Activity");
                break;
            case SENSOR_CONNECTION:
//			Integer intNetwork = ((SensorConnection) sensor).getActiveNetwork();
//			if (intNetwork == null)
//				return true;
//			String strValue = ((ConnectivityManager) m_context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(intNetwork).getTypeName();
//			dataOut.putString("msg", "Connection: " + strValue);
                break;
            case SENSOR_LIGHT:
//			float value = ((SensorLight) sensor).getValue();
//			dataOut.putString("msg", "Light: " + value);
                break;
            case SENSOR_LOCATION:
//			SensorLocation sensorLocation = ((SensorLocation) sensor);
//			strValue = "acc: " + sensorLocation.getAccuracy();
//			strValue += ", long: " + sensorLocation.getLongitude();
//			strValue += ", lat: " + sensorLocation.getLatitude();
//			dataOut.putString("msg", "Location: " + strValue);
                break;
            case SENSOR_RINGTONE:
//			strValue = "unknown";
//			switch (((SensorRingtone) sensor).getRingtoneMode()) {
//			case (AudioManager.RINGER_MODE_NORMAL):
//				strValue = "normal";
//				break;
//			case (AudioManager.RINGER_MODE_SILENT):
//				strValue = "silent";
//				break;
//			case (AudioManager.RINGER_MODE_VIBRATE):
//				strValue = "vibrate";
//				break;
//			}
//			dataOut.putString("msg", "Ringtone: " + strValue);
                break;
            case SENSOR_NETWORK_TRAFFIC:
//                SensorNetworkTraffic sensorNetworkTraffic = ((SensorNetworkTraffic) sensor);
//                strValue = "appname: " + sensorNetworkTraffic.getAppName();
//                strValue += ", recived: " + sensorNetworkTraffic.getRxBytes();
//                strValue += ", send: " + sensorNetworkTraffic.getTxBytes();
//                strValue += ", is Background Traffic: " + sensorNetworkTraffic.getBackground();
//                dataOut.putString("msg","Foreground Network Traffic: " + strValue);
                break;
            case SENSOR_BACKGROUND_TRAFFIC:
//                SensorNetworkTraffic sensorNetworkBackTraffic = ((SensorNetworkTraffic) sensor);
//                strValue = "appname: " + sensorNetworkBackTraffic.getAppName();
//                strValue += ", recived: " + sensorNetworkBackTraffic.getRxBytes();
//                strValue += ", send: " + sensorNetworkBackTraffic.getTxBytes();
//                strValue += ", is Background Traffic: " + sensorNetworkBackTraffic.getBackground();
//                dataOut.putString("msg","Background Network Traffic: " + strValue);
                break;
            default:
                dataOut.putString("msg", "Value from unknown sensor.");
                return false;
        }
        sendToHandler(dataOut);
        return true;
    }

    private void sendToHandler(Bundle data) {
        if (m_messenger != null) {
            Message msg = new Message();
            msg.setData(data);
            try {
                m_messenger.send(msg);
            } catch (RemoteException e) {
                m_messenger = null;
                e.printStackTrace();
            }
        }
    }
}
