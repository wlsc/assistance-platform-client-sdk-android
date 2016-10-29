package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.external;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.DummySensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
@Singleton
public class FacebookSensor extends DummySensor {

    private static final String TAG = FacebookSensor.class.getSimpleName();

    @Inject
    public FacebookSensor(Context context) {
        super(context);
    }

    @Override
    public int getType() {
        return SensorApiType.SOCIAL_FACEBOOK;
    }

    @Override
    public EPushType getPushType() {
        return EPushType.MANUALLY_IMMEDIATE;
    }

    @Override
    public void reset() {
    }

    @Override
    public void dumpData() {
    }

    @Override
    public void updateSensorInterval(Double collectionInterval) {

    }
}
