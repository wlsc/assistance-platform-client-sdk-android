package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.incomplete;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;


public class RunningAppsReaderEvent extends AbstractPeriodicEvent implements ISensor {

    private static final int MAXIMUM_SERVICES = 20;
    private ActivityManager m_activityManager;

    private List<String> m_liLastServices = new LinkedList<>();

    public RunningAppsReaderEvent(Context context) {
        super(context);
        m_activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);

    }

    @Override
    public void dumpData() {

    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }

    @Override
    public int getType() {
        return DtoType.RUNNING_SERVICES;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {

        List<RunningServiceInfo> liServices = m_activityManager.getRunningServices(MAXIMUM_SERVICES);
        List<RunningAppProcessInfo> runningAppProcesses = m_activityManager.getRunningAppProcesses();
        // liTasks.get(0).

        // Account[] accounts = m_activityManager.getAccounts();
        // String[] strAccountTypes = new String[accounts.length];
        //
        // for (int i = 0; i < accounts.length; i++) {
        // strAccountTypes[i] = accounts[i].type;
        // }

        if (liServices.size() > 0) {
            // TODO: implement logic
            StringBuilder sb = new StringBuilder();
            // for (int i = 0; i < liProcesses.size() - 1; i++)
            // sb.append(liProcesses.get.type + ";");
            // sb.append(liProcesses[liProcesses.length - 1]);

//			SensorRunningServices sensor = new SensorRunningServices();
//			// sensor.setAccountTypes(sb.toString());
//			handleDBEntry(sensor);
        }
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
