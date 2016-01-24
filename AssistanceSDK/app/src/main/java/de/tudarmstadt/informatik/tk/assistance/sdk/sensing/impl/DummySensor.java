package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.ISensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public abstract class DummySensor implements ISensor {

    private static final String TAG = DummySensor.class.getSimpleName();

    private boolean running;

    private Context context;

    private boolean disabledByUser;

    private boolean disabledBySystem;

    public DummySensor(Context context) {
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
