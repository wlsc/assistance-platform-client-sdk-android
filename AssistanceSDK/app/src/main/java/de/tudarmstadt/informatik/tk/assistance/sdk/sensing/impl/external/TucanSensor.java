package de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.external;

import android.content.Context;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.assistance.sdk.sensing.impl.DummySensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public final class TucanSensor extends DummySensor {

    private static final String TAG = TucanSensor.class.getSimpleName();

    private static TucanSensor INSTANCE;

    private TucanSensor(Context context) {
        super(context);
    }

    public static TucanSensor getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new TucanSensor(context);
        }

        return INSTANCE;
    }

    @Override
    public int getType() {
        return SensorApiType.UNI_TUCAN;
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
