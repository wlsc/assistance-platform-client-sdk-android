package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesReaderEvent extends AbstractPeriodicEvent implements ISensor {

    private static final String TAG = RunningProcessesReaderEvent.class.getSimpleName();

    private ActivityManager mActivityManager;
    // private Query<SensorRunningProcesses> m_query;
    private List<String> mLastProcesses = new ArrayList<>();

    private String currentProcessName;

    public RunningProcessesReaderEvent(Context context) {
        super(context);

        setDataIntervalInSec(30);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void dumpData() {

        DbRunningProcessesEvent runningProcessesEvent = new DbRunningProcessesEvent();

        runningProcessesEvent.setRunningProcesses(currentProcessName);
        runningProcessesEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getRunningProcessesEventDao().insert(runningProcessesEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.RUNNING_PROCESSES;
    }

    @Override
    public void reset() {

        currentProcessName = "";
    }

    @Override
    protected void getData() {

        List<RunningAppProcessInfo> liProcesses = mActivityManager.getRunningAppProcesses();

        List<String> processNames = new ArrayList<>(liProcesses.size());
        boolean bProcessesChanged = liProcesses.size() != mLastProcesses.size();

        for (RunningAppProcessInfo process : liProcesses) {
            processNames.add(process.processName);
            if (!bProcessesChanged && !mLastProcesses.contains(process.processName))
                bProcessesChanged = true;
        }

        if (bProcessesChanged) {

            mLastProcesses = processNames;

            for (String processName : processNames) {
                // m_query.setParameter(0, processName);
                // List<SensorRunningProcesses> li = m_query.list();
                // if (li == null || li.size() == 0)
                // {
                currentProcessName = processName;
                dumpData();
                // }
            }
        }
    }
}
