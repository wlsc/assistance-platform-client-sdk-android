package de.tudarmstadt.informatik.tk.android.assistance.sdk.event;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 10.12.2015
 */
public class UpdateSensorIntervalEvent {

    private final double collectionFrequency;

    public UpdateSensorIntervalEvent(double collectionFrequency) {
        this.collectionFrequency = collectionFrequency;
    }

    public double getCollectionFrequency() {
        return this.collectionFrequency;
    }
}
