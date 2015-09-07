package de.tudarmstadt.informatik.tk.android.kraken.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.triggered.ForegroundEventSensor;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.triggered.ForegroundTrafficSensor;


public class KrakenAccessibilityService extends AccessibilityService {

    private static final String TAG = KrakenAccessibilityService.class.getSimpleName();

    private ForegroundEventSensor mForegroundSensor;
    private ForegroundTrafficSensor mForegroundTrafficSensor;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        mForegroundSensor = (ForegroundEventSensor) SensorManager
                .getInstance(getApplicationContext())
                .getSensor(ESensorType.SENSOR_FOREGROUND_EVENT);
        mForegroundTrafficSensor = (ForegroundTrafficSensor) SensorManager
                .getInstance(getApplicationContext())
                .getSensor(ESensorType.SENSOR_NETWORK_TRAFFIC);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //Log.d(TAG, "onAccessibilityEvent");

        mForegroundSensor.onEvent(event);

//        mForegroundTrafficSensor.onEvent(event);
    }

    @Override
    public void onInterrupt() {

        Log.d(TAG, "onInterrupt");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.d(TAG, "onServiceConnected");

        /*AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);*/
    }

}
