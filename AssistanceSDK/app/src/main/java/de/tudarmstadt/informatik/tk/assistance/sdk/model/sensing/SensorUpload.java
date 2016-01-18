package de.tudarmstadt.informatik.tk.assistance.sdk.model.sensing;

import android.util.SparseArray;

import java.util.List;

import de.tudarmstadt.informatik.tk.assistance.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 18.01.2016
 */
public class SensorUpload {

    private SparseArray<List<? extends IDbSensor>> dbEvents;

    private SparseArray<List<? extends SensorDto>> requestEvents;

    public SensorUpload(SparseArray<List<? extends IDbSensor>> dbEvents, SparseArray<List<? extends SensorDto>> requestEvents) {
        this.dbEvents = dbEvents;
        this.requestEvents = requestEvents;
    }

    public SparseArray<List<? extends IDbSensor>> getDbEvents() {
        return this.dbEvents;
    }

    public SparseArray<List<? extends SensorDto>> getRequestEvents() {
        return this.requestEvents;
    }
}