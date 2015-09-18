package de.tudarmstadt.informatik.tk.android.kraken.communication;

import android.content.Context;
import android.content.SharedPreferences;

import de.tudarmstadt.informatik.tk.android.kraken.KrakenSdkSettings;
import de.tudarmstadt.informatik.tk.android.kraken.common.SocialNetworkProvider;

public class Authentication {

    private static Authentication m_authentication = null;
    private static SharedPreferences m_sharedPreferences;
    private static Context m_context;

    public static Authentication getInstance(Context context) {
        if (m_authentication == null) {
            m_context = context;
            m_authentication = new Authentication();
            m_sharedPreferences = context.getApplicationContext().getSharedPreferences(KrakenSdkSettings.PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        return m_authentication;
    }

    public void setAuthentication(SocialNetworkProvider provider, String strToken) {
        m_sharedPreferences.edit().putString(provider.toString(), strToken).commit();
    }

    public String getAuthentication(SocialNetworkProvider provider) {
        return m_sharedPreferences.getString(provider.toString(), null);
    }

    public void removeAuthentication(SocialNetworkProvider provider) {
        m_sharedPreferences.edit().remove(provider.toString()).commit();
        if (!anyLoggedIn()) {
//            SdkAuthentication.getInstance(m_context).setKroken(null);
//            KrakenSdkAuthentication.getInstance(m_context).logout();
        }
    }

    public boolean anyLoggedIn() {
        for (SocialNetworkProvider provider : SocialNetworkProvider.values()) {
            if (m_sharedPreferences.getString(provider.toString(), null) != null)
                return true;
        }
        return false;
    }

}
