package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.periodic;

import android.content.Context;
import android.media.AudioManager;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;

public class RingtoneSensor extends AbstractPeriodicSensor implements ISensor {

	private static final int INIT_DATA_INTERVALL = 60;
	private AudioManager m_audioManager;
	private int m_intLastRingerMode = -1;

	public RingtoneSensor(Context context) {
		super(context);
		setDataIntervallInSec(INIT_DATA_INTERVALL);
		m_audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.SENSOR_RINGTONE;
	}

	public void handleDatabaseObject(int ringMode) {
//		SensorRingtone sensorRingtone = new SensorRingtone();
//		sensorRingtone.setRingtoneMode(ringMode);
//		handleDatabaseObject(sensorRingtone);
	}

	@Override
	protected void getData() {
//		System.out.println("invoked");
//		int intRingerMode = m_audioManager.getRingerMode();
//		if (intRingerMode != m_intLastRingerMode) {
//			SensorRingtone sensorRingtone = new SensorRingtone();
//			sensorRingtone.setRingtoneMode(m_audioManager.getRingerMode());
//			handleDatabaseObject(sensorRingtone);
//		}
	}

}
