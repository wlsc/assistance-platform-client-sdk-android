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

    private HashMap<ESensorType, ISensor> m_mapSensors = new HashMap<>();
    private List<ISensor> m_liSensors = new LinkedList<>();

    private static SensorManager INSTANCE;

    public static SensorManager getInstance(Context ctx) {

        if (INSTANCE == null) {
            INSTANCE = new SensorManager(ctx);
        } else {
            INSTANCE.setContext(ctx);
        }

        return INSTANCE;
    }

    private SensorManager(Context context) {

        final RetroServerPushManager retroServerPushManager = RetroServerPushManager.getInstance(context);

        // // triggered
        // works
        AccelerometerSensor accelerometerSensor = new AccelerometerSensor(context);
        m_liSensors.add(accelerometerSensor);
        retroServerPushManager.setPushType(accelerometerSensor, accelerometerSensor.getPushType());

        ActivitySensor activitySensor = new ActivitySensor(context);
        m_liSensors.add(activitySensor);
        retroServerPushManager.setPushType(activitySensor, activitySensor.getPushType());

        // works
        LightSensor lightSensor = new LightSensor(context);
        m_liSensors.add(lightSensor);
        retroServerPushManager.setPushType(lightSensor, lightSensor.getPushType());

        // works
        LocationSensor locationSensor = new LocationSensor(context);
        m_liSensors.add(locationSensor);
        retroServerPushManager.setPushType(locationSensor, locationSensor.getPushType());

        MeasurementSensor measurementSensor = new MeasurementSensor(context);
        m_liSensors.add(measurementSensor);
        retroServerPushManager.setPushType(measurementSensor, measurementSensor.getPushType());

        // works
        ConnectionSensor connectionSensor = new ConnectionSensor(context);
        m_liSensors.add(connectionSensor);
        retroServerPushManager.setPushType(connectionSensor, connectionSensor.getPushType());

        //new foreground traffic
        ForegroundTrafficSensor foregroundTrafficSensor = new ForegroundTrafficSensor(context);
        m_liSensors.add(foregroundTrafficSensor);
        retroServerPushManager.setPushType(foregroundTrafficSensor, foregroundTrafficSensor.getPushType());

        //new periodic background traffic
        BackgroundTrafficSensor backgroundTrafficSensor = new BackgroundTrafficSensor(context);
        m_liSensors.add(backgroundTrafficSensor);
        retroServerPushManager.setPushType(backgroundTrafficSensor, backgroundTrafficSensor.getPushType());

        // loudness sensor is blocking microphone and consuming too much battery
        // LoudnessSensor loudnessSensor = new LoudnessSensor(context);
        // m_liSensors.add(loudnessSensor);
        // ServerPushManager.getInstance(context).setPushType(loudnessSensor, loudnessSensor.getPushType());

        // periodic
        RingtoneSensor ringtoneSensor = new RingtoneSensor(context);
        m_liSensors.add(ringtoneSensor);
        retroServerPushManager.setPushType(ringtoneSensor, ringtoneSensor.getPushType());

        CalendarSensor calendarSensor = new CalendarSensor(context);
        m_liSensors.add(calendarSensor);
        retroServerPushManager.setPushType(calendarSensor, calendarSensor.getPushType());

        ContactsSensor contactsSensor = new ContactsSensor(context);
        m_liSensors.add(contactsSensor);
        retroServerPushManager.setPushType(contactsSensor, contactsSensor.getPushType());

        CallLogSensor callLogSensor = new CallLogSensor(context);
        m_liSensors.add(callLogSensor);
        retroServerPushManager.setPushType(callLogSensor, callLogSensor.getPushType());

        BrowserHistorySensor browserHistorySensor = new BrowserHistorySensor(context);
        m_liSensors.add(browserHistorySensor);
        retroServerPushManager.setPushType(browserHistorySensor, browserHistorySensor.getPushType());

        ForegroundEventSensor foregroundEventSensor = new ForegroundEventSensor(context);
        m_liSensors.add(foregroundEventSensor);
        retroServerPushManager.setPushType(foregroundEventSensor, foregroundEventSensor.getPushType());

        for (ISensor sensor : m_liSensors) {
            m_mapSensors.put(sensor.getSensorType(), sensor);
        }
    }

    public List<ISensor> getSensors() {
        return m_liSensors;
    }

    public ISensor getSensor(ESensorType type) {
        return m_mapSensors.get(type);
    }

    public List<ISensor> getEnabledSensors() {

        List<ISensor> liSensors = new LinkedList<ISensor>();

        for (ISensor sensor : m_liSensors) {
            if (!sensor.isDisabledBySystem() && !sensor.isDisabledByUser()) {
                liSensors.add(sensor);
            }
        }

        return liSensors;
    }

    public void setContext(Context ctx) {
        for (ISensor sensor : m_liSensors) {
            sensor.setContext(ctx);
        }
    }

    public void setDaoSession(DaoSession daoSession) {
        for (ISensor sensor : m_liSensors) {
            sensor.setDaoSession(daoSession);
        }
    }

}
