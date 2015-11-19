package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensor.impl.triggered.MotionActivityEvent;

public class ActivitySensorService extends IntentService {

    private static final String TAG = ActivitySensorService.class.getSimpleName();

    public ActivitySensorService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (ActivityRecognitionResult.hasResult(intent)) {

            Log.d(TAG, "The activity recognition found.");

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            MotionActivityEvent motionActivityEvent = MotionActivityEvent.getInstance(getApplicationContext());

            if (result != null && motionActivityEvent != null) {

                motionActivityEvent.handleData(result);

            } else {
                Log.e(TAG, "Cannot extract recognition result!");
            }
        }
    }

}
