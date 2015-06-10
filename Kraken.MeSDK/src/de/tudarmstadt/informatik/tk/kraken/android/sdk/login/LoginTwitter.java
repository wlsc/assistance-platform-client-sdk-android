package de.tudarmstadt.informatik.tk.kraken.android.sdk.login;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import de.tudarmstadt.informatik.tk.kraken.sdk.R;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.ui.activities.accounts.AccountsAdapter;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.Authentication;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.common.SocialNetworkProvider;

public class LoginTwitter extends AbstractLogin implements ILoginData {

	private static LoginTwitter m_instance;
	
	private LoginTwitter()
	{
	}
	
	public static LoginTwitter getInstance()
	{
		if (m_instance == null)
			m_instance = new LoginTwitter();
		return m_instance;
	}
	
	private Activity m_context;

	private ProgressDialog m_progDialog = null;

	/**
	 * Register your here app https://dev.twitter.com/apps/new and get your
	 * consumer key and secret
	 * */
	static String TWITTER_CONSUMER_KEY = "A4Pk7FVAbGyOTfGsdK5XQ";
	static String TWITTER_CONSUMER_SECRET = "UGOzGjw4wxBbKCBMvcMABiMfnlVHCsU3tTF3OYiOmg";

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	// static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	static final String TWITTER_CALLBACK_URL = "oauth://de.tudarmstadt.informatik.tk.kraken.android.sdk.login.twitter.Twitter_oAuth";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	// Twitter
	private static Twitter twitter;
	private static RequestToken requestToken;
	private AccessToken m_accessToken;

	private AccountsAdapter m_adapter;

	@Override
	public int getLogoId() {
		return R.drawable.logo_twitter;
	}

	@Override
	public void login(final AccountsAdapter adapter) {
		m_progDialog = ProgressDialog.show(m_context, "Please wait", "We are logging in...");

		m_adapter = adapter;
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
					m_context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
					dismissProgressDialog();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.setName("TwitterGetTokenThread");
		thread.start();
	}

	public void dismissProgressDialog() {
		m_progDialog.dismiss();
	}

	public void CallbackFromActivity(AccountsAdapter adapter, Intent intent)
	{
		m_adapter = adapter;

        /** This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isLoggedIn()) {
            Uri uri = intent.getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                final String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {

                                // Get the access token
                                m_accessToken = twitter.getOAuthAccessToken(
                                        requestToken, verifier);
                                completeLogin();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.setName("TwitterGetTokenThread");
                    thread.start();

                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
	}
	
    public void completeLogin()
    {
    	Authentication.getInstance(m_context).setAuthentication(getSocialNetworkProvider(), m_accessToken.getToken());
		m_context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				m_adapter.refreshItem(getName());
			}
		});
		
//        long userID = m_accessToken.getUserId();
//        User user;
//		try {
//			user = twitter.showUser(userID);
//			String username = user.getName();
//			System.out.println();
//		} catch (TwitterException e1) {
//			e1.printStackTrace();
//		}

        // Displaying in xml ui
    }

	@Override
	public SocialNetworkProvider getSocialNetworkProvider() {
		return SocialNetworkProvider.TWITTER;
	}
    
}
