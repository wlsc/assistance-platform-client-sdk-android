package de.tudarmstadt.informatik.tk.android.kraken.model.social.impl;

import android.view.View;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.R;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.AbstractSocialLogin;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.SocialLogin;
import de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts.AccountsAdapter;
import de.tudarmstadt.informatik.tk.android.kraken.provider.SocialNetworkProvider;

public class FacebookSocialLogin extends AbstractSocialLogin implements SocialLogin {

	private static FacebookSocialLogin m_instance;
	
	private FacebookSocialLogin()
	{
	}
	
	public static FacebookSocialLogin getInstance()
	{
		if (m_instance == null)
			m_instance = new FacebookSocialLogin();
		return m_instance;
	}

	@Override
	public int getLogoId() {
		return R.drawable.logo_facebook;
	}

	@Override
	public void login(AccountsAdapter adapter) {
	}

	// user_likes, user_friends, user_about_me, basic_info

	private static final List<String> m_liEmailPermissions = Arrays.asList("email", "basic_info");

	private static final List<String> m_liExtendenPermissions = Arrays.asList("");
	// "read_friendlists", "read_insights", "read_mailbox",
	// "read_requests", "read_stream", "xmpp_login", "user_online_presence",
	// "friends_online_presence");

	private static List<String> m_liExtendedProfilePermissions = Arrays.asList("user_about_me");

	// , "user_activities", "user_birthday",
	// "user_checkins", "user_education_history", "user_events", "user_groups",
	// "user_hometown", "user_interests", "user_likes",
	// "user_location", "user_notes", "user_photos", "user_questions",
	// "user_relationships", "user_relationship_details",
	// "user_religion_politics", "user_status", "user_subscriptions",
	// "user_videos", "user_website", "user_work_history");

	// not used, automatically granted
	// private static List<String> m_liPublicProfileAndFriendList =
	// Arrays.asList("id", "name"); //, "first_name", "last_name", "link",
	// "username", "gender", "locale", "age_range");

	private static List<String> m_liPermissions = new LinkedList<String>();

	// , "email", "read_friendlists",
	// private static List<String> m_liPermissions = Arrays.asList("", "");
	//
	// "", "",
	// "", "", "", "", "",
	// "", "");
	// "",
	// "friends_about_me", "", "friends_activities",
	// "", "friends_birthday", "",
	// "friends_checkins", "user_education_history",
	// "friends_education_history", "", "friends_events",
	// "", "friends_groups", "",
	// "friends_hometown", "", "friends_interests",
	// "", "friends_likes", "", "friends_location",
	// "", "friends_notes", "", "friends_photos",
	// "", "friends_questions", "",
	// "friends_relationships", "",
	// "friends_relationship_details", "",
	// "friends_religion_politics", "", "friends_status",
	// "", "friends_subscriptions", "",
	// "friends_videos", "", "friends_website", // works till here
	// "", "friends_work_history");

	public static void prepareFacebookLogin(View view) {
		LoginButton authButton = (LoginButton) view.findViewById(R.id.btnFacebookAccountLogin);

		m_liPermissions.addAll(m_liEmailPermissions);
		// m_liPermissions.addAll(m_liExtendenPermissions);
		// m_liPermissions.addAll(m_liExtendedProfilePermissions);
        m_liPermissions.add("read_friendlists");
		authButton.setReadPermissions(m_liPermissions);

		// authButton.setFragment(this);
	}

	public static void onSessionStateChange(Session session, SessionState state, Exception exception) {
		try {
            // Log.d("kraken", "onSessionStateChange " + state.isClosed() + " " + state.isOpened() + " " + session.getAccessToken());
            if(exception != null) {
                throw exception;
            }
			if (state.isOpened()) {

				FacebookSocialLogin.getInstance().handleReceivedToken(session.getAccessToken(), null, m_ctxActivity);

			} else if (state.isClosed()) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SocialNetworkProvider getSocialNetworkProvider() {
		return SocialNetworkProvider.FACEBOOK;
	}

}
