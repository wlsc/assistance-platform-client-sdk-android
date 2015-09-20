package de.tudarmstadt.informatik.tk.android.kraken.login;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

import java.io.IOException;

import de.tudarmstadt.informatik.tk.android.kraken.R;
import de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts.AccountsAdapter;
import de.tudarmstadt.informatik.tk.android.kraken.common.SocialNetworkProvider;

public class LoginGoogle extends AbstractLogin implements ILoginData {

    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    public static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;

    public static final String SCOPE = "oauth2:email profile https://www.googleapis.com/auth/plus.login";

	private ProgressDialog m_progDialog = null;

	private static LoginGoogle m_instance;

	private LoginGoogle() {
	}

	public static LoginGoogle getInstance() {
		if (m_instance == null)
			m_instance = new LoginGoogle();
		return m_instance;
	}

	@Override
	public int getLogoId() {
		return R.drawable.ic_google;
	}

	@Override
	public void login(final AccountsAdapter adapter) {
        pickUserAccount();

		// Intent intent = new Intent(m_ctxActivity, OAuthAccessTokenActivity.class);
		// m_ctxActivity.startActivity(intent);
	}

    /** Starts an activity in Google Play Services so the user can pick an account */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        m_ctxActivity.startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public void handleAuthorizeResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            show("Unknown error, click the button again");
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            getToken(email);
            return;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
                show("You must pick an account");
            }
            else if(requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
                show("User rejected authorization.");
            }
            return;
        }
        show("Unknown error, click the button again");
    }

    private void show(String text) {
        Toast.makeText(m_ctxActivity, text, Toast.LENGTH_SHORT).show();
    }

    private void getToken(String email) {
        new GetTokenTask(email).execute();
    }

	public void dismissProgressDialog() {
		m_progDialog.dismiss();
	}

	@Override
	public SocialNetworkProvider getSocialNetworkProvider() {
		return SocialNetworkProvider.GOOGLE;
	}

    public class GetTokenTask extends AsyncTask<Void, Void, Void> {

        private final String mEmail;

        public GetTokenTask(String email) {
            this.mEmail = email;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String token = GoogleAuthUtil.getToken(m_ctxActivity, mEmail, LoginGoogle.SCOPE);
                handleReceivedToken(token, null, m_ctxActivity);
            } catch (UserRecoverableAuthException e) {
                Intent intent = e.getIntent();
                m_ctxActivity.startActivityForResult(intent,
                        REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
            } catch (GoogleAuthException | IOException fatalException) {
                fatalException.printStackTrace();
            }
            return null;
        }
    }
}
