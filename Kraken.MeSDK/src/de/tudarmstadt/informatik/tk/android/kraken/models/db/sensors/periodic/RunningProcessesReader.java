package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.interfaces.ISensor;


public class RunningProcessesReader extends AbstractPeriodicSensor implements ISensor {

    private ActivityManager m_activityManager;
    // private Query<SensorRunningProcesses> m_query;
    private ArrayList<String> m_liLastProcesses = new ArrayList<String>();

    public RunningProcessesReader(Context context) {
        super(context);
        setDataIntervallInSec(30);
        m_activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
        // m_query =
        // m_daoSession.getSensorRunningProcessesDao().queryBuilder().where(Properties.RunningProcesses.eq("")).build();
    }

    @Override
    protected void dumpData() {

    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.ONE_TIME_SENSOR_RUNNING_PROCESSES;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {

        List<RunningAppProcessInfo> liProcesses = m_activityManager.getRunningAppProcesses();

        ArrayList<String> liProcessNames = new ArrayList<String>(liProcesses.size());
        boolean bProcessesChanged = liProcesses.size() != m_liLastProcesses.size();
        for (RunningAppProcessInfo process : liProcesses) {
            liProcessNames.add(process.processName);
            if (!bProcessesChanged && !m_liLastProcesses.contains(process.processName))
                bProcessesChanged = true;
        }

        if (bProcessesChanged) {
            m_liLastProcesses = liProcessNames;

            long longTimestamp = Calendar.getInstance().getTimeInMillis();
            for (String processName : liProcessNames) {
                // m_query.setParameter(0, processName);
                // List<SensorRunningProcesses> li = m_query.list();
                // if (li == null || li.size() == 0)
                // {
//				SensorRunningProcesses sensor = new SensorRunningProcesses();
//				sensor.setRunningProcesses(processName);
//				sensor.setTimestamp(longTimestamp);
//				handleDBEntry(sensor, false, false, true);
                // }
            }
        }
    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
