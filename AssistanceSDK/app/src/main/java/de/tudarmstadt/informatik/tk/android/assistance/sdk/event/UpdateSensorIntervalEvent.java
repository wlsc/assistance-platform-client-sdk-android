package de.tudarmstadt.informatik.tk.android.assistance.sdk.event;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 10.12.2015
 */
public class UpdateSensorIntervalEvent {

    private final int dtoType;

    private final double collectionFrequency;

    public UpdateSensorIntervalEvent(int dtoType, double collectionFrequency) {
        this.dtoType = dtoType;
        this.collectionFrequency = collectionFrequency;
    }

    public int getDtoType() {
        return this.dtoType;
    }

    public double getCollectionFrequency() {
        return this.collectionFrequency;
    }
}