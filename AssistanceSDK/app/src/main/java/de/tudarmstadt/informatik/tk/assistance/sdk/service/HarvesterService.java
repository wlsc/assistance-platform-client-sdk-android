package de.tudarmstadt.informatik.tk.assistance.sdk.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmNetworkManager;

import java.util.HashSet;
import java.util.Set;

import de.tudarmstadt.informatik.tk.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.assistance.sdk.R;
import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.ServiceUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.StringUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class HarvesterService extends Service implements Callback {

    private static final String TAG = HarvesterService.class.getSimpleName();

    /**
     * Messenger incoming commands
     */
    public static final int MSG_CMD_REGISTER_CLIENT = 1;
    public static final int MSG_CMD_UNREGISTER_CLIENT = 2;
    public static final int MSG_CMD_START_SERVICE = 3;
    public static final int MSG_CMD_STOP_SERVICE = 4;
    public static final int MSG_CMD_SHOW_ICON = 5;
    public static final int MSG_CMD_HIDE_ICON = 6;

    private static HarvesterService INSTANCE;

    private final Messenger messenger = new Messenger(new Handler(this));

    // clients that wants to communicate with this service
    private Set<Messenger> mClients = new HashSet<>();

    private PreferenceProvider mPreferenceProvider;

    private DaoProvider daoProvider;

    private boolean mSensorsStarted;
    private SensorProvider sensorProvider;

    public HarvesterService() {
    }

    public static HarvesterService getInstance() {
        return INSTANCE;
    }

    public DaoSession getDaoSession() {

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(getApplicationContext());
        }

        return daoProvider.getDaoSession();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (INSTANCE == null) {
            INSTANCE = this;
        }

        Log.d(TAG, "Service starting...");

        this.sensorProvider = SensorProvider.getInstance(getApplicationContext());
        mPreferenceProvider = PreferenceProvider.getInstance(getApplicationContext());

        String userToken = mPreferenceProvider.getUserToken();

        // check if user is logged in
        if (StringUtils.isNullOrEmpty(userToken)) {
            Log.d(TAG, "User token is null or empty");
            return;
        }

        if (daoProvider == null) {
            daoProvider = DaoProvider.getInstance(getApplicationContext());
        }

        initService();
    }

    /**
     * Initializes a service
     */
    private void initService() {

        Log.d(TAG, "Initializing service...");

        if (!ServiceUtils.isHarvesterAbleToRun(getApplicationContext())) {
            Log.d(TAG, "Harvester was not able to run now");
            return;
        }

//        Map<Integer, ISensor> enabledSensors = SensorProvider
//                .getInstance(getApplicationContext())
//                .getRunningSensors();
//
//        if (enabledSensors != null && enabledSensors.size() > 0) {

        monitorStart();

        // schedule uploader task
        startService(new Intent(this, SensorUploadService.class));

        startAccessibilityService();

//        } else {
//            Log.d(TAG, "No active module were found!");
//            mSensorsStarted = false;
//        }

        Log.d(TAG, "Service was initiated.");
    }

    /**
     * Stop the service
     */
    private void stopService() {

        monitorStop();

        GcmNetworkManager.getInstance(getApplicationContext()).cancelAllTasks(SensorUploadService.class);

        stopForeground(true);
        stopSelf();
    }

    /**
     * Starts sensors / events
     */
    private void monitorStart() {

        Log.d(TAG, "Starting sensor monitoring...");

        mSensorsStarted = true;

        SensorProvider.getInstance(getApplicationContext()).startAllStoppedSensors();

        showIcon();

        Log.d(TAG, "All sensors are enabled!");
    }

    /**
     * Stops sensors / events
     */
    private void monitorStop() {

        Log.d(TAG, "Stopping sensor monitoring...");

        mSensorsStarted = false;

        SensorProvider.getInstance(getApplicationContext()).stopAllRunningSensors();

        Log.d(TAG, "All sensors were stopped.");
        Log.d(TAG, "Service was stopped.");
    }

    /**
     * Shows service ongoing notification that service is running
     */
    private void showIcon() {

        if (!ServiceUtils.isHarvesterAbleToRun(getApplicationContext())) {
            Log.d(TAG, "Harvester was not able to run now");
            return;
        }

        Log.d(TAG, "Showing icon...");

        Class mainClass = null;

        try {
            mainClass = Class.forName("de.tudarmstadt.informatik.tk.assistance.activity.MainActivity");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Not found main activity");
        }

        Intent notificationIntent = new Intent(getApplicationContext(), mainClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_assistance_service)
                        .setContentTitle(getString(R.string.service_running_notification_title))
                        .setContentText(getString(R.string.service_running_notification_text))
                        .setContentIntent(pendingIntent)
//                        .setPriority(Notification.PRIORITY_MIN)
                        .setOngoing(true);

        startForeground(Config.DEFAULT_NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Hides service ongoing notification that service is running
     */
    private void hideIcon() {

        Log.d(TAG, "Hiding icon...");

        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");

        String userToken = mPreferenceProvider.getUserToken();

        if (userToken != null && !userToken.isEmpty()) {

            if (mSensorsStarted) {
                Log.d(TAG, "OK: sensors are started");

                return Service.START_STICKY;
            } else {
                Log.d(TAG, "WARNING: sensors were NOT started!");
                initService();
            }
        } else {
            Log.d(TAG, "User token is NULL or EMPTY");
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service...");
        mClients = null;
        stopService();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Unbinding...");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding...");
        return messenger.getBinder();
    }

    @Override
    public boolean handleMessage(Message msg) {

        Log.d(TAG, "Processing incoming message for command " + msg.what + "...");

        switch (msg.what) {
            case MSG_CMD_REGISTER_CLIENT:
                Log.d(TAG, "Command: REGISTER_CLIENT received");
                mClients.add(msg.replyTo);
                break;

            case MSG_CMD_UNREGISTER_CLIENT:
                Log.d(TAG, "Command: UNREGISTER_CLIENT received");
                mClients.remove(msg.replyTo);
                break;

            case MSG_CMD_START_SERVICE:
                Log.d(TAG, "Command: START_SERVICE received");
                initService();
                break;

            case MSG_CMD_STOP_SERVICE:
                Log.d(TAG, "Command: STOP_SERVICE received");
                stopService();
                break;

            case MSG_CMD_HIDE_ICON:
                Log.d(TAG, "Command: HIDE_ICON received");
                hideIcon();
                break;

            case MSG_CMD_SHOW_ICON:
                Log.d(TAG, "Command: SHOW_ICON received");

                if (mSensorsStarted) {
                    showIcon();
                } else {
                    initService();
                }
                break;

            default:
                Log.d(TAG, "Unknown service command!");
                return false;
        }

        Log.d(TAG, "Done processing.");
        return true;
    }

    public static void sendCommand(Messenger messenger, int command, Object value) {

        if (messenger == null || command <= 0) {
            return;
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putSerializable("command", command);

        if (value != null) {
            if (value instanceof String) {
                bundle.putString("value", (String) value);
            } else {
                if (value instanceof Parcelable) {
                    bundle.putParcelable("value", (Parcelable) value);
                }
            }
        }

        msg.setData(bundle);

        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot send message! Error: ", e);
        }
    }

    /**
     * Starts accessibility service
     */
    private void startAccessibilityService() {

        if (!ServiceUtils.isServiceRunning(getApplicationContext(),
                AssistanceAccessibilityService.class)) {

            Log.d(TAG, "Starting accessibility service...");

            Intent intent = new Intent(this, AssistanceAccessibilityService.class);
            startService(intent);
        }
    }

}
