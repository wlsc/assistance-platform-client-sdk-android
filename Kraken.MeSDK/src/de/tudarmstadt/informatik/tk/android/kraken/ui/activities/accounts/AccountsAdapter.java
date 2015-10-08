package de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import de.tudarmstadt.informatik.tk.android.kraken.R;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.SocialLogin;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.impl.FacebookSocialLogin;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.impl.GoogleSocialLogin;
import de.tudarmstadt.informatik.tk.android.kraken.model.social.impl.LiveSocialLogin;

@Deprecated
public class AccountsAdapter extends BaseAdapter {

	LinkedList<SocialLogin> m_liData = new LinkedList<SocialLogin>();
	private Activity m_context;
	private ListView m_listView;
	private GoogleSocialLogin m_GoogleSocialLogin;
	private FacebookSocialLogin m_FacebookSocialLogin;
//	private LoginTwitter m_loginTwitter;
	private LiveSocialLogin m_LiveSocialLogin;

	public AccountsAdapter(Activity context, ListView listView, Intent intent) {
		this.m_context = context;
		this.m_listView = listView;

		m_LiveSocialLogin = LiveSocialLogin.getInstance();
		m_LiveSocialLogin.setContext(context);
		m_liData.push(m_LiveSocialLogin);

//		m_loginTwitter = LoginTwitter.getInstance();
//		m_loginTwitter.setContextToSensors(context);
//		m_loginTwitter.CallbackFromActivity(this, intent);
//		m_liData.push(m_loginTwitter);

		m_GoogleSocialLogin = GoogleSocialLogin.getInstance();
		m_GoogleSocialLogin.setContext(context);
		m_liData.push(m_GoogleSocialLogin);

		m_FacebookSocialLogin = FacebookSocialLogin.getInstance();
		m_FacebookSocialLogin.setContext(context);
		m_liData.push(m_FacebookSocialLogin);
	}

	@Override
	public int getCount() {
		return m_liData.size();
	}

	@Override
	public Object getItem(int position) {
		return m_liData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private boolean isFacebookView(View view) {
		return ((view.findViewById(R.id.btnFacebookAccountLogin) == null) ? false : true);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		final SocialLogin loginData = m_liData.get(position);
		boolean needsFacebookView = "facebook".equalsIgnoreCase(loginData.getName());

		if (view == null || needsFacebookView != isFacebookView(view)) {
			LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (needsFacebookView)
				view = inflater.inflate(R.layout.accountview_facebook, parent, false);
			else
				view = inflater.inflate(R.layout.accountview, parent, false);
		}

		TextView txtAccountName = (TextView) view.findViewById(R.id.account_view_name);
		txtAccountName.setText(loginData.getName());

		//ImageView viewImage = (ImageView) view.findViewById(R.id.account_view_logo);
		//viewImage.setImageResource(loginData.getLogoId());

		// TextView txtAccountDescription = (TextView)
		// view.findViewById(R.id.account_view_description);

		if (!needsFacebookView) {
			Button btn = (Button) view.findViewById(R.id.btnAccountLogin);
            btn.setCompoundDrawablesWithIntrinsicBounds(loginData.getLogoId(), 0, 0 ,0);
			setLoginButtonBehaviour(btn, loginData.isLoggedIn(), loginData);
		}
		if ("facebook".equalsIgnoreCase(loginData.getName()))
			FacebookSocialLogin.prepareFacebookLogin(view);

		return view;
	}

	public void refreshItem(String strName) {
		for (int i = 0; i < getCount(); i++) {
			View item = m_listView.getChildAt(i);
			TextView txtAccountName = (TextView) item.findViewById(R.id.account_view_name);

			String strAccountName = txtAccountName.getText().toString();

			if (strName.equalsIgnoreCase(strAccountName)) {
				for (int j = 0; j < getCount(); j++) {
					SocialLogin loginData = (SocialLogin) getItem(j);
					if (strName.equalsIgnoreCase(loginData.getName())) {
						Button btn = (Button) item.findViewById(R.id.btnAccountLogin);
						setLoginButtonBehaviour(btn, loginData.isLoggedIn(), loginData);
					}
				}
				return;
			}
		}
	}

	private void setLoginButtonBehaviour(Button btn, boolean bLoggedIn, final SocialLogin loginData) {
		final AccountsAdapter adapter = this;
		if (loginData.getName().equals("Google")) {
			if (!bLoggedIn) {
				btn.setText("Login");
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Thread thread = new Thread(new Runnable() {

							@Override
							public void run() {
								loginData.login(adapter);
							}
						});
						thread.setName("LoginThread");
						thread.start();
					}
				});
			} else {
				btn.setText("Logout");
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Thread thread = new Thread(new Runnable() {
							@Override
							public void run() {
								loginData.logout(adapter);
							}
						});
						thread.setName("LogoutThread");
						thread.start();
						;
					}
				});
			}
		} else {
			if (!bLoggedIn) {
				btn.setText("Login");
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						loginData.login(adapter);
					}
				});
			} else {
				btn.setText("Logout");
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						loginData.logout(adapter);
					}
				});

			}
		}
	}
}
