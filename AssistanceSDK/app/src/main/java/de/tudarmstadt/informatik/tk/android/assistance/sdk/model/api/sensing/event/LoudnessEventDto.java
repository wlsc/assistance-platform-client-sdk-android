package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.sensing.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.SensorDto;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 30.08.2015
 */
public class LoudnessEventDto implements SensorDto {

    @SerializedName("loudness")
    @Expose
    private float loudness;
    /**
     * Not-null value.
     */
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public LoudnessEventDto() {
        this.type = DtoType.LOUDNESS;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public LoudnessEventDto(float loudness, String created) {
        this.loudness = loudness;
        this.created = created;
        this.type = DtoType.LOUDNESS;
        this.typeStr = DtoType.getApiName(this.type);
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

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public String toString() {
        return "LoudnessEventRequest{" +
                ", loudness=" + loudness +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
