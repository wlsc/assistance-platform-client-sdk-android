package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningTasksReaderSensor extends AbstractPeriodicSensor {

    private static final String TAG = RunningTasksReaderSensor.class.getSimpleName();

    private static RunningTasksReaderSensor INSTANCE;

    private static final int MAXIMUM_TASKS = 10;

    public int UPDATE_INTERVAL_IN_SEC = 30;

    private ActivityManager mActivityManager;
    private List<String> mLastTasks = new ArrayList<>();

    private String currentTaskName;
    private int currentStackPosition;

    private RunningTasksReaderSensor(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);

        mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static RunningTasksReaderSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new RunningTasksReaderSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        DbRunningTasksSensor runningTasksEvent = new DbRunningTasksSensor();

        runningTasksEvent.setName(currentTaskName);
        runningTasksEvent.setStackPosition(currentStackPosition);
        runningTasksEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getRunningTasksSensorDao().insert(runningTasksEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return SensorApiType.RUNNING_TASKS;
    }

    @Override
    public void reset() {

        currentTaskName = "";
        currentStackPosition = -1;
    }

    @Override
    protected void getData() {

        try {
            List<RunningTaskInfo> tasks = mActivityManager.getRunningTasks(MAXIMUM_TASKS);

            List<String> tasksNames = new ArrayList<>(tasks.size());
            boolean isTasksChanged = tasks.size() != mLastTasks.size();
            int i = 0;

            for (RunningTaskInfo task : tasks) {

                if (task == null) {
                    continue;
                }

                String strCurrentClassName = task.baseActivity.getClassName();
                tasksNames.add(strCurrentClassName);

                if (!isTasksChanged) {

                    String strLastTask = mLastTasks.get(i);
                    if (strLastTask == null || !strCurrentClassName.equals(strLastTask))
                        isTasksChanged = true;
                }

                i++;
            }

            if (isTasksChanged) {
                mLastTasks = tasksNames;

                i = 0;

                for (String taskName : tasksNames) {

                    currentTaskName = taskName;
                    currentStackPosition = i;

                    dumpData();

                    i++;
                }
            }
        } catch (SecurityException se) {
            Log.e(TAG, "No access to task permission!", se);
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
