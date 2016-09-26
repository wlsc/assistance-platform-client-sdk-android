package de.tudarmstadt.informatik.tk.assistance.sdk.provider;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.provider.social.FacebookProvider;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.12.2015
 */
public final class SocialProvider {

    private static final String TAG = SocialProvider.class.getSimpleName();

    private static SocialProvider INSTANCE;

    private static Context mContext;

    private SocialProvider(Context context) {
        this.mContext = context;
    }

    public static SocialProvider getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new SocialProvider(context);
        }

        mContext = context;

        return INSTANCE;
    }

    /**
     * Provides implementation for Facebook provider
     *
     * @return
     */
    public FacebookProvider getFacebookProvider() {
        return FacebookProvider.getInstance(mContext);
    }
}
