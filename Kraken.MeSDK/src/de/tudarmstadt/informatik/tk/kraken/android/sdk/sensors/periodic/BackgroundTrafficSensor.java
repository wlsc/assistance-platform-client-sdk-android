package de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.periodic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.EPushType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.SensorNetworkTraffic;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.triggered.ForegroundTrafficSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.TrafficLocation;

/**
 * This is a Periodic Sensor class which collect the mobile traffic data produced by the apps in the
 * background in periodic intervals. This is important to get the traffic from apps which runs
 * services in background.
 *
 * Created by Stefan Hacker on 09.12.14.
 */
public class BackgroundTrafficSensor extends AbstractPeriodicSensor implements ISensor {

    private static final int INIT_DATA_INTERVAL = 5 * 60;
    private PackageManager m_PackageManager;

    /**
     * Constructor of a new Background Traffic Sensor
     *
     * @param context global information about Kraken.Me app
     */
    public BackgroundTrafficSensor(Context context) {
        super(context);
        setDataIntervallInSec(INIT_DATA_INTERVAL);
        m_PackageManager = this.context.getPackageManager();
        //initial Data
        SensorNetworkTraffic sensorNetworkTraffic = new SensorNetworkTraffic();
        sensorNetworkTraffic.setAppName(ForegroundTrafficSensor.EVENT_START_KRAKEN);
        handleDatabaseObject(sensorNetworkTraffic);
        getData();
    }

    /**
     * Called when timer for period is zero and new measurement data will collect the traffic of all
     * installed apps
     */
    @Override
    protected void getData() {
        List<ApplicationInfo> packages = m_PackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            insertData(packageInfo);
        }
    }

    /**
     * Insert data of app in database
     * @param packageInfo app which will insert in Database
     */
    private void insertData(ApplicationInfo packageInfo){
        SensorNetworkTraffic sensorNetworkTraffic = new SensorNetworkTraffic();
        if(TrafficStats.getUidTxPackets(packageInfo.uid) != 0 || TrafficStats.getUidRxPackets(packageInfo.uid) != 0) {
            sensorNetworkTraffic.setAppName(packageInfo.packageName);
            sensorNetworkTraffic.setTxBytes(TrafficStats.getUidTxPackets(packageInfo.uid));
            sensorNetworkTraffic.setRxBytes(TrafficStats.getUidRxPackets(packageInfo.uid));
            sensorNetworkTraffic.setBackground(true);
            sensorNetworkTraffic.setLatitude(TrafficLocation.getInstance().getLatitude());
            sensorNetworkTraffic.setLongitude(TrafficLocation.getInstance().getLongitude());
            handleDatabaseObject(sensorNetworkTraffic);
        }
    }

    /**
     * set own sensor type for push manager
     * @return sensor type of sensor
     */
    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_BACKGROUND_TRAFFIC;
    }

    /**
     * set push type of sensor for push manager
     * @return push type of sensor
     */
    @Override
    public EPushType getPushType() {
        return EPushType.WLAN_ONLY;
    }
}
