package de.tudarmstadt.informatik.tk.kraken.android.sdk.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import java.io.Serializable;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.ActivityCommunicator;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.RetroServerPushManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.preference.PreferenceManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.ECommandType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.SensorManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.utils.DatabaseManager;
import de.tudarmstadt.informatik.tk.kraken.sdk.R;

//import de.tudarmstadt.informatik.tk.kraken.android.ui.activities.MainActivity;

public class KrakenService extends Service implements Callback {

	// public static ScheduledExecutorService m_scheduleTaskExecutor;

	private boolean m_bIsRunning = false;
	private static KrakenService m_service;

	final private Messenger m_Messenger = new Messenger(new Handler(this));

	private SensorManager m_sensorManager;
//	private static ObjectMapper m_mapper;
    private PreferenceManager mPreferenceManager;
    private DatabaseManager mDatabaseManager;

//	public class ServiceBinder extends Binder {
//
//		public KrakenService getService() {
//			return KrakenService.this;
//		}
//	}

	public static KrakenService getInstance() {
		return m_service;
	}

	public DaoSession getDaoSession() {
		return mDatabaseManager.getDaoSession();
	}

	@Override
	public void onCreate() {
		super.onCreate();

        Log.d("kraken", "Service onCreate");

		m_service = this;

		// Init database FIRST!
		mDatabaseManager = DatabaseManager.getInstance(this);

        mPreferenceManager = PreferenceManager.getInstance(this);

		// GcmManager.getInstance(this).registerAtCloud();

		// TODO: enable it later
//		startService();
	}

	private void startService() {
		if (!m_bIsRunning) {
			m_bIsRunning = true;
			monitorStart();
		}

		//ServerPushManager.getInstance(this);
        // TODO: remove
		RetroServerPushManager.getInstance(this);

        if(mPreferenceManager.getShowNotification()) {
            showIcon();
        }
        else {
            hideIcon();
        }
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showIcon() {
		//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		//stackBuilder.addParentStack(MainActivity.class);
		//Intent resultIntent = new Intent(this, MainActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		//stackBuilder.addNextIntent(resultIntent);
		//PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		//builder.setSmallIcon(R.drawable.ic_kraken_service).setContentIntent(resultPendingIntent);

        // FIXME This is disabled for a short time, due to ongoing brainstorming on design

        Resources res = getResources();
        int height = Math.round(res.getDimension(android.R.dimen.notification_large_icon_height) * 0.75f);
        int width = Math.round(res.getDimension(android.R.dimen.notification_large_icon_width) * 0.75f);
        Bitmap kraki = BitmapFactory.decodeResource(res, R.drawable.kraki_big);
        builder.setLargeIcon(Bitmap.createScaledBitmap(kraki, width, height, false));

		builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.service_notfication_text));

		builder.setOngoing(true);
		// startForeground(7331, builder.build());

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify("kraken", 7331, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ? builder.build() : builder.getNotification()));
	}

	private void hideIcon() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel("kraken", 7331);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("kraken", "Service onStartCommand");

        if(intent != null && intent.hasExtra("showIcon")) {
            boolean showIcon = intent.getBooleanExtra("showIcon", PreferenceManager.DEFAULT_KRAKEN_SHOW_NOTIFICATION);
            if(showIcon) {
                showIcon();
            }
            else {
                hideIcon();
            }
        }

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {

        Log.d("kraken", "Service onDestroy");

		stopService();
		super.onDestroy();
	}

	private void stopService() {
		if (m_bIsRunning) {
			monitorStop();
			m_bIsRunning = false;
		}

		//ServerPushManager.stopPeriodicPush();
		RetroServerPushManager.stopPeriodicPush();
		setActivityHandler(null);
		hideIcon();
		stopSelf();
	}

	public boolean isRunning() {
		return m_bIsRunning;
	}

	private void monitorStart() {

		System.out.println("start service");

//		Handler handler = ActivityCommunicator.getHandler();

		m_sensorManager = SensorManager.getInstance(this);
		for (ISensor sensor : m_sensorManager.getEnabledSensors()) {
			sensor.startSensor();
//			sensor.setCallbackHandler(handler);
		}

		startAccessibilityService();
	}

	private void monitorStop() {
		System.out.println("stop service");
		for (ISensor sensor : m_sensorManager.getEnabledSensors()) {
			sensor.stopSensor();
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {

        Log.d("kraken", "Service onUnbind");

		setActivityHandler(null);
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {

        Log.d("kraken", "Service onBind");

		// Object obj = intent.getExtras().get("messenger");
		// if (obj != null && obj instanceof Messenger)
		// setActivityHandler((Messenger) obj);

		return m_Messenger.getBinder();
		// return m_serviceBinder;
	}

//	public static ObjectMapper getJacksonObjectMapper() {
//		if (m_mapper == null)
//		{
//			m_mapper = new ObjectMapper();
//			m_mapper.registerModule(new JsonOrgModule());
//		}
//		return m_mapper;
//	}

	private void setActivityHandler(Messenger messenger) {
		ActivityCommunicator.setMessenger(messenger, this);
	}

	@Override
	public boolean handleMessage(Message msg) {

		Bundle data = msg.getData();
		Serializable obj = data.getSerializable("command");
		if (obj == null || !(obj instanceof ECommandType))
			return false;

		switch ((ECommandType) obj) {
		case SET_HANDLER:
			Messenger messenger = (Messenger) data.getParcelable("value");
			setActivityHandler(messenger);
			break;
		case START_SERVICE:
			startService();
			break;
		case STOP_SERVICE:
			stopService();
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

	public static void handleCommand(Messenger messenger, ECommandType command, Object value) {

		if (messenger == null || command == null)
			return;

		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putSerializable("command", command);

		if (value != null) {
			if (value instanceof String)
				bundle.putString("value", (String) value);
			else if (value instanceof Parcelable)
				bundle.putParcelable("value", (Parcelable) value);
		}

		msg.setData(bundle);
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void startAccessibilityService() {
		Intent intent = new Intent(this, KrakenAccessibilityService.class);
		startService(intent);
	}

}
