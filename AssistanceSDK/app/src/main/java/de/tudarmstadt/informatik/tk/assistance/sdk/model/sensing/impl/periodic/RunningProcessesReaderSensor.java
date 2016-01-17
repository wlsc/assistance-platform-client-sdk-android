package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningProcessesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningProcessesReaderSensor extends AbstractPeriodicSensor {

    private static final String TAG = RunningProcessesReaderSensor.class.getSimpleName();

    private static RunningProcessesReaderSensor INSTANCE;

    private int UPDATE_INTERVAL_IN_SEC = 30;

    private ActivityManager mActivityManager;
    private List<String> mLastProcesses = new ArrayList<>();

    private String currentProcessName = "";

    private RunningProcessesReaderSensor(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static RunningProcessesReaderSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new RunningProcessesReaderSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        DbRunningProcessesSensor runningProcessesEvent = new DbRunningProcessesSensor();

        runningProcessesEvent.setName(currentProcessName);
        runningProcessesEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getRunningProcessesSensorDao().insert(runningProcessesEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return SensorApiType.RUNNING_PROCESSES;
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

    /**
     * Update intervals
     */
    @Override
    public void updateSensorInterval(Double collectionInterval) {

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = collectionInterval.intValue();

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        this.UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
        setDataIntervalInSec(newUpdateIntervalInSec);
    }
}