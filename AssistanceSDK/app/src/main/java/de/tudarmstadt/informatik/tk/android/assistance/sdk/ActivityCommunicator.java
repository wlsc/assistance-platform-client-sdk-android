package de.tudarmstadt.informatik.tk.android.assistance.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.ESensorType;

public class ActivityCommunicator {

    private static final String TAG = ActivityCommunicator.class.getSimpleName();

    private static ActivityCommunicator instance;
    private static Messenger mMessenger;
    private static Context mContext;

    private ActivityCommunicator() {
    }

    public static ActivityCommunicator getInstance() {

        if (instance == null) {
            Log.d(TAG, TAG + " was created");
            instance = new ActivityCommunicator();
        }

        return instance;
    }

    public static void setMessenger(Messenger messenger, Context context) {

        Log.d(TAG, "New messenger was set");

        if (instance == null) {
            instance = new ActivityCommunicator();
        }

        mMessenger = messenger;
        mContext = context;
    }

    public boolean handleData(Bundle data) {

        if (mMessenger == null) {
            // System.out.println("would send, but there's no activity messenger registered");
            return false;
        }

        if (mContext == null) {
            // System.out.println("would send, but there's no context registered");
            return false;
        }

        Bundle dataOut = new Bundle();
        IDbSensor sensor = (IDbSensor) data.getSerializable("sensorData");

        switch ((ESensorType) data.getSerializable("sensorType")) {

            case ACCELEROMETER_SENSOR:
                Log.d(TAG, "Processing Accelerometer sensor data...");
                DbAccelerometerSensor accelerometerSensor = (DbAccelerometerSensor) sensor;
                double result = accelerometerSensor.getX() * accelerometerSensor.getY() * accelerometerSensor.getZ();
                result = ((double) (int) (result * 100)) / 100;
                dataOut.putString("msg", "Accelerometer: " + result);
                break;
            case MOTION_ACTIVITY_EVENT:
                dataOut.putString("msg", "Activity");
                break;
            case CONNECTION_EVENT:
//			Integer intNetwork = ((SensorConnection) sensor).getActiveNetwork();
//			if (intNetwork == null)
//				return true;
//			String strValue = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(intNetwork).getTypeName();
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
                Log.e(TAG, "Unknown sensor data found!");
                dataOut.putString("msg", "Value from unknown sensor.");
                return false;
        }
        sendToHandler(dataOut);
        return true;
    }

    private void sendToHandler(Bundle data) {

        if (mMessenger != null) {

            Message msg = new Message();
            msg.setData(data);

            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                mMessenger = null;
                Log.e(TAG, "Cannot send sensor data their handler. Error", e);
            }
        }
    }
}
