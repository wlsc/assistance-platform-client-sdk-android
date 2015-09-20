package de.tudarmstadt.informatik.tk.android.kraken.utils;

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
import de.tudarmstadt.informatik.tk.android.kraken.services.KrakenService;

/**
 * @author Karsten Planz
 */
public class KrakenServiceManager implements Handler.Callback {

    private static KrakenServiceManager instance;

    private final Context mContext;
    private final Intent mIntent;
    private Messenger mServiceMessenger;

    private boolean mBound = false;

    protected ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            KrakenService.handleCommand(
                    mServiceMessenger,
                    ECommandType.SET_HANDLER,
                    new Messenger(new Handler(KrakenServiceManager.this)));
            mBound = true;
        }
    };

    private KrakenServiceManager(Context context) {

        mContext = context;
        mIntent = new Intent(context, KrakenService.class);
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

    public void startService() {
        mContext.startService(mIntent);
        //mContext.bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (mBound) {
            mContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }

    public void stopService() {
        //KrakenService.handleCommand(mServiceMessenger, ECommandType.STOP_SERVICE, null);
        mContext.stopService(mIntent);
        //unbindService();
    }

    public void showIcon(boolean show) {

        Intent intent = mIntent;
        intent.putExtra("showIcon", show);
        mContext.startService(intent);

        /*
        if(show) {
            KrakenService.handleCommand(mServiceMessenger, ECommandType.SHOW_ICON, null);
        }
        else {
            KrakenService.handleCommand(mServiceMessenger, ECommandType.HIDE_ICON, null);
        }
        */
    }

    public boolean isBound() {
        return mBound;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
