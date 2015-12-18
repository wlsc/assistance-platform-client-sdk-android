package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbRunningServicesEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.ServiceUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class RunningServicesReaderEvent extends AbstractPeriodicEvent {

    private static final String TAG = RunningServicesReaderEvent.class.getSimpleName();

    private static RunningServicesReaderEvent INSTANCE;

    private int UPDATE_INTERVAL_IN_SEC = 30;

    private static final int MAXIMUM_SERVICES = Integer.MAX_VALUE;

    private List<String> lastServicePackageNames = new ArrayList<>();
    private List<String> lastServiceClassNames = new ArrayList<>();

    private RunningServicesReaderEvent(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static RunningServicesReaderEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new RunningServicesReaderEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        Log.d(TAG, "Insert entries");

        final Date nowDate = new Date();

        for (int i = 0, size = lastServicePackageNames.size(); i < size; i++) {

            DbRunningServicesEvent runningServicesEvent = new DbRunningServicesEvent();

            runningServicesEvent.setPackageName(lastServicePackageNames.get(i));
            runningServicesEvent.setClassName(lastServiceClassNames.get(i));
            runningServicesEvent.setCreated(DateUtils.dateToISO8601String(nowDate, Locale.getDefault()));

            daoProvider.getRunningServicesEventDao().insert(runningServicesEvent);
        }

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.RUNNING_SERVICES;
    }

    @Override
    public void reset() {

        lastServicePackageNames.clear();
        lastServiceClassNames.clear();
    }

    @Override
    protected void getData() {

        List<RunningServiceInfo> services = ServiceUtils
                .getRunningServices(context, MAXIMUM_SERVICES);

        List<String> servicesPackageNames = new ArrayList<>(services.size());
        List<String> servicesClassNames = new ArrayList<>(services.size());

        boolean processesChanged = services.size() != lastServicePackageNames.size();

        for (ActivityManager.RunningServiceInfo service : services) {

            servicesPackageNames.add(service.process);
            servicesClassNames.add(service.service.getClassName());

            if (!processesChanged && !lastServicePackageNames.contains(service.process)) {
                processesChanged = true;
            }
        }

        if (processesChanged) {

            lastServicePackageNames.clear();
            lastServicePackageNames.addAll(servicesPackageNames);

            lastServiceClassNames.clear();
            lastServiceClassNames.addAll(servicesClassNames);

            dumpData();
        }
    }

    @Override
    protected int getDataIntervalInSec() {
        return UPDATE_INTERVAL_IN_SEC;
    }

    /**
     * Update intervals
     *
     * @param event
     */
    @Override
    public void onEvent(UpdateSensorIntervalEvent event) {

        // only accept this sensor topic type
        if (event.getTopic() != getType()) {
            return;
        }

        Log.d(TAG, "onUpdate interval");
        Log.d(TAG, "Old update interval: " + UPDATE_INTERVAL_IN_SEC + " sec");

        int newUpdateIntervalInSec = (int) Math.round(1.0 / event.getCollectionFrequency());

        Log.d(TAG, "New update interval: " + newUpdateIntervalInSec + " sec");

        this.UPDATE_INTERVAL_IN_SEC = newUpdateIntervalInSec;
        setDataIntervalInSec(newUpdateIntervalInSec);
    }
}