package de.tudarmstadt.informatik.tk.android.kraken.provider;

import android.content.Context;
import android.content.SharedPreferences;

public class SocialAuthProvider {

    private static SocialAuthProvider m_socialAuthProvider = null;
    private static SharedPreferences m_sharedPreferences;
    private static Context m_context;

    public static SocialAuthProvider getInstance(Context context) {
        if (m_socialAuthProvider == null) {
            m_context = context;
            m_socialAuthProvider = new SocialAuthProvider();
            m_sharedPreferences = context.getApplicationContext().getSharedPreferences(PreferenceProvider.PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
        return m_socialAuthProvider;
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
