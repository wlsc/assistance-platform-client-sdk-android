package de.tudarmstadt.informatik.tk.kraken.android.sdk.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "loudness_event".
 */
public class LoudnessEvent implements de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor {

    private long id;
    private float loudness;
    /** Not-null value. */
    private String created;

    public LoudnessEvent() {
    }

    public LoudnessEvent(long id) {
        this.id = id;
    }

    public LoudnessEvent(long id, float loudness, String created) {
        this.id = id;
        this.loudness = loudness;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(float loudness) {
        this.loudness = loudness;
    }

    /** Not-null value. */
    public String getCreated() {
        return created;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreated(String created) {
        this.created = created;
    }

}