package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.triggered.ForegroundTrafficEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * Assistance Accessibility Service implementation
 *
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class AssistanceAccessibilityService extends AccessibilityService {

    private static final String TAG = AssistanceAccessibilityService.class.getSimpleName();

    private ForegroundEvent mForegroundSensor;
    private ForegroundTrafficEvent mForegroundTrafficEvent;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Starting service...");

        mForegroundSensor = (ForegroundEvent) SensorProvider
                .getInstance(getApplicationContext())
                .getAvailableSensor(DtoType.FOREGROUND);

        mForegroundTrafficEvent = (ForegroundTrafficEvent) SensorProvider
                .getInstance(getApplicationContext())
                .getAvailableSensor(DtoType.FOREGROUND_TRAFFIC);

        Log.d(TAG, "Successfully started.");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(TAG, "onAccessibilityEvent");

        if (mForegroundSensor != null) {
            mForegroundSensor.onEvent(event);
        }

        if (mForegroundTrafficEvent != null) {
            mForegroundTrafficEvent.onEvent(event);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service was interrupted!");
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.d(TAG, "Unbinding accessibility service...");

        PreferenceProvider.getInstance(getApplicationContext()).setActivated(false);

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

        Log.d(TAG, "Service is connected!");

        PreferenceProvider.getInstance(getApplicationContext()).setActivated(true);

    }

}
