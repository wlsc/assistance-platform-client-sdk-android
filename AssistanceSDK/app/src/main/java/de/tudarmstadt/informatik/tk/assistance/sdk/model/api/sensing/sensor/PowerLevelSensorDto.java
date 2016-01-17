package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 06.12.2015
 */
public class PowerLevelSensorDto implements SensorDto {

    @SerializedName("percent")
    @Expose
    private Float percent;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public PowerLevelSensorDto() {
        this.type = SensorApiType.POWER_LEVEL;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public PowerLevelSensorDto(Float percent, String created) {
        this.percent = percent;
        this.created = created;
        this.type = SensorApiType.POWER_LEVEL;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Float getPercent() {
        return this.percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
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
        return "PowerLevelSensorDto{" +
                ", percent=" + percent +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}
