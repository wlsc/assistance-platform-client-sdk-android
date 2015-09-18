package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.triggered;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;

public class ActivitySensorService extends IntentService {

    public ActivitySensorService() {
        super("ActivityRecognitionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            ActivitySensor activitySensor = ActivitySensor.getInstance();

            if (result != null && activitySensor != null) {
                activitySensor.sendData(result.getMostProbableActivity());
            }

//			List<DetectedActivity> liActivities = result.getProbableActivities();
//			ActivitySensor.getInstance().sendData(liActivities);
        }
    }

}
