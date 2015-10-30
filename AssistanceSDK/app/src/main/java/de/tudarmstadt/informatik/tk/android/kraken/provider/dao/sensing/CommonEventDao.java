package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.kraken.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.sensor.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CommonEventDao {

    List<? extends IDbSensor> getAll();

    List<? extends IDbSensor> getFirstN(int amount);

    List<? extends IDbSensor> getLastN(int amount);

    long insert(IDbSensor sensor);

    void delete(List<? extends IDbSensor> events);

    List<Sensor> convertObjects(List<? extends IDbSensor> dbSensors);
}
