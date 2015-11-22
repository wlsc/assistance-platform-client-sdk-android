package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import java.util.List;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CommonEventDao<T> extends CommonDao<T> {

    SensorDto convertObject(T sensor);

    List<SensorDto> convertObjects(List<T> dbSensors);
}
