package de.tudarmstadt.informatik.tk.android.assistance.sdk.event;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 10.12.2015
 */
public class UpdateSensorIntervalEvent {

    private final int topic;

    private final double collectionFrequency;

    public UpdateSensorIntervalEvent(int topic, double collectionFrequency) {
        this.topic = topic;
        this.collectionFrequency = collectionFrequency;
    }

    public int getTopic() {
        return this.topic;
    }

    public double getCollectionFrequency() {
        return this.collectionFrequency;
    }
}