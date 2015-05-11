package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

public class ServerPushManagerResponseHandler implements IServerCommunicationResponseHandler {

	private List<SensorData> m_liSensorMapping;
	private Context m_ctx;

	public ServerPushManagerResponseHandler(List<SensorData> liSensorMapping, Context ctx) {
		this.m_liSensorMapping = liSensorMapping;
		this.m_ctx = ctx;
	}

	@Override
	public void handleData(Bundle data) {
		if (data.containsKey("error") && data.getBoolean("error") == false) {
			for (SensorData sensorData : m_liSensorMapping)
			{
				sensorData.getSensor().removeDataFromDb(sensorData.getSensorEntities(), sensorData.getFullQualifiedBeanClassName());
			}
		} else {
			ServerPushManager.getInstance(m_ctx).addToCache(m_liSensorMapping);
		}
	}

}
