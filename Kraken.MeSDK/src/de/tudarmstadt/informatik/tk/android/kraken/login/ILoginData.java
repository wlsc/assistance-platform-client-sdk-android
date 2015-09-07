package de.tudarmstadt.informatik.tk.android.kraken.login;

import android.app.Activity;
import de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts.AccountsAdapter;
import de.tudarmstadt.informatik.tk.android.kraken.common.SocialNetworkProvider;

public interface ILoginData {

	public void setContext(Activity ctx);

	public String getName();

	public SocialNetworkProvider getSocialNetworkProvider();
	
	public int getLogoId();

	public boolean isLoggedIn();
	
	public void login(AccountsAdapter adapter);

	public void logout(AccountsAdapter adapter);
	
}
