package de.tudarmstadt.informatik.tk.android.assistance.sdk.ui.activities.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.R;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.social.impl.FacebookSocialLogin;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.social.impl.GoogleSocialLogin;

@Deprecated
public class AccountsActivity extends Activity {

	private UiLifecycleHelper uiHelper;
	private AccountsAdapter m_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounts_activity);

        getActionBar().setDisplayHomeAsUpEnabled(true);

		final ListView listview = (ListView) findViewById(R.id.accounts_listview);

		m_adapter = new AccountsAdapter(this, listview, getIntent());
		listview.setAdapter(m_adapter);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			FacebookSocialLogin.onSessionStateChange(session, state, exception);
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        GoogleSocialLogin.getInstance().handleAuthorizeResult(requestCode, resultCode, data);

		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();

		m_adapter.notifyDataSetChanged();
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.

        // ###### duplicate call :(
		/*Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			LoginFacebook.onSessionStateChange(session, session.getState(), null);
		}*/

		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
