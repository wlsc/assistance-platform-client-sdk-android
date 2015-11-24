package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;


public interface ISensor {

    void startSensor();

    void stopSensor();

    int getType();

    void setContext(Context context);

    boolean isDisabledByUser();

    void setDisabledByUser(boolean isDisabled);

    boolean isDisabledBySystem();

    void setDisabledBySystem(boolean isDisabled);

    EPushType getPushType();

    int getPushIntervalInMin();

    void setPushIntervalInMin(int value);

    boolean isRunning();

    void setRunning(boolean isRunning);

    void setDaoSession(DaoSession daoSession);

    void reset();

    void dumpData();
}
