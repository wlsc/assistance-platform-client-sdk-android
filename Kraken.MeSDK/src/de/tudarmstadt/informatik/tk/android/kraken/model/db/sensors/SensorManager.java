package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.contentobserver.BrowserHistorySensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.contentobserver.CalendarSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.contentobserver.CallLogSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.contentobserver.ContactsSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.periodic.BackgroundTrafficSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.periodic.RingtoneSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.ActivitySensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.ForegroundEventSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.ForegroundTrafficSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.MeasurementSensor;


public class SensorManager {

    private HashMap<ESensorType, ISensor> m_mapSensors = new HashMap<ESensorType, ISensor>();
    private List<ISensor> m_liSensors = new LinkedList<ISensor>();
    private static SensorManager m_sensorManager;

    public static SensorManager getInstance(Context ctx) {
        if (m_sensorManager == null)
            m_sensorManager = new SensorManager(ctx);
        else
            m_sensorManager.setContext(ctx);

        return m_sensorManager;
    }

    private SensorManager(Context context) {

        // // triggered
        // works
        AccelerometerSensor accelerometerSensor = new AccelerometerSensor(context);
        m_liSensors.add(accelerometerSensor);
        RetroServerPushManager.getInstance(context).setPushType(accelerometerSensor, accelerometerSensor.getPushType());

        ActivitySensor activitySensor = new ActivitySensor(context);
        m_liSensors.add(activitySensor);

        RetroServerPushManager.getInstance(context).setPushType(activitySensor, activitySensor.getPushType());

        // works
        LightSensor lightSensor = new LightSensor(context);
        m_liSensors.add(lightSensor);
        //ServerPushManager.getInstance(context).setPushType(lightSensor, lightSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(lightSensor, lightSensor.getPushType());

        // works
        LocationSensor locationSensor = new LocationSensor(context);
        m_liSensors.add(locationSensor);
        //ServerPushManager.getInstance(context).setPushType(locationSensor, locationSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(locationSensor, locationSensor.getPushType());

        MeasurementSensor measurementSensor = new MeasurementSensor(context);
        m_liSensors.add(measurementSensor);
        //ServerPushManager.getInstance(context).setPushType(measurementSensor, measurementSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(measurementSensor, measurementSensor.getPushType());

        // works
        ConnectionSensor connectionSensor = new ConnectionSensor(context);
        m_liSensors.add(connectionSensor);
        //ServerPushManager.getInstance(context).setPushType(connectionSensor, connectionSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(connectionSensor, connectionSensor.getPushType());

        //new foreground traffic
        ForegroundTrafficSensor foregroundTrafficSensor = new ForegroundTrafficSensor(context);
        m_liSensors.add(foregroundTrafficSensor);
        RetroServerPushManager.getInstance(context).setPushType(foregroundTrafficSensor, foregroundTrafficSensor.getPushType());

        //new periodic background traffic
        BackgroundTrafficSensor backgroundTrafficSensor = new BackgroundTrafficSensor(context);
        m_liSensors.add(backgroundTrafficSensor);
        RetroServerPushManager.getInstance(context).setPushType(backgroundTrafficSensor, backgroundTrafficSensor.getPushType());

        // loudness sensor is blocking microphone and consuming too much battery
        // LoudnessSensor loudnessSensor = new LoudnessSensor(context);
        // m_liSensors.add(loudnessSensor);
        // ServerPushManager.getInstance(context).setPushType(loudnessSensor, loudnessSensor.getPushType());

        // periodic
        RingtoneSensor ringtoneSensor = new RingtoneSensor(context);
        m_liSensors.add(ringtoneSensor);
        //ServerPushManager.getInstance(context).setPushType(ringtoneSensor, ringtoneSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(ringtoneSensor, ringtoneSensor.getPushType());

        CalendarSensor calendarSensor = new CalendarSensor(context);
        m_liSensors.add(calendarSensor);
        //ServerPushManager.getInstance(context).setPushType(calendarSensor, calendarSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(calendarSensor, calendarSensor.getPushType());

        ContactsSensor contactsSensor = new ContactsSensor(context);
        m_liSensors.add(contactsSensor);
        //ServerPushManager.getInstance(context).setPushType(contactsSensor, contactsSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(contactsSensor, contactsSensor.getPushType());

        CallLogSensor callLogSensor = new CallLogSensor(context);
        m_liSensors.add(callLogSensor);
        RetroServerPushManager.getInstance(context).setPushType(callLogSensor, callLogSensor.getPushType());
        //ServerPushManager.getInstance(context).setPushType(callLogSensor, callLogSensor.getPushType());

        BrowserHistorySensor browserHistorySensor = new BrowserHistorySensor(context);
        m_liSensors.add(browserHistorySensor);
        //ServerPushManager.getInstance(context).setPushType(browserHistorySensor, browserHistorySensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(browserHistorySensor, browserHistorySensor.getPushType());

        ForegroundEventSensor foregroundEventSensor = new ForegroundEventSensor(context);
        m_liSensors.add(foregroundEventSensor);
        //ServerPushManager.getInstance(context).setPushType(foregroundEventSensor, foregroundEventSensor.getPushType());
        RetroServerPushManager.getInstance(context).setPushType(foregroundEventSensor, foregroundEventSensor.getPushType());

        for (ISensor sensor : m_liSensors)
            m_mapSensors.put(sensor.getSensorType(), sensor);
    }

    public List<ISensor> getSensors() {
        return m_liSensors;
    }

    public ISensor getSensor(ESensorType type) {
        return m_mapSensors.get(type);
    }

    public List<ISensor> getEnabledSensors() {

        List<ISensor> liSensors = new LinkedList<ISensor>();

        for (ISensor sensor : m_liSensors)
            if (!sensor.isDisabledBySystem() && !sensor.isDisabledByUser())
                liSensors.add(sensor);

        return liSensors;
    }

    public void setContext(Context ctx) {
        for (ISensor sensor : m_liSensors)
            sensor.setContext(ctx);
    }

    public void setDaoSession(DaoSession daoSession) {
        for (ISensor sensor : m_liSensors)
            sensor.setDaoSession(daoSession);
    }

}
