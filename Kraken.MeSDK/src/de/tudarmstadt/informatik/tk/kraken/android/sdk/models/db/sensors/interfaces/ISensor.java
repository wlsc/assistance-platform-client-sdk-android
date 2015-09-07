package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.interfaces;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.EPushType;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.communication.SensorData;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.db.DaoSession;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.models.db.sensors.ESensorType;


public interface ISensor {

    public void startSensor();

    public void stopSensor();

    public ESensorType getSensorType();

    public void setContext(Context context);

    public boolean isDisabledByUser();

    public void setDisabledByUser(boolean bDisabled);

    public boolean isDisabledBySystem();

    public void setDisabledBySystem(boolean bDisabled);

    public SensorData flushData(DaoSession daoSession) throws JSONException;

    public ApiMessage.DataWrapper flushDataRetro();

    public SensorData flushData(DaoSession daoSession, String strFullqualifiedSensorClassName) throws JSONException;

    public ApiMessage.DataWrapper flushDataRetro(String strFullqualifiedSensorClassName);

    public EPushType getPushType();

    //	public MessageType getMessageType();
    public void configure(JSONObject json);

    public int getPushIntervalInMin();

    public void setPushIntervalInMin(int value);

    public boolean isRunning();

    void removeDataFromDb(List<? extends IDbSensor> liSensorData, String strFullqualifiedSensorClassName);

    void setDaoSession(DaoSession daoSession);

}
