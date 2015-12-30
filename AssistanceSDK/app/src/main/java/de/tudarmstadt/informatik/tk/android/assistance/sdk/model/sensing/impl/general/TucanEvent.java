package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.general;

import android.content.Context;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.event.UpdateSensorIntervalEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.enums.EPushType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.DummyEvent;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.12.2015
 */
public class TucanEvent extends DummyEvent {

    private static final String TAG = TucanEvent.class.getSimpleName();

    private static TucanEvent INSTANCE;

    private TucanEvent(Context context) {
        super(context);
    }

    public static TucanEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new TucanEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public int getType() {
        return DtoType.UNI_TUCAN;
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
    public void onEvent(UpdateSensorIntervalEvent event) {
    }
}
