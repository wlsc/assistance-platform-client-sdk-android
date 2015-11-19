package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbPositionSensor;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.sensor.LocationSensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface LocationSensorDao extends CommonEventDao {

    LocationSensorDto convertObject(DbPositionSensor dbSensor);

}
