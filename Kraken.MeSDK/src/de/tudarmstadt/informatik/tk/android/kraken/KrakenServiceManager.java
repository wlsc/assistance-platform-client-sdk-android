package de.tudarmstadt.informatik.tk.android.kraken;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ECommandType;
import de.tudarmstadt.informatik.tk.android.kraken.service.KrakenService;

/**
 * @author Karsten Planz
 */
public class KrakenServiceManager implements Handler.Callback {

    private static KrakenServiceManager instance;

    private final Context mContext;
    private final Intent mKrakenIntent;
    private Messenger mMessenger;

    private boolean isServiceBound = false;

    protected ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mMessenger = new Messenger(service);

            KrakenService.sendCommand(
                    mMessenger,
                    ECommandType.SET_HANDLER,
                    new Messenger(new Handler(KrakenServiceManager.this)));

            isServiceBound = true;

        }
    };

    private KrakenServiceManager(Context context) {

        mContext = context;
        mKrakenIntent = new Intent(context, KrakenService.class);
    }

    public static KrakenServiceManager getInstance(Context context) {

        if (instance == null) {
            instance = new KrakenServiceManager(context);
        }

        return instance;
    }

    public boolean isServiceRunning() {

        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (KrakenService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Starts sensing service
     */
    public void startKrakenService() {

        mContext.startService(mKrakenIntent);

        showIcon(true);
    }

    /**
     * Stops sensing service
     */
    public void stopKrakenService() {
        //KrakenService.sendCommand(mMessenger, ECommandType.STOP_SERVICE, null);
        mContext.stopService(mKrakenIntent);
        //unbindKrakenService();
        showIcon(false);
    }

    public void unbindKrakenService() {

        if (isServiceBound) {
            mContext.unbindService(mServiceConnection);
            isServiceBound = false;
        }
    }

    public void showIcon(boolean show) {

        Intent intent = mKrakenIntent;
        intent.putExtra("showIcon", show);
        mContext.startService(intent);

        /*
        if(show) {
            KrakenService.sendCommand(mMessenger, ECommandType.SHOW_ICON, null);
        }
        else {
            KrakenService.sendCommand(mMessenger, ECommandType.HIDE_ICON, null);
        }
        */
    }

    public boolean isServiceBound() {
        return isServiceBound;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
