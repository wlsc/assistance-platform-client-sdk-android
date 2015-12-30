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
public class FacebookEvent extends DummyEvent {

    private static final String TAG = FacebookEvent.class.getSimpleName();

    private static FacebookEvent INSTANCE;

    public FacebookEvent(Context context) {
        super(context);
    }

    public static FacebookEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new FacebookEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public int getType() {
        return DtoType.SOCIAL_FACEBOOK;
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
