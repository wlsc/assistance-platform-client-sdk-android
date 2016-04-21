package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao.sensing;

import java.util.ArrayList;
import java.util.Collections;
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

    protected Property[] properties;
    protected static final String DEVICE_ID_FIELD_NAME = "deviceId";

    public CommonEventDaoImpl(AbstractDao<T, Long> dao) {
        super(dao);
        properties = dao.getProperties();
    }

    @Override
    public List<SensorDto> convertObjects(List<T> sensors) {

        if (sensors == null || sensors.isEmpty()) {
            return Collections.emptyList();
        }

        List<SensorDto> result = new ArrayList<>(sensors.size());

        for (T sensor : sensors) {
            result.add(convertObject(sensor));
        }

        return result;
    }

    @Override
    public List<T> getAll(long deviceId) {

        if (deviceId < 0) {
            return Collections.emptyList();
        }

        for (Property property : properties) {
            if (property.name.equals(DEVICE_ID_FIELD_NAME)) {
                return dao
                        .queryBuilder()
                        .where(property.eq(deviceId))
                        .build()
                        .list();
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<T> getFirstN(int amount, long deviceId) {

        if (amount <= 0 || deviceId < 0) {
            return Collections.emptyList();
        }

        for (Property property : properties) {
            if (property.name.equals(DEVICE_ID_FIELD_NAME)) {
                return dao
                        .queryBuilder()
                        .where(property.eq(deviceId))
                        .limit(amount)
                        .build()
                        .list();
            }
        }

        return Collections.emptyList();
    }
}