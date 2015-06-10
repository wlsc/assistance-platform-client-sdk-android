package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import android.content.Context;
import android.content.SharedPreferences;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.KrakenSdkSettings;

public class SdkAuthentication {

	private static SdkAuthentication mInstance = null;
	private static SharedPreferences mPrefs;

	private static String mKroken = null;

	public String getKroken() {
		if (mKroken == null && mPrefs != null) {
			mKroken = mPrefs.getString("kroken", null);
		}
		return mKroken;
	}

	public void setKroken(String strKroken) {
        if(strKroken != null && strKroken.equals(getKroken())) {
            return;
        }
        mKroken = strKroken;
        mPrefs.edit().putString("kroken", mKroken).apply();
	}

	public static SdkAuthentication getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new SdkAuthentication();
			mPrefs = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
		}
		return mInstance;
	}


}
