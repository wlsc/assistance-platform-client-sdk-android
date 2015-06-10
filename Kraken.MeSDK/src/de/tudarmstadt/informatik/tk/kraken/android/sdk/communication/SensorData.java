package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import org.json.JSONObject;

import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;

public class SensorData {

	private JSONObject m_jsonData;
	private String m_strFullQualifiedBeanClassName;
	private ISensor m_sensorObject;
	private List<? extends IDbSensor> m_liSensorEntities;
	
	public SensorData() {
	}

	public JSONObject getJsonData() {
		return m_jsonData;
	}

	public void setJsonData(JSONObject jsonData) {
		this.m_jsonData = jsonData;
	}

	public String getFullQualifiedBeanClassName() {
		return m_strFullQualifiedBeanClassName;
	}

	public void setFullQualifiedBeanClassName(String strFullQualifiedBeanClassName) {
		m_strFullQualifiedBeanClassName = strFullQualifiedBeanClassName;
	}

	public ISensor getSensor() {
		return m_sensorObject;
	}

	public void setSensor(ISensor sensor) {
		this.m_sensorObject = sensor;
	}

	public void setSensorEntities(List<? extends IDbSensor> liSensorEntities) {
		m_liSensorEntities = liSensorEntities;
	}

	public List<? extends IDbSensor> getSensorEntities() {
		return m_liSensorEntities;
	}

}
