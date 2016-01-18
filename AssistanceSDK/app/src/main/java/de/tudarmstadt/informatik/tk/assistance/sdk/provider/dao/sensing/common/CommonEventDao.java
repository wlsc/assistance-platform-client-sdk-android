package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common;

import android.support.annotation.Nullable;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public interface CommonEventDao<T> extends CommonDao<T> {

    @Nullable
    SensorDto convertObject(T sensor);

    List<SensorDto> convertObjects(List<T> dbSensors);

    /**
     * Gets item for deviceId
     *
     * @param deviceId
     * @return
     */
    T getForDevice(Long deviceId);
}
