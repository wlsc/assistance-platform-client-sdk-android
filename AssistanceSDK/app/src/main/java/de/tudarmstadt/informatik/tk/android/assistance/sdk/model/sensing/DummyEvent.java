package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public abstract class DummyEvent implements ISensor {

    private static final String TAG = DummyEvent.class.getSimpleName();

    private boolean running;

    private Context context;

    private boolean disabledByUser;

    private boolean disabledBySystem;

    public DummyEvent(Context context) {
        this.context = context;
    }

    @Override
    public void startSensor() {
        this.running = true;
    }

    @Override
    public void stopSensor() {
        this.running = false;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean isDisabledByUser() {
        return false;
    }

    @Override
    public void setDisabledByUser(boolean isDisabled) {
        this.disabledByUser = isDisabled;
    }

    @Override
    public boolean isDisabledBySystem() {
        return disabledBySystem;
    }

    @Override
    public void setDisabledBySystem(boolean isDisabled) {
        this.disabledBySystem = isDisabled;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    @Override
    public void setDaoSession(DaoSession daoSession) {

    }
}
