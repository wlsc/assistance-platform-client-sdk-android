package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.triggered;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorLight;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractTriggeredSensor;

public class LightSensor extends AbstractTriggeredSensor implements SensorEventListener {

	public static enum Configuration {
		SENSOR_DELAY_BETWEEN_TWO_EVENTS, MIN_DIFFERENCE;
	};

	// ------------------- Configuration -------------------
	private int SENSOR_DELAY_BETWEEN_TWO_EVENTS = SensorManager.SENSOR_DELAY_NORMAL;
	private int MIN_DIFFERENCE = 5;
	// -----------------------------------------------------

	private SensorManager m_sensorManager;
	private Sensor m_accelerometerSensor;
	private float m_floatLastValue;

	public LightSensor(Context context) {
		super(context);

		m_sensorManager = (SensorManager) m_context.getSystemService(Context.SENSOR_SERVICE);
		m_accelerometerSensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	@Override
	public void startSensor() {
		m_sensorManager.registerListener(this, m_accelerometerSensor, SENSOR_DELAY_BETWEEN_TWO_EVENTS);
		m_bIsRunning = true;
	}

	@Override
	public void stopSensor() {
		m_sensorManager.unregisterListener(this, m_accelerometerSensor);
		m_bIsRunning = false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.SENSOR_LIGHT;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float floatValue = event.values[0];
		if (floatValue < m_floatLastValue - MIN_DIFFERENCE || floatValue > m_floatLastValue + MIN_DIFFERENCE) {
			m_floatLastValue = floatValue;
			SensorLight sensorLight = new SensorLight();
			sensorLight.setAccuracy(event.accuracy);
			sensorLight.setValue(floatValue);
			handleDatabaseObject(sensorLight);
		}
	}
}
