package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.triggered;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractTriggeredSensor;


public class MeasurementSensor extends AbstractTriggeredSensor {

    public MeasurementSensor(Context context) {
        super(context);
    }

    @Override
    protected void dumpData() {

    }

    public void startSensor() {
//		SensorMeasurementLog measurementLog = new SensorMeasurementLog();
//		measurementLog.setStarted(true);
//		handleDBEntry(measurementLog);
//		m_bIsRunning = true;
    }

    public void stopSensor() {
//		SensorMeasurementLog measurementLog = new SensorMeasurementLog();
//		measurementLog.setStarted(false);
//		handleDBEntry(measurementLog);
//		m_bIsRunning = false;
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.MEASUREMENT_LOG;
    }

    @Override
    public void reset() {

    }
}
