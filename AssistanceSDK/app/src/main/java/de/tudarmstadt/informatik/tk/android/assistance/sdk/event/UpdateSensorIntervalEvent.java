package de.tudarmstadt.informatik.tk.android.assistance.sdk.event;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 10.12.2015
 */
public class UpdateSensorIntervalEvent {

    private final int dtoType;

    private final double collectionInterval;

    public UpdateSensorIntervalEvent(int dtoType, double collectionInterval) {
        this.dtoType = dtoType;
        this.collectionInterval = collectionInterval;
    }

    public int getDtoType() {
        return this.dtoType;
    }

    public double getCollectionInterval() {
        return this.collectionInterval;
    }
}