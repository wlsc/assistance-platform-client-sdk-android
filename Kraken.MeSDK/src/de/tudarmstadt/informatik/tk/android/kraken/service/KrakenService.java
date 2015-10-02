package de.tudarmstadt.informatik.tk.android.kraken.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.Serializable;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.ActivityCommunicator;
import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.PreferenceManager;
import de.tudarmstadt.informatik.tk.android.kraken.R;
import de.tudarmstadt.informatik.tk.android.kraken.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.db.DatabaseManager;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallation;
import de.tudarmstadt.informatik.tk.android.kraken.db.DbModuleInstallationDao;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ECommandType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.interfaces.ISensor;


public class KrakenService extends Service implements Callback {

    private static final String TAG = KrakenService.class.getSimpleName();

    // public static ScheduledExecutorService m_scheduleTaskExecutor;

    private boolean m_bIsRunning = false;

    private static KrakenService instance;

    private final Messenger messenger = new Messenger(new Handler(this));

    private SensorManager mSensorManager;
    private PreferenceManager mPreferenceManager;
    private DatabaseManager mDatabaseManager;

    private static DbModuleInstallationDao dbModuleInstallationDao;

    private NotificationManager mNotificationManager;

    public static KrakenService getInstance() {
        return instance;
    }

    public DaoSession getDaoSession() {
        return mDatabaseManager.getDaoSession();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service starting...");

        instance = this;

        // Init database FIRST!
        mDatabaseManager = DatabaseManager.getInstance(getApplicationContext());

        mPreferenceManager = PreferenceManager.getInstance(getApplicationContext());

        startKrakenService();
    }

    private void startKrakenService() {

        if (!m_bIsRunning) {
            m_bIsRunning = true;

            if (dbModuleInstallationDao == null) {
                dbModuleInstallationDao = mDatabaseManager.getDaoSession().getDbModuleInstallationDao();
            }

            SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            long userId = sharedPreferences.getLong("current_user_id", -1);

            List<DbModuleInstallation> dbModuleInstallations = dbModuleInstallationDao
                    .queryBuilder()
                    .where(DbModuleInstallationDao.Properties.UserId.eq(userId))
                    .build()
                    .list();

            if (dbModuleInstallations != null && !dbModuleInstallations.isEmpty()) {
                Log.d(TAG, "Found active modules -> starting monitoring activities...");

                monitorStart();

            } else {
                Log.d(TAG, "No active module were found!");
            }
        }

        RetroServerPushManager.getInstance(getApplicationContext());

        if (mPreferenceManager.getShowNotification()) {
            showIcon();
        } else {
            hideIcon();
        }

        Log.d(TAG, "Service has started.");
    }

    private void stopKrakenService() {

        if (m_bIsRunning) {
            monitorStop();
            m_bIsRunning = false;
        }

        RetroServerPushManager.stopPeriodicPush();

        setActivityHandler(null);
        hideIcon();
        stopSelf();
    }

    private void monitorStart() {

        Log.d(TAG, "Starting monitoring service...");

//		Handler handler = ActivityCommunicator.getHandler();

        mSensorManager = SensorManager.getInstance(this);

        List<ISensor> enabledSensors = mSensorManager.getEnabledSensors();

        Log.d(TAG, "Active sensors: " + enabledSensors.size());

        for (ISensor sensor : enabledSensors) {
            sensor.startSensor();
        }

        Log.d(TAG, "All sensors are enabled!");

        // TODO: enable that later
//        startAccessibilityService();
    }

    private void monitorStop() {

        Log.d(TAG, "Stopping service...");

        if (mSensorManager == null) {
            mSensorManager = SensorManager.getInstance(this);
        }

        Log.d(TAG, "Active sensors: " + mSensorManager.getEnabledSensors());

        for (ISensor sensor : mSensorManager.getEnabledSensors()) {
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

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_kraken_service)
                        .setContentTitle(getString(R.string.service_running_notification_title))
                        .setContentText(getString(R.string.service_running_notification_text))
                        .setOngoing(true);

        mNotificationManager.notify("kraken", KrakenSdkSettings.KRAKEN_NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Hides service ongoing notification that service is running
     */
    private void hideIcon() {

        Log.d(TAG, "Hiding icon...");

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        mNotificationManager.cancel("kraken", KrakenSdkSettings.KRAKEN_NOTIFICATION_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");

        if (intent != null && intent.hasExtra(KrakenSdkSettings.INTENT_EXTRA_SHOW_ICON)) {

            boolean showIcon = intent.getBooleanExtra(KrakenSdkSettings.INTENT_EXTRA_SHOW_ICON, PreferenceManager.DEFAULT_KRAKEN_SHOW_NOTIFICATION);

            if (showIcon) {
                showIcon();
            } else {
                hideIcon();
            }
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "Service onDestroy");

        stopKrakenService();

        dbModuleInstallationDao = null;

        super.onDestroy();
    }

    public boolean isRunning() {
        return m_bIsRunning;
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
                Messenger messenger = (Messenger) data.getParcelable("value");
                setActivityHandler(messenger);
                break;
            case START_SERVICE:
                startKrakenService();
                break;
            case STOP_SERVICE:
                stopKrakenService();
                break;
            case REMOVE_HANDLER:
                setActivityHandler(null);
                break;
            case HIDE_ICON:
                hideIcon();
                break;
            case SHOW_ICON:
                showIcon();
                break;
            default:
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

    private void startAccessibilityService() {

        Log.d(TAG, "Starting accessibility service...");

        Intent intent = new Intent(this, KrakenAccessibilityService.class);
        startService(intent);
    }

}
