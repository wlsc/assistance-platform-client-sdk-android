package de.tudarmstadt.informatik.tk.android.assistance.sdk.service;

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
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;

import java.io.Serializable;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.ActivityCommunicator;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.Config;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbModule;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbUser;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.ECommandType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.DaoProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.PreferenceProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.SensorProvider;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DeviceUtils;

/**
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 08.10.2015
 */
public class HarvesterService extends Service implements Callback {

    private static final String TAG = HarvesterService.class.getSimpleName();

    private static HarvesterService INSTANCE;

    private final Messenger messenger = new Messenger(new Handler(this));

    private SensorProvider mSensorProvider;
    private PreferenceProvider mPreferenceProvider;

    private DaoProvider daoProvider;

//    private NotificationManager mNotificationManager;

    private static boolean mSensorsStarted;

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

        mPreferenceProvider = PreferenceProvider.getInstance(getApplicationContext());

        String userToken = mPreferenceProvider.getUserToken();

        // check if user is logged in
        if (userToken == null || userToken.isEmpty()) {
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

        String userToken = PreferenceProvider.getInstance(getApplicationContext()).getUserToken();

        if (userToken.isEmpty()) {
            Log.d(TAG, "UserToken is empty. No point to continue");
            return;
        }

        DbUser user = daoProvider
                .getUserDao()
                .getByToken(userToken);

        if (user == null) {
            Log.d(TAG, "User is null. No point to continue");
            return;
        }

        List<DbModule> activeModules = daoProvider
                .getModuleDao()
                .getAllActive(user.getId());

        if (activeModules != null && !activeModules.isEmpty()) {

            if (!mSensorsStarted) {

                Log.d(TAG, "Found installed modules -> starting monitoring activities...");

                monitorStart();
            }

            // schedule uploader task
            startService(new Intent(this, EventUploaderService.class));

            if (!DeviceUtils.isServiceRunning(getApplicationContext(),
                    AssistanceAccessibilityService.class)) {

                startAccessibilityService();
            }

        } else {

            Log.d(TAG, "No active module were found!");

            mSensorsStarted = false;
        }

        Log.d(TAG, "Service was initiated.");
    }

    /**
     * Stop the service
     */
    private void stopService() {

        monitorStop();

        GcmNetworkManager.getInstance(getApplicationContext()).cancelAllTasks(EventUploaderService.class);

        setActivityHandler(null);
        stopForeground(true);
        stopSelf();
    }

    /**
     * Starts sensors / events
     */
    private void monitorStart() {

        Log.d(TAG, "Starting monitoring service...");

        mSensorsStarted = true;

        mSensorProvider = SensorProvider.getInstance(this);

        List<ISensor> enabledSensors = mSensorProvider.getEnabledSensors();

        for (ISensor sensor : enabledSensors) {

            if (sensor == null) {
                continue;
            }

            sensor.startSensor();
        }

        showIcon();

        Log.d(TAG, "All sensors are enabled!");
    }

    /**
     * Stops sensors / events
     */
    private void monitorStop() {

        Log.d(TAG, "Stopping service...");

        mSensorsStarted = false;

        if (mSensorProvider == null) {
            mSensorProvider = SensorProvider.getInstance(this);
        }

        for (ISensor sensor : mSensorProvider.getEnabledSensors()) {

            if (sensor == null) {
                continue;
            }

            sensor.stopSensor();
        }

        Log.d(TAG, "All sensors were stopped.");
        Log.d(TAG, "Service was stopped.");
    }

    /**
     * Shows service ongoing notification that service is running
     */
    private void showIcon() {

        Log.d(TAG, "Showing icon...");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_kraken_service)
                        .setContentTitle(getString(R.string.service_running_notification_title))
                        .setContentText(getString(R.string.service_running_notification_text))
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

//            if (mSensorsStarted) {
//                monitorStop();
//            } else {
//                monitorStart();
//            }

            if (mSensorsStarted) {

                // show icon on command
                if (intent != null && intent.hasExtra(Config.INTENT_EXTRA_SHOW_ICON)) {

//                    boolean showIcon = intent.getBooleanExtra(
//                            Config.INTENT_EXTRA_SHOW_ICON,
//                            PreferenceProvider.DEFAULT_KRAKEN_SHOW_NOTIFICATION);
//
//                    if (showIcon) {
//                        showIcon();
//                    } else {
//                        hideIcon();
//                    }
                }

                return Service.START_STICKY;
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service...");
        stopService();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Unbinding...");
        setActivityHandler(null);
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding...");
        return messenger.getBinder();
    }

    private void setActivityHandler(Messenger messenger) {
        ActivityCommunicator.setMessenger(messenger, this);
    }

    @Override
    public boolean handleMessage(Message msg) {

        Bundle data = msg.getData();
        Serializable obj = data.getSerializable("command");
        if (obj == null || !(obj instanceof ECommandType)) {
            return false;
        }

        ECommandType command = (ECommandType) obj;

        switch (command) {
            case SET_HANDLER:
                Log.d(TAG, "Command: SET_HANDLER received");
                Messenger messenger = (Messenger) data.getParcelable("value");
                setActivityHandler(messenger);
                break;
            case START_SERVICE:
                Log.d(TAG, "Command: START_SERVICE received");
                initService();
                break;
            case STOP_SERVICE:
                Log.d(TAG, "Command: STOP_SERVICE received");
                stopService();
                break;
            case REMOVE_HANDLER:
                Log.d(TAG, "Command: REMOVE_HANDLER received");
                setActivityHandler(null);
                break;
            case HIDE_ICON:
                Log.d(TAG, "Command: HIDE_ICON received");
                hideIcon();
                break;
            case SHOW_ICON:
                Log.d(TAG, "Command: SHOW_ICON received");
                showIcon();
                break;
            default:
                Log.e(TAG, "Unknown Command!");
                return false;
        }
        return true;
    }

    public static void sendCommand(Messenger messenger, ECommandType command, Object value) {

        if (messenger == null || command == null) {
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
     * Starts AccessibilityService
     */
    private void startAccessibilityService() {

        Log.d(TAG, "Starting accessibility service...");

        Intent intent = new Intent(this, AssistanceAccessibilityService.class);
        startService(intent);
    }

}
