package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api;

import android.support.annotation.Nullable;

/**
 * General sensor request interface
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public interface SensorDto {

    int getType();

    @Nullable
    String getCreated();
}