package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.interfaces.ISensor;


public class RunningTasksReader extends AbstractPeriodicSensor implements ISensor {

    private static final int MAXIMUM_TASKS = 10;
    private ActivityManager m_activityManager;
    private ArrayList<String> m_liLastTasks = new ArrayList<String>();

    public RunningTasksReader(Context context) {
        super(context);
        setDataIntervallInSec(30);
        m_activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.ONE_TIME_SENSOR_RUNNING_TASKS;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {

        List<RunningTaskInfo> liTasks = m_activityManager.getRunningTasks(MAXIMUM_TASKS);

        ArrayList<String> liTasksNames = new ArrayList<String>(liTasks.size());
        boolean bTasksChanged = liTasks.size() != m_liLastTasks.size();
        int i = 0;
        for (RunningTaskInfo task : liTasks) {
            String strCurrentClassName = task.baseActivity.getClassName();
            liTasksNames.add(strCurrentClassName);
            if (!bTasksChanged) {
                String strLastTask = m_liLastTasks.get(i);
                if (strLastTask == null || !strCurrentClassName.equals(strLastTask))
                    bTasksChanged = true;
            }
            i++;
        }

        if (bTasksChanged) {
            m_liLastTasks = liTasksNames;

//			long longTimestamp = Calendar.getInstance().getTimeInMillis();
//			i = 0;
//			for (String taskName : liTasksNames) {
//				SensorRunningTasks sensor = new SensorRunningTasks();
//				sensor.setRunningTasks(taskName);
//				sensor.setStackPosition(i);
//				sensor.setTimestamp(longTimestamp);
//				handleDBEntry(sensor, false, false, true);
//				i++;
//			}
        }

    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
