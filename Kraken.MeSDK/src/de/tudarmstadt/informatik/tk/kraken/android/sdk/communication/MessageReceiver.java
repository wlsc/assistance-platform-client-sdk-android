package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.interfaces.ISensor;


public class MessageReceiver extends BroadcastReceiver {

    public static void forwardToSensor(Context context, JSONArray json) {
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject jsonSensor = json.optJSONObject(i);
                if (jsonSensor != null) {
                    String strType = jsonSensor.getString("type");
                    ESensorType type = ESensorType.valueOf(strType);
                    ISensor sensor = SensorManager.getInstance(context).getSensor(type);

                    if (jsonSensor.has("data"))
                        sensor.configure(jsonSensor.getJSONObject("data"));

                    if (jsonSensor.has("control")) {
                        if ("start".equals(jsonSensor.getString("control")))
                            sensor.startSensor();
                        else if ("stop".equals(jsonSensor.getString("control")))
                            sensor.stopSensor();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

    }

}
