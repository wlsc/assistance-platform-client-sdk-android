package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbGyroscopeSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.GyroscopeSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface GyroscopeSensorDao extends CommonEventDao {

    GyroscopeSensorDto convertObject(DbGyroscopeSensor dbSensor);

}
