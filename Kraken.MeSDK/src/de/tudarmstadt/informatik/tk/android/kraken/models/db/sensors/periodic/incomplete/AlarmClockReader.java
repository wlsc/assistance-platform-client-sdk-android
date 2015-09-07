package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.periodic.incomplete;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.interfaces.ISensor;

public class AlarmClockReader extends AbstractPeriodicSensor implements ISensor {

    public AlarmClockReader(Context context) {
        super(context);

        // http://stackoverflow.com/questions/11492517/how-can-i-get-the-clock-alarms-in-android
        // geht nicht :(
    }

    @Override
    public ESensorType getSensorType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getDataIntervallInSec() {
        return 3600;
    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
