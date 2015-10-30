package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.LocationSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface LocationSensorDao extends CommonEventDao {

    LocationSensorDto convertObject(DbPositionSensor dbSensor);

}
