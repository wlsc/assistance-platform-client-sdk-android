package de.tudarmstadt.informatik.tk.android.kraken.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler.Callback;

import de.tudarmstadt.informatik.tk.android.kraken.service.KrakenServiceManager;

@Deprecated
public abstract class BindingActivity extends Activity implements Callback {

    private KrakenServiceManager mServiceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceManager = KrakenServiceManager.getInstance(this);
    }

    protected KrakenServiceManager getServiceManager() {
        return mServiceManager;
    }

    @Override
	protected void onStart() {
		super.onStart();

        mServiceManager.startKrakenService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

        mServiceManager.unbindKrakenService();
	}

	@Override
	protected void onStop() {
		super.onStop();

        mServiceManager.unbindKrakenService();
	}

}
