package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered.ActivitySensor;

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
            ActivitySensor activitySensor = ActivitySensor.getInstance();

            if (result != null && activitySensor != null) {
                activitySensor.handleData(result);
            } else {
                Log.e(TAG, "Cannot extract recognition result!");
            }
        }
    }

}
