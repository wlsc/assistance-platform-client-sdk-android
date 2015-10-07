package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.TaskParams;

import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.ServiceManager;
import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.communication.ServiceGenerator;
import de.tudarmstadt.informatik.tk.android.kraken.communication.endpoint.EventUploadEndpoint;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensorDao;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.EventUploadRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.AccelerometerSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.PositionSensorRequest;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.sensors.SensorType;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class UploadService extends GcmTaskService {

    private static final String TAG = UploadService.class.getSimpleName();

    private static final int PUSH_NUMBER_OF_EACH_ELEMENTS = 50;

    // task identifier
    private long taskID = 1;
    // the task should be executed every 30 seconds
    private long periodSecs = 30L;
    // the task can run as early as -15 seconds from the scheduled time
    private long flexSecs = 15L;

    // a unique task identifier
    private String tag = "periodic  | " + taskID++ + ": " + periodSecs + "s, f:" + flexSecs;

    private ServiceManager serviceManager;

    private static PreferenceManager mPreferenceManager;

    private static PeriodicTask periodicTask;

    private List<DbAccelerometerSensor> dbAccelerometerSensors;
    private List<DbPositionSensor> dbPositionSensors;

    private static DbAccelerometerSensorDao dbAccelerometerSensorDao;
    private static DbPositionSensorDao dbPositionSensorDao;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Creating...");

        if (serviceManager == null) {
            serviceManager = ServiceManager.getInstance(getApplicationContext());
        }

        serviceManager.showIcon(true);

        if (mPreferenceManager == null) {
            mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());
        }

        if (dbAccelerometerSensorDao == null) {
            dbAccelerometerSensorDao = DbManager.getInstance(getApplicationContext()).getDaoSession().getDbAccelerometerSensorDao();
        }

        if (dbPositionSensorDao == null) {
            dbPositionSensorDao = DbManager.getInstance(getApplicationContext()).getDaoSession().getDbPositionSensorDao();
        }

        if (periodicTask == null) {

            Log.d(TAG, "Setting up periodic task...");

            periodicTask = new PeriodicTask.Builder()
                    .setService(UploadService.class)
                    .setPeriod(periodSecs)
                    .setFlex(flexSecs)
                    .setTag(tag)
                    .setPersisted(true)
                    .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                    .setRequiresCharging(false)
                    .build();

            GcmNetworkManager.getInstance(getApplicationContext()).schedule(periodicTask);
        }
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        // Do some upload work.

        Log.d(TAG, "On run task executed");

        EventUploadRequest eventUploadRequest = new EventUploadRequest();

        long serverDeviceId = mPreferenceManager.getServerDeviceId();

        Log.d(TAG, "Current server device id: " + serverDeviceId);

        List<Sensor> events = new LinkedList<>();

        events.addAll(getAccelerometerEntries());
        events.addAll(getPositionEntries());

        eventUploadRequest.setDataEvents(events);
        eventUploadRequest.setServerDeviceId(serverDeviceId);

        doUploadEventData(eventUploadRequest);

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    /**
     * Harvest particular number of accelerometer sensor entries from database
     *
     * @return
     */
    private List<Sensor> getAccelerometerEntries() {

        List<Sensor> result = new LinkedList<>();

        dbAccelerometerSensors = dbAccelerometerSensorDao
                .queryBuilder()
                .limit(PUSH_NUMBER_OF_EACH_ELEMENTS)
                .build()
                .list();

        if (dbAccelerometerSensors != null) {

            for (int i = 0; i < dbAccelerometerSensors.size(); i++) {

                DbAccelerometerSensor sensor = dbAccelerometerSensors.get(i);

                AccelerometerSensorRequest accelerometerSensorRequest = new AccelerometerSensorRequest();

                accelerometerSensorRequest.setType(SensorType.ACCELEROMETER);
                accelerometerSensorRequest.setTypeStr(SensorType.getApiName(SensorType.ACCELEROMETER));
                accelerometerSensorRequest.setX(sensor.getX());
                accelerometerSensorRequest.setY(sensor.getY());
                accelerometerSensorRequest.setZ(sensor.getZ());
                accelerometerSensorRequest.setAccuracy(sensor.getAccuracy());
                accelerometerSensorRequest.setCreated(sensor.getCreated());

                result.add(accelerometerSensorRequest);
            }
        }

        return result;
    }

    /**
     * Returns number of first entries of position events from database
     *
     * @return
     */
    private List<Sensor> getPositionEntries() {

        List<Sensor> result = new LinkedList<>();

        dbPositionSensors = dbPositionSensorDao
                .queryBuilder()
                .limit(PUSH_NUMBER_OF_EACH_ELEMENTS)
                .build()
                .list();

        if (dbPositionSensors != null) {

            for (int i = 0; i < dbPositionSensors.size(); i++) {

                DbPositionSensor sensor = dbPositionSensors.get(i);

                PositionSensorRequest positionSensorRequest = new PositionSensorRequest();

                positionSensorRequest.setType(SensorType.POSITION);
                positionSensorRequest.setTypeStr(SensorType.getApiName(SensorType.POSITION));
                positionSensorRequest.setLatitude(sensor.getLatitude());
                positionSensorRequest.setLongitude(sensor.getLongitude());
                positionSensorRequest.setAccuracyHorizontal(sensor.getAccuracyHorizontal());
                positionSensorRequest.setAccuracyVertical(sensor.getAccuracyVertical());
                positionSensorRequest.setAltitude(sensor.getAltitude());
                positionSensorRequest.setSpeed(sensor.getSpeed());
                positionSensorRequest.setCreated(sensor.getCreated());

                result.add(positionSensorRequest);
            }
        }

        return result;
    }

    /**
     * Pushes events data to server
     *
     * @param eventUploadRequest
     */
    private void doUploadEventData(final EventUploadRequest eventUploadRequest) {

        if (eventUploadRequest == null || eventUploadRequest.getDataEvents() == null) {
            return;
        }

        if (eventUploadRequest.getDataEvents().size() == 0) {
            return;
        }

        // send to upload data service
        EventUploadEndpoint eventUploadEndpoint = ServiceGenerator.createService(EventUploadEndpoint.class);

        String userToken = mPreferenceManager.getUserToken();

        eventUploadEndpoint.uploadData(userToken, eventUploadRequest, new Callback<Void>() {

            @Override
            public void success(Void aVoid, Response response) {

                if (response != null && (response.getStatus() == 200 || response.getStatus() == 204)) {

                    // successful transmission of event data -> remove that data from db
                    removeDbEventsSent();

                } else {
                    // TODO: show error
                }
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO process error
            }
        });
    }

    /**
     * Removes successful transmitted entries from database
     */
    private void removeDbEventsSent() {

        Log.d(TAG, "Removing sent event data from db...");

        if (dbAccelerometerSensors != null) {

            if (dbAccelerometerSensorDao == null) {
                dbAccelerometerSensorDao = DbManager.getInstance(getApplicationContext()).getDaoSession().getDbAccelerometerSensorDao();
            }

            dbAccelerometerSensorDao.deleteInTx(dbAccelerometerSensors);
        }

        if (dbPositionSensors != null) {

            if (dbPositionSensorDao == null) {
                dbPositionSensorDao = DbManager.getInstance(getApplicationContext()).getDaoSession().getDbPositionSensorDao();
            }

            dbPositionSensorDao.deleteInTx(dbPositionSensors);
        }

        Log.d(TAG, "Finished removing data from db");
    }

}
