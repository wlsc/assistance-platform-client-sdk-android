package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.AccelerometerSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface AccelerometerSensorDao extends CommonEventDao {

    AccelerometerSensorDto convertObject(DbAccelerometerSensor dbSensor);

}
