package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;

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

    List<SensorDto> convertObjects(List<? extends IDbSensor> dbSensors);
}
