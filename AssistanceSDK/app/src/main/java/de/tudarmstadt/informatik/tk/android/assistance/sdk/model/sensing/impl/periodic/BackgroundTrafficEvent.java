package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbNetworkTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;

/**
 * This is a Periodic Sensor class which collect the mobile traffic data produced by the apps in the
 * background in periodic intervals. This is important to get the traffic from apps which runs
 * services in background.
 * <p/>
 * Created by Stefan Hacker on 09.12.14
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class BackgroundTrafficEvent extends
        AbstractPeriodicEvent implements
        ISensor {

    private static final String TAG = BackgroundTrafficEvent.class.getSimpleName();

    private static final int INIT_DATA_INTERVAL = 5 * 60;
    private PackageManager packageManager;

    /**
     * Constructor of a new Background Traffic Sensor
     *
     * @param context global information about assistance app
     */
    public BackgroundTrafficEvent(Context context) {
        super(context);

        setDataIntervalInSec(INIT_DATA_INTERVAL);
        packageManager = context.getPackageManager();

        //initial Data
        DbNetworkTrafficEvent networkTrafficEvent = new DbNetworkTrafficEvent();
        networkTrafficEvent.setAppName(ForegroundTrafficEvent.EVENT_START_ASSISTANCE);
        networkTrafficEvent.setBackground(true);

        daoProvider.getNetworkTrafficEventDao().insert(networkTrafficEvent);

        getData();
    }

    /**
     * Called when timer for period is zero and new measurement data will collect the traffic of all
     * installed apps
     */
    @Override
    protected void getData() {

        List<ApplicationInfo> packages = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        if (packages == null) {
            return;
        }

        for (ApplicationInfo packageInfo : packages) {

            if (packageInfo == null) {
                continue;
            }

            insertData(packageInfo);
        }
    }

    /**
     * Insert data of app in database
     *
     * @param packageInfo app which will insert in Database
     */
    private void insertData(ApplicationInfo packageInfo) {

        DbNetworkTrafficEvent networkTrafficEvent = new DbNetworkTrafficEvent();

        if (TrafficStats.getUidTxPackets(packageInfo.uid) != 0 ||
                TrafficStats.getUidRxPackets(packageInfo.uid) != 0) {

            networkTrafficEvent.setAppName(packageInfo.packageName);
            networkTrafficEvent.setTxBytes(TrafficStats.getUidTxPackets(packageInfo.uid));
            networkTrafficEvent.setRxBytes(TrafficStats.getUidRxPackets(packageInfo.uid));
            networkTrafficEvent.setBackground(true);

            Double lastLatitude = PreferenceProvider.getInstance(context).getLastLatitude();
            Double lastLongitude = PreferenceProvider.getInstance(context).getLastLongitude();

            networkTrafficEvent.setLatitude(lastLatitude);
            networkTrafficEvent.setLongitude(lastLongitude);

            Log.d(TAG, "Insert entry");

            daoProvider.getNetworkTrafficEventDao().insert(networkTrafficEvent);

            Log.d(TAG, "Finished");
        }
    }

    /**
     * set own sensor type for push manager
     *
     * @return sensor type of sensor
     */
    @Override
    public int getType() {
        return DtoType.BACKGROUND_TRAFFIC;
    }

    /**
     * set push type of sensor for push manager
     *
     * @return push type of sensor
     */
    @Override
    public EPushType getPushType() {
        return EPushType.WLAN_ONLY;
    }

    @Override
    public void dumpData() {

    }

    @Override
    public void reset() {

    }
}
