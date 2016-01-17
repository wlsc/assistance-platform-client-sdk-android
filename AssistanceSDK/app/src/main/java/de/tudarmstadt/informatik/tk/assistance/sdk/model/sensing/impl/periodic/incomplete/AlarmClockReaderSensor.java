package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic.incomplete;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.ISensor;

public class AlarmClockReaderSensor extends AbstractPeriodicSensor implements ISensor {

    public AlarmClockReaderSensor(Context context) {
        super(context);

        // http://stackoverflow.com/questions/11492517/how-can-i-get-the-clock-alarms-in-android
        // geht nicht :(
    }

    @Override
    public void dumpData() {

    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }

    @Override
    public int getType() {
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int getDataIntervalInSec() {
        return 3600;
    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
