package de.tudarmstadt.informatik.tk.android.kraken.event;

import android.content.Context;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 07.10.2015
 */
public class StopSensingEvent {

    private Context context;

    public StopSensingEvent(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
