package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.incomplete;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;

public class AlarmClockReaderEvent extends AbstractPeriodicEvent implements ISensor {

    public AlarmClockReaderEvent(Context context) {
        super(context);

        // http://stackoverflow.com/questions/11492517/how-can-i-get-the-clock-alarms-in-android
        // geht nicht :(
    }

    @Override
    public void dumpData() {

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
    protected int getDataIntervallInSec() {
        return 3600;
    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
