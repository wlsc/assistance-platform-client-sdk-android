package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.periodic.incomplete;

import android.Manifest.permission;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.Date;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DbAccountReaderSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class AccountsReaderSensor extends AbstractPeriodicSensor {

    private static final String TAG = AccountsReaderSensor.class.getSimpleName();

    public static final int INTERVAL_IN_SEC = 3600;
    public static final char SEMICOLON = ';';

    private AccountManager mAccountManager;

    private String currentAccountValue;

    public AccountsReaderSensor(Context context) {
        super(context);

        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void dumpData() {

        if (currentAccountValue == null || currentAccountValue.isEmpty()) {
            return;
        }

        DbAccountReaderSensor accountReaderEvent = new DbAccountReaderSensor();

        accountReaderEvent.setTypes(currentAccountValue);
        accountReaderEvent.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getAccountReaderSensorDao().insert(accountReaderEvent);

        Log.d(TAG, "Finished");
    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }

    @Override
    public int getType() {
        return SensorApiType.ACCOUNT_READER;
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

        if (ActivityCompat.checkSelfPermission(context,
                permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
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
                sb.append(accounts[i].type).append(SEMICOLON);
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
