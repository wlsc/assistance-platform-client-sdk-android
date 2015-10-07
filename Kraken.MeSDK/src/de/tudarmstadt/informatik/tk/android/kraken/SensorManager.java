package de.tudarmstadt.informatik.tk.android.kraken;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.api.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.model.enums.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.ISensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.BrowserHistoryEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.CalendarEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.CallLogEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.contentobserver.ContactsEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.periodic.BackgroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.periodic.RingtoneEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.AccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ConnectionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.LocationSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MeasurementEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.MotionActivityEvent;


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

        MotionActivityEvent motionActivityEvent = MotionActivityEvent.getInstance(context);
        m_liSensors.add(motionActivityEvent);
        retroServerPushManager.setPushType(motionActivityEvent, motionActivityEvent.getPushType());

        // works
        LightSensor lightSensor = new LightSensor(context);
        m_liSensors.add(lightSensor);
        retroServerPushManager.setPushType(lightSensor, lightSensor.getPushType());

        // works
        LocationSensor locationSensor = new LocationSensor(context);
        m_liSensors.add(locationSensor);
        retroServerPushManager.setPushType(locationSensor, locationSensor.getPushType());

        MeasurementEvent measurementEvent = new MeasurementEvent(context);
        m_liSensors.add(measurementEvent);
        retroServerPushManager.setPushType(measurementEvent, measurementEvent.getPushType());

        // works
        ConnectionSensor connectionSensor = new ConnectionSensor(context);
        m_liSensors.add(connectionSensor);
        retroServerPushManager.setPushType(connectionSensor, connectionSensor.getPushType());

        //new foreground traffic
        ForegroundTrafficEvent foregroundTrafficEvent = new ForegroundTrafficEvent(context);
        m_liSensors.add(foregroundTrafficEvent);
        retroServerPushManager.setPushType(foregroundTrafficEvent, foregroundTrafficEvent.getPushType());

        //new periodic background traffic
        BackgroundTrafficEvent backgroundTrafficEvent = new BackgroundTrafficEvent(context);
        m_liSensors.add(backgroundTrafficEvent);
        retroServerPushManager.setPushType(backgroundTrafficEvent, backgroundTrafficEvent.getPushType());

        // loudness sensor is blocking microphone and consuming too much battery
        // LoudnessSensor loudnessSensor = new LoudnessSensor(context);
        // m_liSensors.add(loudnessSensor);
        // ServerPushManager.getInstance(context).setPushType(loudnessSensor, loudnessSensor.getPushType());

        // periodic
        RingtoneEvent ringtoneEvent = new RingtoneEvent(context);
        m_liSensors.add(ringtoneEvent);
        retroServerPushManager.setPushType(ringtoneEvent, ringtoneEvent.getPushType());

        CalendarEvent calendarEvent = new CalendarEvent(context);
        m_liSensors.add(calendarEvent);
        retroServerPushManager.setPushType(calendarEvent, calendarEvent.getPushType());

        ContactsEvent contactsEvent = new ContactsEvent(context);
        m_liSensors.add(contactsEvent);
        retroServerPushManager.setPushType(contactsEvent, contactsEvent.getPushType());

        CallLogEvent callLogEvent = new CallLogEvent(context);
        m_liSensors.add(callLogEvent);
        retroServerPushManager.setPushType(callLogEvent, callLogEvent.getPushType());

        BrowserHistoryEvent browserHistoryEvent = new BrowserHistoryEvent(context);
        m_liSensors.add(browserHistoryEvent);
        retroServerPushManager.setPushType(browserHistoryEvent, browserHistoryEvent.getPushType());

        ForegroundEvent foregroundEvent = new ForegroundEvent(context);
        m_liSensors.add(foregroundEvent);
        retroServerPushManager.setPushType(foregroundEvent, foregroundEvent.getPushType());

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
