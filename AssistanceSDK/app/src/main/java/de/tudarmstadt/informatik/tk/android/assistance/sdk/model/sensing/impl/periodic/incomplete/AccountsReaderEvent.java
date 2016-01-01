package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic.incomplete;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccountReaderEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.ISensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountsReaderEvent extends AbstractPeriodicEvent implements ISensor {

    private static final String TAG = AccountsReaderEvent.class.getSimpleName();

    public static final int INTERVAL_IN_SEC = 3600;

    private AccountManager mAccountManager;

    private String currentAccountValue;

    public AccountsReaderEvent(Context context) {
        super(context);

        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void dumpData() {

        if (currentAccountValue == null || currentAccountValue.isEmpty()) {
            return;
        }

        DbAccountReaderEvent accountReaderEvent = new DbAccountReaderEvent();

        accountReaderEvent.setTypes(currentAccountValue);
        accountReaderEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getAccountReaderEventDao().insert(accountReaderEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public int getType() {
        return DtoType.ACCOUNT_READER;
    }

    @Override
    public void reset() {

        currentAccountValue = "";
    }

    @Override
    protected void getData() {

        if (mAccountManager == null) {
            return;
        }

        Account[] accounts = mAccountManager.getAccounts();

        if (accounts == null) {
            return;
        }

        int accountsLength = accounts.length;

        String[] strAccountTypes = new String[accountsLength];

        for (int i = 0; i < accountsLength; i++) {
            strAccountTypes[i] = accounts[i].type;
        }

        // for (Account acc : accounts) {
        // System.out.println("Account-Type: " + acc.type);
        // try {
        // String pass = mAccountManager.getPassword(acc);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }

        if (accountsLength > 0) {

            StringBuilder sb = new StringBuilder();

            int accountsLengthMinusOne = accountsLength - 1;

            for (int i = 0; i < accountsLengthMinusOne; i++) {
                sb.append(accounts[i].type).append(";");
            }

            sb.append(accounts[accountsLengthMinusOne]);

            currentAccountValue = sb.toString();

            dumpData();
        }
    }

    @Override
    protected int getDataIntervalInSec() {
        return INTERVAL_IN_SEC;
    }

}
