package de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.sensor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.SensorDto;
import de.tudarmstadt.informatik.tk.assistance.sdk.model.api.sensing.SensorApiType;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 31.10.2015
 */
public class PowerStateSensorDto implements SensorDto {

    @SerializedName("isCharging")
    @Expose
    private Boolean isCharging;

    @SerializedName("percent")
    @Expose
    private Float percent;

    @SerializedName("chargingState")
    @Expose
    private Integer chargingState;

    @SerializedName("chargingMode")
    @Expose
    private Integer chargingMode;

    @SerializedName("powerSaveMode")
    @Expose
    private Boolean powerSaveMode;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("type")
    @Expose
    private String typeStr;

    private int type;

    public PowerStateSensorDto() {
        this.type = SensorApiType.POWER_STATE;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public PowerStateSensorDto(Boolean isCharging, Float percent, Integer chargingState, Integer chargingMode, Boolean powerSaveMode, String created) {
        this.isCharging = isCharging;
        this.percent = percent;
        this.chargingState = chargingState;
        this.chargingMode = chargingMode;
        this.powerSaveMode = powerSaveMode;
        this.created = created;
        this.type = SensorApiType.POWER_STATE;
        this.typeStr = SensorApiType.getApiName(this.type);
    }

    public Boolean getIsCharging() {
        return this.isCharging;
    }

    public Float getPercent() {
        return this.percent;
    }

    public Integer getChargingState() {
        return this.chargingState;
    }

    public Integer getChargingMode() {
        return this.chargingMode;
    }

    public Boolean getPowerSaveMode() {
        return this.powerSaveMode;
    }

    @Override
    public String getCreated() {
        return this.created;
    }

    public String getTypeStr() {
        return this.typeStr;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "PowerStateSensorDto{" +
                "isCharging=" + isCharging +
                ", percent=" + percent +
                ", chargingState=" + chargingState +
                ", chargingMode=" + chargingMode +
                ", powerSaveMode=" + powerSaveMode +
                ", created='" + created + '\'' +
                ", typeStr='" + typeStr + '\'' +
                ", type=" + type +
                '}';
    }
}