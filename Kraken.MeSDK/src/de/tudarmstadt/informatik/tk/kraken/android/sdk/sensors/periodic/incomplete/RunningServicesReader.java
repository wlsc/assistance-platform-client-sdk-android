package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.periodic.incomplete;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorRunningServices;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;

public class RunningServicesReader extends AbstractPeriodicSensor implements ISensor {

	private static final int MAXIMUM_SERVICES = 20;
	private ActivityManager m_activityManager;
	@SuppressWarnings("unused")
	private List<String> m_liLastServices = new LinkedList<String>();

	public RunningServicesReader(Context context) {
		super(context);
		m_activityManager = (ActivityManager) m_context.getSystemService(Context.ACTIVITY_SERVICE);

	}

	@Override
	public ESensorType getSensorType() {
		return ESensorType.ONE_TIME_SENSOR_RUNNING_SERVICES;
	}

	@SuppressWarnings("unused")
	@Override
	protected void getData() {

		List<RunningServiceInfo> liServices = m_activityManager.getRunningServices(MAXIMUM_SERVICES);

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

			SensorRunningServices sensor = new SensorRunningServices();
			// sensor.setAccountTypes(sb.toString());
			handleDatabaseObject(sensor);
		}
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
