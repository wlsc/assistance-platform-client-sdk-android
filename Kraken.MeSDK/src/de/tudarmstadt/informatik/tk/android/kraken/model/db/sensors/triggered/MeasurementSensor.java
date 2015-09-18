package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors.AbstractTriggeredSensor;


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
//		isRunning = true;
    }

    public void stopSensor() {
//		SensorMeasurementLog measurementLog = new SensorMeasurementLog();
//		measurementLog.setStarted(false);
//		handleDBEntry(measurementLog);
//		isRunning = false;
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.MEASUREMENT_LOG;
    }

    @Override
    public void reset() {

    }
}
