package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.communication.EPushType;
import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.interfaces.Sensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;


public interface ISensor {

    void startSensor();

    void stopSensor();

    ESensorType getSensorType();

    void setContext(Context context);

    boolean isDisabledByUser();

    void setDisabledByUser(boolean bDisabled);

    boolean isDisabledBySystem();

    void setDisabledBySystem(boolean bDisabled);

    boolean flushData(DaoSession daoSession) throws JSONException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

    List<Sensor> flushDataRetro();

    List<Sensor> flushDataRetro(String strFullqualifiedSensorClassName);

    EPushType getPushType();

    //	MessageType getMessageType();
    void configure(JSONObject json);

    int getPushIntervalInMin();

    void setPushIntervalInMin(int value);

    boolean isRunning();

    void removeDataFromDb(List<? extends IDbSensor> liSensorData, String strFullqualifiedSensorClassName);

    void setDaoSession(DaoSession daoSession);

    void reset();
}
