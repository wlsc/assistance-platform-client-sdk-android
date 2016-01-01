package de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.sensing;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public abstract class CommonEventDaoImpl<T> extends
        CommonDaoImpl<T> implements
        CommonEventDao<T> {

    public CommonEventDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);
    }

    @Override
    public List<SensorDto> convertObjects(List<T> dbSensors) {

        List<SensorDto> result = new ArrayList<>();

        if (dbSensors != null && !dbSensors.isEmpty()) {

            for (T dbSensor : dbSensors) {
                result.add(convertObject(dbSensor));
            }
        }

        return result;
    }

}
