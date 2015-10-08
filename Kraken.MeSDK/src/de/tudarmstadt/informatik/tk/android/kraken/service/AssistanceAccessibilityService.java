package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import de.tudarmstadt.informatik.tk.android.kraken.SensorManager;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.impl.triggered.ForegroundTrafficEvent;


public class AssistanceAccessibilityService extends AccessibilityService {

    private static final String TAG = AssistanceAccessibilityService.class.getSimpleName();

    private ForegroundEvent mForegroundSensor;
    private ForegroundTrafficEvent mForegroundTrafficEvent;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Starting service...");

        mForegroundSensor = (ForegroundEvent) SensorManager
                .getInstance(getApplicationContext())
                .getSensor(SensorType.FOREGROUND);

        mForegroundTrafficEvent = (ForegroundTrafficEvent) SensorManager
                .getInstance(getApplicationContext())
                .getSensor(SensorType.NETWORK_TRAFFIC);

        Log.d(TAG, "Successfully started.");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(TAG, "onAccessibilityEvent");

        mForegroundSensor.onEvent(event);

//        mForegroundTrafficSensor.onEvent(event);
    }

    @Override
    public void onInterrupt() {

        Log.d(TAG, "Service interrupted!");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Stopping service...");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        Log.d(TAG, "Service connected.");

        /*AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);*/
    }

}
