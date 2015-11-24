package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningTasksEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class RunningTasksReaderEvent extends
        AbstractPeriodicEvent implements
        ISensor {

    private static final String TAG = RunningTasksReaderEvent.class.getSimpleName();

    private static final int MAXIMUM_TASKS = 10;
    private ActivityManager mActivityManager;
    private List<String> mLastTasks = new ArrayList<>();

    private String currentTaskName;
    private int currentStackPosition;

    public RunningTasksReaderEvent(Context context) {
        super(context);

        setDataIntervalInSec(30);
        mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public void dumpData() {

        DbRunningTasksEvent runningTasksEvent = new DbRunningTasksEvent();

        runningTasksEvent.setRunningTasks(currentTaskName);
        runningTasksEvent.setStackPosition(currentStackPosition);
        runningTasksEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getRunningTasksEventDao().insert(runningTasksEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.RUNNING_TASKS;
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
}
