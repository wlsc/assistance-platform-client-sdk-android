package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.periodic.incomplete;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractPeriodicSensor;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.interfaces.ISensor;


public class AccountsReader extends AbstractPeriodicSensor implements ISensor {

    private AccountManager m_accountManager;

    public AccountsReader(Context context) {
        super(context);
        m_accountManager = AccountManager.get(this.context);
    }

    @Override
    protected void dumpData() {

    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.ONE_TIME_SENSOR_ACCOUNT_READER;
    }

    @Override
    public void reset() {

    }

    @Override
    protected void getData() {
        Account[] accounts = m_accountManager.getAccounts();
        String[] strAccountTypes = new String[accounts.length];

        for (int i = 0; i < accounts.length; i++) {
            strAccountTypes[i] = accounts[i].type;
        }

        // for (Account acc : accounts) {
        // System.out.println("Account-Type: " + acc.type);
        // try {
        // String pass = m_accountManager.getPassword(acc);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }

        if (accounts.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < accounts.length - 1; i++)
                sb.append(accounts[i].type + ";");
            sb.append(accounts[accounts.length - 1]);

//			SensorAccountsReader sensor = new SensorAccountsReader();
//			sensor.setAccountTypes(sb.toString());
//			handleDBEntry(sensor);
        }
    }

    @Override
    protected int getDataIntervallInSec() {
        return 3600;
    }

//	@Override
//	public MessageType getMessageType() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
