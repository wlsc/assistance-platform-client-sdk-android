package de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.AbstractTriggeredEvent;


public class MeasurementEvent extends AbstractTriggeredEvent {

    public MeasurementEvent(Context context) {
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
