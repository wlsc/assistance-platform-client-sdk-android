package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * Light sensor request DTO
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 25.10.2015
 */
public class LightSensorDto implements SensorDto {

    @SerializedName("value")
    @Expose
    private Float value;

    @SerializedName("accuracy")
    @Expose
    private Integer accuracy;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public LightSensorDto() {
        this.type = SensorApiType.LIGHT;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public LightSensorDto(Float value, Integer accuracy, String created, String typeStr, int type) {
        this.value = value;
        this.accuracy = accuracy;
        this.created = created;
        this.typeStr = typeStr;
        this.type = type;
        this.type = SensorApiType.LIGHT;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Float getValue() {
        return this.value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "LightSensorRequest{" +
                ", value=" + value +
                ", accuracy=" + accuracy +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
