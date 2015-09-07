package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.triggered;

import android.content.Context;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.abstract_sensors.AbstractTriggeredSensor;


public class MeasurementSensor extends AbstractTriggeredSensor {

    public MeasurementSensor(Context context) {
        super(context);
    }

    public void startSensor() {
//		SensorMeasurementLog measurementLog = new SensorMeasurementLog();
//		measurementLog.setStarted(true);
//		handleDatabaseObject(measurementLog);
//		m_bIsRunning = true;
    }

    public void stopSensor() {
//		SensorMeasurementLog measurementLog = new SensorMeasurementLog();
//		measurementLog.setStarted(false);
//		handleDatabaseObject(measurementLog);
//		m_bIsRunning = false;
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.MEASUREMENT_LOG;
    }
}
