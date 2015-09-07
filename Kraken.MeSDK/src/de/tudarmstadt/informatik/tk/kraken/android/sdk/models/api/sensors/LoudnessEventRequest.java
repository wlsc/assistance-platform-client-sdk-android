package de.tudarmstadt.informatik.tk.kraken.android.sdk.models.api.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.Sensor;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class LoudnessEventRequest implements Sensor {

    private long id;

    @SerializedName("loudness")
    @Expose
    private float loudness;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    private int type;

    public LoudnessEventRequest() {
    }

    public LoudnessEventRequest(long id) {
        this.id = id;
    }

    public LoudnessEventRequest(long id, float loudness, String created) {
        this.id = id;
        this.loudness = loudness;
        this.created = created;
        this.type = SensorType.LOUDNESS_EVENT;
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

    /**
     * Not-null value.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LoudnessEventRequest{" +
                "id=" + id +
                ", loudness=" + loudness +
                ", created='" + created + '\'' +
                ", type=" + type +
                '}';
    }
}
