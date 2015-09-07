package de.tudarmstadt.informatik.tk.android.kraken;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.tudarmstadt.informatik.tk.android.kraken.communication.SdkAuthentication;


public class KrakenLoginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(KrakenParams.ACTION_LOGIN)) {
            Log.d("kraken", "ACTION_LOGIN");
            Log.d("kraken", "kroken:" + SdkAuthentication.getInstance(context).getKroken());
            Intent krakenKroken = new Intent();
            krakenKroken.setAction(KrakenParams.ACTION_KROKEN);
            krakenKroken.putExtra(KrakenParams.EXTRA_LOGIN_KROKEN, SdkAuthentication.getInstance(context).getKroken());
            context.sendBroadcast(krakenKroken);
        }
        else if(action.equals(KrakenParams.ACTION_KROKEN)) {
            Log.d("kraken", "ACTION_KROKEN");
            if(intent.hasExtra(KrakenParams.EXTRA_LOGIN_KROKEN)) {
                String kroken = intent.getStringExtra(KrakenParams.EXTRA_LOGIN_KROKEN);
                Log.d("kraken", "kroken: " + kroken);
                SdkAuthentication.getInstance(context).setKroken(kroken);
            }
        }
        else if(action.equals(KrakenParams.ACTION_LOGOUT)) {
            Log.d("kraken", "ACTION_LOGOUT");
            SdkAuthentication.getInstance(context).setKroken(null);
        }
    }
}
