package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.periodic;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbRunningServicesSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.RunningServicesSensorDao;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ServiceUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class RunningServicesReaderSensor extends AbstractPeriodicSensor {

    private static final String TAG = RunningServicesReaderSensor.class.getSimpleName();

    private static RunningServicesReaderSensor INSTANCE;

    private int UPDATE_INTERVAL_IN_SEC = 30;

    private static final int MAXIMUM_SERVICES = Integer.MAX_VALUE;

    private List<String> lastServicePackageNames = new ArrayList<>();
    private List<String> lastServiceClassNames = new ArrayList<>();

    private RunningServicesReaderSensor(Context context) {
        super(context);

        setDataIntervalInSec(UPDATE_INTERVAL_IN_SEC);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static RunningServicesReaderSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new RunningServicesReaderSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

        Log.d(TAG, "Insert entries");

        long deviceId = PreferenceProvider.getInstance(context).getCurrentDeviceId();
        RunningServicesSensorDao dao = daoProvider.getRunningServicesSensorDao();

        final Date nowDate = new Date();

        for (int i = 0, size = lastServicePackageNames.size(); i < size; i++) {

            DbRunningServicesSensor runningServicesEvent = new DbRunningServicesSensor();

            runningServicesEvent.setPackageName(lastServicePackageNames.get(i));
            runningServicesEvent.setClassName(lastServiceClassNames.get(i));
            runningServicesEvent.setCreated(DateUtils.dateToISO8601String(nowDate, Locale.getDefault()));
            runningServicesEvent.setDeviceId(deviceId);

            dao.insert(runningServicesEvent);
        }

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return SensorApiType.RUNNING_SERVICES;
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