package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningProcessesEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesReaderEvent extends AbstractPeriodicEvent implements ISensor {

    private static final String TAG = RunningProcessesReaderEvent.class.getSimpleName();

    private static final int INIT_DATA_INTERVAL_IN_SEC = 30;

    private ActivityManager mActivityManager;
    private List<String> mLastProcesses = new ArrayList<>();

    private String currentProcessName = "";

    public RunningProcessesReaderEvent(Context context) {
        super(context);

        setDataIntervalInSec(INIT_DATA_INTERVAL_IN_SEC);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void dumpData() {

        DbRunningProcessesEvent runningProcessesEvent = new DbRunningProcessesEvent();

        runningProcessesEvent.setName(currentProcessName);
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
        mLastProcesses.clear();
    }

    @Override
    protected void getData() {

        List<RunningAppProcessInfo> processes = mActivityManager.getRunningAppProcesses();

        List<String> processNames = new ArrayList<>(processes.size());

        boolean processesChanged = processes.size() != mLastProcesses.size();

        for (RunningAppProcessInfo process : processes) {

            processNames.add(process.processName);

            if (!processesChanged && !mLastProcesses.contains(process.processName)) {
                processesChanged = true;
            }
        }

        if (processesChanged) {

            mLastProcesses = processNames;

            for (String processName : processNames) {
                currentProcessName = processName;
                dumpData();
            }
        }
    }
}