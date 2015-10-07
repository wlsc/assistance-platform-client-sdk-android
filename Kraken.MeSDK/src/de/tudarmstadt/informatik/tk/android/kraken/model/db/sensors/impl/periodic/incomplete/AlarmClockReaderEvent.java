package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.impl.periodic.incomplete;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ISensor;

public class AlarmClockReaderEvent extends AbstractPeriodicSensor implements ISensor {

    public AlarmClockReaderEvent(Context context) {
        super(context);

        // http://stackoverflow.com/questions/11492517/how-can-i-get-the-clock-alarms-in-android
        // geht nicht :(
    }

    @Override
    protected void dumpData() {

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
