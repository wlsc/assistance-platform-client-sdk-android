package de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.kraken.model.api.dto.SensorDto;

/**
 * Light sensor request DTO
 *
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 25.10.2015
 */
public class LightSensorDto implements SensorDto {

    private Long id;

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
        this.type = DtoType.LIGHT;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public LightSensorDto(Long id) {
        this.id = id;
        this.type = DtoType.LIGHT;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public LightSensorDto(Long id, Float value, Integer accuracy, String created, String typeStr, int type) {
        this.id = id;
        this.value = value;
        this.accuracy = accuracy;
        this.created = created;
        this.typeStr = typeStr;
        this.type = type;
        this.type = DtoType.LIGHT;
        this.typeStr = DtoType.getApiName(this.type);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LightSensorRequest{" +
                "id=" + id +
                ", value=" + value +
                ", accuracy=" + accuracy +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
