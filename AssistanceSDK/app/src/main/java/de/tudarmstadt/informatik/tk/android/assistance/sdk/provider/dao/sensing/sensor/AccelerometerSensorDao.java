package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbAccelerometerSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensor.AccelerometerSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface AccelerometerSensorDao extends CommonEventDao {

    AccelerometerSensorDto convertObject(DbAccelerometerSensor dbSensor);

}
