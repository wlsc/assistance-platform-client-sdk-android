package de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.sensor;

import de.tudarmstadt.informatik.tk.android.kraken.db.DbLightSensor;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor.LightSensorDto;
import de.tudarmstadt.informatik.tk.android.kraken.provider.dao.sensing.CommonEventDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface LightSensorDao extends CommonEventDao {

    LightSensorDto convertObject(DbLightSensor dbSensor);

}