package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.periodic;

import android.content.Context;
import android.media.AudioManager;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.ISensor;


public class RingtoneEvent extends AbstractPeriodicEvent implements ISensor {

    private static final int INIT_DATA_INTERVALL = 60;
    private AudioManager m_audioManager;
    private int m_intLastRingerMode = -1;

    public RingtoneEvent(Context context) {
        super(context);
        setDataIntervallInSec(INIT_DATA_INTERVALL);
        m_audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void dumpData() {

    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_RINGTONE;
    }

    @Override
    public void reset() {

    }

    public void handleDatabaseObject(int ringMode) {
//		SensorRingtone sensorRingtone = new SensorRingtone();
//		sensorRingtone.setRingtoneMode(ringMode);
//		handleDBEntry(sensorRingtone);
    }

    @Override
    protected void getData() {
//		System.out.println("invoked");
//		int intRingerMode = m_audioManager.getRingerMode();
//		if (intRingerMode != m_intLastRingerMode) {
//			SensorRingtone sensorRingtone = new SensorRingtone();
//			sensorRingtone.setRingtoneMode(m_audioManager.getRingerMode());
//			handleDBEntry(sensorRingtone);
//		}
    }

}
