package de.tudarmstadt.informatik.tk.android.kraken.model.social;

import android.app.Activity;

import de.tudarmstadt.informatik.tk.android.kraken.model.enums.SocialNetworkEnum;
import de.tudarmstadt.informatik.tk.android.kraken.ui.activities.accounts.AccountsAdapter;

public interface SocialLogin {

    void setContext(Activity ctx);

    String getName();

    SocialNetworkEnum getSocialNetworkProvider();

    int getLogoId();

    boolean isLoggedIn();

    void login(AccountsAdapter adapter);

    void logout(AccountsAdapter adapter);

}
