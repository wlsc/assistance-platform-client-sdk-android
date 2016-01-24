package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;

import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.triggered.MotionActivitySensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

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

            MotionActivitySensor motionActivitySensor = MotionActivitySensor.getInstance(getApplicationContext());

            if (result != null && motionActivitySensor != null) {

                motionActivitySensor.handleData(result);

            } else {
                Log.e(TAG, "Cannot extract recognition result!");
            }
        }
    }

}
