package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing.common;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.CommonDaoImpl;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.10.2015
 */
public abstract class CommonEventDaoImpl<T> extends
        CommonDaoImpl<T> implements
        CommonEventDao<T> {

    protected Property deviceIdProperty;

    public CommonEventDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);

        // general deviceId property of an SQLite table
//        this.deviceIdProperty = new WhereCondition.StringCondition()
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


    @Override
    public T getForDevice(Long deviceId) {

        if (deviceId == null) {
            return null;
        }

        return dao
                .queryBuilder()
//                .where(idPr.Id.eq())
                .limit(1)
                .build()
                .unique();
    }
}
