package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.ECommandType;
import de.tudarmstadt.informatik.tk.android.kraken.service.HarvesterService;
import de.tudarmstadt.informatik.tk.android.kraken.util.DeviceUtils;

/**
 * @author Karsten Planz
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 28.06.2015
 */
public class HarvesterServiceProvider implements Handler.Callback {

    private static final String TAG = HarvesterServiceProvider.class.getSimpleName();

    private static HarvesterServiceProvider INSTANCE;

    private final Context mContext;
    private final Intent mSensingIntent;
    private Messenger mMessenger;

    private boolean isServiceBound = false;

//    protected ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        private final String TAG = ServiceConnection.class.getSimpleName();
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            isServiceBound = false;
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            Log.d(TAG, "Service was connected to binder");
//
//            mMessenger = new Messenger(service);
//
//            KrakenService.sendCommand(
//                    mMessenger,
//                    ECommandType.SET_HANDLER,
//                    new Messenger(new Handler(KrakenServiceManager.this)));
//
//            isServiceBound = true;
//
//        }
//    };

    private HarvesterServiceProvider(Context context) {
        mContext = context;
        mSensingIntent = new Intent(mContext, HarvesterService.class);
    }

    public static HarvesterServiceProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new HarvesterServiceProvider(context);
        }

        return INSTANCE;
    }

    public boolean isServiceRunning() {
        return DeviceUtils.isServiceRunning(mContext, HarvesterService.class);
    }

    /**
     * Starts sensing service
     */
    public void startSensingService() {
        startHarvestingWithIcon(true);
    }

    /**
     * Stops sensing service
     */
    public void stopSensingService() {

        Intent intent = new Intent(mContext, HarvesterService.class);
        intent.putExtra("command", ECommandType.STOP_SERVICE);
        mContext.stopService(intent);
    }

    public void startHarvestingWithIcon(boolean show) {

        Intent intent = new Intent(mContext, HarvesterService.class);
        intent.putExtra("showIcon", show);
        mContext.startService(intent);
    }

    public boolean isServiceBound() {
        return isServiceBound;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
